package escoba.controller;

import escoba.game.GameState;
import escoba.game.ResultadoJugada;
import escoba.view.PlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para Escoba de 15.
 *
 * RESPONSABILIDAD ÚNICA: Coordinar entre Modelo y Vistas (barandilla).
 *
 * Este controlador NO contiene lógica de negocio.
 * Solo:
 * - Recibe input del usuario
 * - Parsea comandos básicos
 * - Delega TODO al Modelo
 * - Muestra resultados en las Vistas
 */
public class GameController {
    private GameState gameState;
    private PlayerView view1;
    private PlayerView view2;

    public GameController(GameState gameState, PlayerView view1, PlayerView view2) {
        this.gameState = gameState;
        this.view1 = view1;
        this.view2 = view2;

        // Registrar vistas como observadores
        gameState.addObserver(view1);
        gameState.addObserver(view2);
    }

    /**
     * Inicia el juego.
     */
    public void iniciarJuego() {
        gameState.startNewGame();
        actualizarAmbasVistas();
    }

    /**
     * Procesa el input del jugador.
     * SOLO parsea comandos y delega al Modelo.
     */
    public void procesarInputJugador(int numeroJugador, String input) {
        // Verificar si el juego terminó
        if (gameState.isGameOver()) {
            return;
        }

        // Verificar turno
        if (numeroJugador != gameState.getCurrentPlayerNumber()) {
            obtenerVista(numeroJugador).displayError("¡No es tu turno!");
            return;
        }

        input = input.toLowerCase().trim();

        if (input.isEmpty()) {
            return;
        }

        // Comando: salir
        if (input.equals("salir") || input.equals("quit")) {
            System.exit(0);
            return;
        }

        // Comando: ayuda
        if (input.equals("ayuda") || input.equals("help")) {
            obtenerVista(numeroJugador).displayHelp();
            actualizarVista(numeroJugador);
            return;
        }

        // Comando: jugar
        if (input.startsWith("jugar ") || input.startsWith("play ")) {
            manejarComandoJugar(numeroJugador, input);
        } else {
            obtenerVista(numeroJugador).displayError("Comando desconocido. Escribe 'ayuda' para ver instrucciones.");
        }
    }

    /**
     * Maneja el comando "jugar".
     * SOLO parsea y delega al Modelo.
     */
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
                // Solo colocar carta
                ejecutarColocarCarta(numeroJugador, indiceCarta);
            }

        } catch (NumberFormatException e) {
            vista.displayError("¡Comando inválido! Usa: jugar <carta#> [llevar <mesa#> ...]");
        }
    }

    /**
     * Ejecuta colocar carta - DELEGA al Modelo.
     */
    private void ejecutarColocarCarta(int numeroJugador, int indiceCarta) {
        PlayerView vista = obtenerVista(numeroJugador);

        // Delegar al Modelo
        ResultadoJugada resultado = gameState.jugarCarta(indiceCarta);

        // Mostrar resultado
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

    /**
     * Ejecuta captura - DELEGA al Modelo.
     */
    private void ejecutarCaptura(int numeroJugador, int indiceCarta, List<Integer> indicesMesa) {
        PlayerView vista = obtenerVista(numeroJugador);

        // Delegar al Modelo
        ResultadoJugada resultado = gameState.intentarCaptura(indiceCarta, indicesMesa);

        // Mostrar resultado
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

    /**
     * Muestra el fin del juego - DELEGA al Modelo para obtener información.
     */
    private void mostrarFinJuego() {
        // Obtener resumen del Modelo
        String[] lineas = gameState.obtenerResumenFinJuego();

        // Mostrar en ambas vistas
        for (String linea : lineas) {
            view1.displayMessage(linea);
            view2.displayMessage(linea);
        }
    }

    /**
     * Actualiza ambas vistas.
     */
    private void actualizarAmbasVistas() {
        actualizarVista(1);
        actualizarVista(2);
    }

    /**
     * Actualiza una vista específica.
     */
    private void actualizarVista(int numeroJugador) {
        PlayerView vista = obtenerVista(numeroJugador);
        escoba.model.Player jugador = (numeroJugador == 1) ? gameState.getPlayer1() : gameState.getPlayer2();
        escoba.model.Player oponente = (numeroJugador == 1) ? gameState.getPlayer2() : gameState.getPlayer1();
        boolean esTurnoActual = gameState.getCurrentPlayerNumber() == numeroJugador;

        vista.displayGameState(gameState.getTable(), jugador, oponente,
                              gameState.getDeckSize(), esTurnoActual);
    }

    /**
     * Obtiene la vista para un número de jugador.
     */
    private PlayerView obtenerVista(int numeroJugador) {
        return numeroJugador == 1 ? view1 : view2;
    }
}
