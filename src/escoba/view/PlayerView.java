package escoba.view;

import escoba.events.GameEvent;
import escoba.model.Card;
import escoba.model.Player;
import framework.observer.IObservable;
import framework.observer.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * CLASE: PlayerView (Vista del Jugador)
 *
 * Maneja la ventana y la interfaz gráfica de un jugador.
 * Muestra la mesa, la mano del jugador, y los puntajes.
 *
 * RESPONSABILIDADES:
 * - Crear y mostrar la ventana del jugador
 * - Mostrar el estado del juego (mesa, mano, puntajes)
 * - Capturar input del jugador
 * - Mostrar mensajes, errores y ayuda
 * - Observar cambios en el GameState (Observer pattern)
 *
 * CÓMO MODIFICAR:
 * - Cambiar colores: modificar setBackground() y setForeground()
 * - Cambiar tamaño: modificar setSize()
 * - Cambiar fuente: modificar Font()
 * - Cambiar textos: modificar los strings en displayGameState() y displayHelp()
 */
public class PlayerView implements IObserver {
    // Componentes de la ventana
    private JFrame frame;           // La ventana principal
    private JTextArea textArea;     // Área de texto para mostrar el juego
    private JTextField inputField;  // Campo para escribir comandos
    private String playerName;      // Nombre del jugador
    private int playerNumber;       // Número del jugador (1 o 2)

    /**
     * Constructor de la vista del jugador.
     *
     * @param playerName Nombre del jugador ("Jugador 1", "Jugador 2", etc.)
     * @param playerNumber Número del jugador (1 o 2) para definir el esquema de colores
     * @param x Posición X de la ventana en la pantalla
     * @param y Posición Y de la ventana en la pantalla
     *
     * CÓMO USAR:
     * PlayerView view = new PlayerView("Player 1", 1, 100, 100);
     * view.show();
     */
    public PlayerView(String playerName, int playerNumber, int x, int y) {
        this.playerName = playerName;
        this.playerNumber = playerNumber;
        createWindow(x, y);
    }

    /**
     * Crea la ventana y todos sus componentes.
     *
     * COMPONENTES:
     * - JFrame: La ventana principal
     * - JTextArea: Área de texto (muestra juego)
     * - JTextField: Campo de entrada (jugador escribe comandos)
     * - JScrollPane: Permite scroll en el área de texto
     *
     * CÓMO MODIFICAR APARIENCIA:
     * - Tamaño ventana: cambiar frame.setSize(600, 500)
     * - Color fondo: cambiar textArea.setBackground(Color.BLACK)
     * - Color texto: cambiar textArea.setForeground(Color.GREEN)
     * - Fuente: cambiar Font("Monospaced", Font.PLAIN, 12)
     */
    private void createWindow(int x, int y) {
        // Crear ventana principal
        frame = new JFrame(playerName + " - La Escoba de 15");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);  // Ancho: 600, Alto: 500
        frame.setLocation(x, y);   // Posición en pantalla
        frame.setLayout(new BorderLayout());

        // Definir colores según el jugador
        // Player 1: Soft Blue (fondo oscuro azul, texto celeste claro)
        // Player 2: Soft Green (fondo oscuro verde, texto verde claro)
        Color backgroundColor;
        Color foregroundColor;

        if (playerNumber == 1) {
            // Soft blue theme for Player 1
            backgroundColor = new Color(15, 25, 45);      // Dark blue background
            foregroundColor = new Color(173, 216, 230);   // Light blue text
        } else {
            // Soft green theme for Player 2
            backgroundColor = new Color(15, 35, 25);      // Dark green background
            foregroundColor = new Color(144, 238, 144);   // Light green text
        }

