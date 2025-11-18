package escoba.controller;

import escoba.game.GameState;
import escoba.game.ResultadoJugada;
import escoba.view.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final GameState gameState;
    private final PlayerView view1;
    private final PlayerView view2;

    public GameController(GameState gameState, PlayerView view1, PlayerView view2) {
        this.gameState = gameState;
        this.view1 = view1;
        this.view2 = view2;

        gameState.addObserver(view1);
        gameState.addObserver(view2);
    }

    public void iniciarJuego() {
        gameState.startNewGame();
        actualizarAmbasVistas();
    }

    public void procesarInputJugador(int numeroJugador, String input) {
        input = input.toLowerCase().trim();
        if (input.isEmpty()) {
            return;
        }

        PlayerView vista = obtenerVista(numeroJugador);

        // Manejar comando salir
        if (input.equals("salir") || input.equals("quit")) {
            System.exit(0);
            return;
        }

        // Manejar comando ayuda - preguntar al modelo por comandos disponibles
        if (input.equals("ayuda") || input.equals("help")) {
            mostrarAyuda(numeroJugador);
            return;
        }

        // Manejar comando nueva partida - solo válido cuando el juego terminó
        if (input.equals("nueva") || input.equals("new")) {
            if (gameState.isGameOver()) {
                vista.displayMessage("Iniciando nueva partida...");
                iniciarJuego();
            } else {
                vista.displayError("No se puede iniciar nueva partida mientras el juego está en curso.");
            }
            return;
        }

        // Manejar comandos de juego - solo válidos durante el juego
        if (input.startsWith("jugar ") || input.startsWith("play ")) {
            // Preguntar al modelo si el jugador puede actuar
            if (!gameState.puedeJugadorActuar(numeroJugador)) {
                if (gameState.isGameOver()) {
                    vista.displayError("El juego terminó. Escribe 'nueva' para iniciar una nueva partida.");
                } else {
                    vista.displayError("¡No es tu turno!");
                }
                return;
            }
            manejarComandoJugar(numeroJugador, input);
            return;
        }

        // Comando inválido
        vista.displayError("Comando desconocido. Escribe 'ayuda' para ver instrucciones.");
    }

    private void manejarComandoJugar(int numeroJugador, String input) {
        PlayerView vista = obtenerVista(numeroJugador);
        String[] partes = input.split(" ");

        if (partes.length < 2) {
            vista.displayError("Uso: jugar <carta#> [llevar <mesa#> ...]");
            return;
        }

        try {
            int indiceCarta = Integer.parseInt(partes[1]) - 1;
            // Verificar si hay captura
            if (partes.length > 2 && (partes[2].equals("llevar") || partes[2].equals("take"))) {
                // Parsear índices de mesa
                List<Integer> indicesMesa = new ArrayList<>();
                for (int i = 3; i < partes.length; i++) {
                    indicesMesa.add(Integer.parseInt(partes[i]) - 1);
                }
                ejecutarCaptura(numeroJugador, indiceCarta, indicesMesa);
            } else {
                ejecutarColocarCarta(numeroJugador, indiceCarta);
            }

        } catch (NumberFormatException e) {
            vista.displayError("¡Comando inválido! Usa: jugar <carta#> [llevar <mesa#> ...]");
        }
    }

    private void ejecutarColocarCarta(int numeroJugador, int indiceCarta) {
        PlayerView vista = obtenerVista(numeroJugador);
        ResultadoJugada resultado = gameState.jugarCarta(indiceCarta);

        if (resultado.isExito()) {
            vista.displayMessage(resultado.getMensaje());
            if (resultado.isJuegoTerminado()) {
                mostrarFinJuego();
            } else {
                actualizarAmbasVistas();
            }
        } else {
            vista.displayError(resultado.getMensaje());
        }
    }

    private void ejecutarCaptura(int numeroJugador, int indiceCarta, List<Integer> indicesMesa) {
        PlayerView vista = obtenerVista(numeroJugador);
        ResultadoJugada resultado = gameState.intentarCaptura(indiceCarta, indicesMesa);

        if (resultado.isExito()) {
            vista.displayMessage(resultado.getMensaje());
            if (resultado.isEsEscoba()) {
                vista.displayMessage("*** ¡ESCOBA! ***");
            }
            if (resultado.isJuegoTerminado()) {
                mostrarFinJuego();
            } else {
                actualizarAmbasVistas();
            }
        } else {
            vista.displayError(resultado.getMensaje());
        }
    }

    private void mostrarFinJuego() {
        // Obtener resumen del juego desde el modelo
        String[] lineas = gameState.obtenerResumenFinJuego();

        // Mostrar en ambas vistas
        for (String linea : lineas) {
            view1.displayMessage(linea);
            view2.displayMessage(linea);
        }

        // Preguntar al modelo por comandos disponibles y mostrar
        view1.displayMessage("");
        view1.displayMessage("Comandos disponibles: 'nueva' para nueva partida, 'salir' para terminar, 'ayuda' para ayuda");
        view2.displayMessage("");
        view2.displayMessage("Comandos disponibles: 'nueva' para nueva partida, 'salir' para terminar, 'ayuda' para ayuda");
    }

    private void mostrarAyuda(int numeroJugador) {
        PlayerView vista = obtenerVista(numeroJugador);

        // Preguntar al modelo por comandos disponibles según el estado del juego
        String[] comandos = gameState.obtenerComandosDisponibles();

        vista.displayMessage("");
        if (gameState.isGameOver()) {
            vista.displayMessage("=== AYUDA - JUEGO TERMINADO ===");
            vista.displayMessage("");
            vista.displayMessage("El juego ha terminado. Comandos disponibles:");
        } else {
            vista.displayMessage("=== AYUDA - LA ESCOBA DE 15 ===");
            vista.displayMessage("");
            vista.displayMessage("Comandos disponibles:");
        }

        vista.displayMessage("");
        for (String comando : comandos) {
            vista.displayMessage("  " + comando);
        }
        vista.displayMessage("");

        // Refrescar vista si el juego está en curso
        if (!gameState.isGameOver()) {
            actualizarVista(numeroJugador);
        }
    }

    private void actualizarAmbasVistas() {
        actualizarVista(1);
        actualizarVista(2);
    }

    private void actualizarVista(int numeroJugador) {
        PlayerView vista = obtenerVista(numeroJugador);
        escoba.model.Player jugador = (numeroJugador == 1) ? gameState.getPlayer1() : gameState.getPlayer2();
        escoba.model.Player oponente = (numeroJugador == 1) ? gameState.getPlayer2() : gameState.getPlayer1();
        boolean esTurnoActual = gameState.getCurrentPlayerNumber() == numeroJugador;

        vista.displayGameState(gameState.getTable(), jugador, oponente,
                              gameState.getDeckSize(), esTurnoActual);
    }

    private PlayerView obtenerVista(int numeroJugador) {
        return numeroJugador == 1 ? view1 : view2;
    }
}