        // Área de texto para mostrar el juego
        textArea = new JTextArea();
        textArea.setEditable(false);  // No se puede editar
        textArea.setBackground(backgroundColor);
        textArea.setForeground(foregroundColor);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Panel de scroll para el área de texto
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior para input del jugador
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.DARK_GRAY);

        // Etiqueta con el nombre del jugador
        JLabel label = new JLabel(playerName + " > ");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 12));
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        inputPanel.add(label, BorderLayout.WEST);

        // Campo de texto para escribir comandos
        inputField = new JTextField();
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.WHITE);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        inputPanel.add(inputField, BorderLayout.CENTER);

        frame.add(inputPanel, BorderLayout.SOUTH);
    }

    /**
     * Muestra la ventana en la pantalla.
     *
     * CUÁNDO USAR:
     * Llamar después de crear la vista para que sea visible.
     */
    public void show() {
        frame.setVisible(true);
        inputField.requestFocus();  // Pone el cursor en el campo de input
    }

    /**
     * Establece el listener para cuando el jugador presiona Enter.
     *
     * @param listener Función que maneja el input del jugador
     *
     * CÓMO SE USA:
     * En Main.java, se conecta con el GameController para procesar comandos.
     */
    public void setInputListener(ActionListener listener) {
        inputField.addActionListener(listener);
    }

    /**
     * Obtiene el texto que el jugador escribió.
     *
     * @return El texto escrito en el campo de input
     */
    public String getInput() {
        return inputField.getText().trim();
    }

    /**
     * Limpia el campo de input (después de procesar el comando).
     */
    public void clearInput() {
        inputField.setText("");
    }

    /**
     * Agrega texto al área de texto (sin borrar lo anterior).
     *
     * @param text Texto a agregar
     */
    public void appendText(String text) {
        textArea.append(text);
        textArea.setCaretPosition(textArea.getDocument().getLength());  // Auto-scroll al final
    }

    /**
     * Borra todo el texto del área de texto.
     */
    public void clearText() {
        textArea.setText("");
    }

    /**
     * Muestra el estado completo del juego.
     *
     * MUESTRA:
     * - Cartas en la mesa
     * - Cartas en la mano del jugador
     * - Puntajes actuales
     * - De quién es el turno
     * - Comandos disponibles
     *
     * CÓMO MODIFICAR:
     * - Cambiar formato: modificar los strings y StringBuilder
     * - Cambiar información mostrada: agregar/quitar secciones
     * - Cambiar idioma: traducir todos los strings
     *
     * @param table Cartas en la mesa
     * @param player El jugador dueño de esta vista
     * @param opponent El oponente
     * @param deckSize Cartas restantes en el mazo
     * @param isCurrentPlayer Si es el turno de este jugador
     */
    public void displayGameState(List<Card> table, Player player, Player opponent,
                                  int deckSize, boolean isCurrentPlayer) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LA ESCOBA DE 15 ===\n\n");

        // Mostrar cartas en la mesa
        sb.append("CARTAS EN LA MESA:\n");
        if (table.isEmpty()) {
            sb.append("  [mesa vacía]\n");
        } else {
            for (int i = 0; i < table.size(); i++) {
                Card card = table.get(i);
                sb.append("  [" + (i + 1) + "] " + card + " (valor: " + card.getGameValue() + ")\n");
            }
        }
        sb.append("\n");

        // Mostrar mano del jugador
        sb.append("TU MANO:\n");
        List<Card> hand = player.getHand();
        if (hand.isEmpty()) {
            sb.append("  [sin cartas]\n");
        } else {
            for (int i = 0; i < hand.size(); i++) {
                Card card = hand.get(i);
                sb.append("  [" + (i + 1) + "] " + card + " (valor: " + card.getGameValue() + ")\n");
            }
        }
        sb.append("\n");

        // Mostrar puntajes
        sb.append("PUNTAJE:\n");
        sb.append("  " + player.getName() + ": " + player.getCapturedCount() +
                  " cartas, " + player.getEscobasCount() + " escobas\n");
        sb.append("  " + opponent.getName() + ": " + opponent.getCapturedCount() +
                  " cartas, " + opponent.getEscobasCount() + " escobas\n");
        sb.append("  Cartas en el mazo: " + deckSize + "\n\n");

        // Mostrar estado del turno
        if (isCurrentPlayer) {
            sb.append(">>> TU TURNO <<<\n");
            sb.append("Comandos:\n");
            sb.append("  jugar <carta#> - Poner carta en la mesa\n");
            sb.append("  jugar <carta#> llevar <mesa#> <mesa#> ... - Capturar cartas (deben sumar 15)\n");
            sb.append("  Ejemplo: jugar 1 llevar 2 3\n");
            sb.append("  ayuda - Mostrar ayuda\n");
            sb.append("  salir - Salir del juego\n");
        } else {
            sb.append("Esperando al otro jugador...\n");
        }
        sb.append("\n");

        appendText(sb.toString());
    }

    /**
     * Muestra un mensaje normal al jugador.
     *
     * @param message Mensaje a mostrar
     */
    public void displayMessage(String message) {
        appendText(message + "\n");
    }

    /**
     * Muestra un mensaje de error al jugador.
     *
     * @param error Mensaje de error
     */
    public void displayError(String error) {
        appendText("ERROR: " + error + "\n");
    }

    /**
     * Muestra la ayuda completa del juego.
     *
     * CONTENIDO:
     * - Objetivo del juego
     * - Cómo usar los comandos
     * - Ejemplos de jugadas
     * - Sistema de puntos
     * - Qué es una escoba
     *
     * CÓMO MODIFICAR:
     * - Traducir: cambiar todos los strings
     * - Agregar información: agregar más líneas al StringBuilder
     * - Cambiar formato: modificar los saltos de línea y espacios
     */
    public void displayHelp() {
        StringBuilder help = new StringBuilder();
        help.append("\n=== CÓMO JUGAR LA ESCOBA DE 15 ===\n\n");

        help.append("OBJETIVO:\n");
        help.append("  Capturar cartas de la mesa que sumen exactamente 15.\n");
        help.append("  Cuando juegas una carta, se combina con las cartas de la mesa que elijas.\n");
        help.append("  El total (tu carta + cartas de mesa) debe ser igual a 15.\n\n");

        help.append("VALORES DE LAS CARTAS:\n");
        help.append("  - Cartas 1-7: valen su número (1, 2, 3, 4, 5, 6, 7)\n");
        help.append("  - Sota: vale 8 puntos\n");
        help.append("  - Caballo: vale 9 puntos\n");
        help.append("  - Rey: vale 10 puntos\n\n");

        help.append("COMANDOS:\n");
        help.append("  jugar 1\n");
        help.append("    - Juega la carta #1 de tu mano en la mesa\n");
        help.append("    - Úsalo cuando no puedes hacer 15\n\n");

        help.append("  jugar 1 llevar 2 3\n");
        help.append("    - Juega carta #1 y captura cartas #2 y #3 de la mesa\n");
        help.append("    - ¡Carta 1 + Carta 2 + Carta 3 debe sumar 15!\n\n");

        help.append("EJEMPLO:\n");
        help.append("  Mesa: [1] 7 de Oro (7), [2] 5 de Espada (5)\n");
        help.append("  Tu mano: [1] 3 de Basto (3)\n");
        help.append("  Escribes: jugar 1 llevar 1 2\n");
        help.append("  Resultado: 3 + 7 + 5 = 15 → ¡Capturas las tres cartas!\n\n");

        help.append("ESCOBA:\n");
        help.append("  Si vacías la mesa (no quedan cartas), ¡haces una ESCOBA!\n");
        help.append("  Cada escoba = 1 punto al final.\n\n");

        help.append("PUNTOS (al final del juego):\n");
        help.append("  - 1 punto por cada escoba\n");
        help.append("  - 1 punto por más cartas capturadas\n");
        help.append("  - 1 punto por más cartas de Oro\n");
        help.append("  - 1 punto por tener el 7 de Oro\n");
        help.append("  - 1 punto por más 7s\n\n");

        appendText(help.toString());
    }

    /**
     * Observer pattern update method.
     * Called when the GameState (observable) changes.
     *
     * @param observable The GameState that changed
     * @param event The event that occurred (GameEvent enum)
     */
    @Override
    public void update(IObservable observable, Object event) {
        if (event instanceof GameEvent) {
            GameEvent gameEvent = (GameEvent) event;

            // Handle different game events
            switch (gameEvent) {
                case GAME_STARTED:
                    clearText();
                    displayMessage("=== NUEVO JUEGO INICIADO ===\n");
                    break;

                case CARDS_DEALT:
                    displayMessage("--- Nuevas cartas repartidas ---\n");
                    break;

                case TURN_SWITCHED:
                    // View will be refreshed by controller
                    break;

                case TABLE_UPDATED:
                    // View will be refreshed by controller
                    break;

                case GAME_OVER:
                    displayMessage("\n=== EL JUEGO HA TERMINADO ===\n");
                    break;

                default:
                    // Other events handled by controller directly
                    break;
            }
        }
    }
}
