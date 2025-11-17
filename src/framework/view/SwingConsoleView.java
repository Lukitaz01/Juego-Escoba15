package framework.view;

import framework.mvc.Controller;
import framework.mvc.IView;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Swing-based console view for MVC pattern.
 * Provides a text-based interface using Swing components.
 *
 * Features:
 * - Text output area (JTextPane with styled text)
 * - Text input field (JTextField)
 * - Scrollable output
 * - Command history
 * - Customizable colors and styles
 */
public abstract class SwingConsoleView extends JFrame implements IView {

    protected Controller controller;
    protected JTextPane outputArea;
    protected JTextField inputField;
    protected JScrollPane scrollPane;
    protected StyledDocument document;

    // Styles for different text types
    protected Style normalStyle;
    protected Style errorStyle;
    protected Style successStyle;
    protected Style infoStyle;

    /**
     * Creates a new SwingConsoleView with default settings.
     *
     * @param title The window title
     * @param width The window width
     * @param height The window height
     */
    public SwingConsoleView(String title, int width, int height) {
        super(title);
        initializeUI(width, height);
        setupStyles();
    }

    /**
     * Creates a new SwingConsoleView with default size (800x600).
     *
     * @param title The window title
     */
    public SwingConsoleView(String title) {
        this(title, 800, 600);
    }

    /**
     * Initializes the UI components.
     */
    private void initializeUI(int width, int height) {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Output area (text pane with styles)
        outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        document = outputArea.getStyledDocument();

        scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.DARK_GRAY);

        JLabel promptLabel = new JLabel("> ");
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        promptLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        inputPanel.add(promptLabel, BorderLayout.WEST);

        inputField = new JTextField();
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput(inputField.getText());
                inputField.setText("");
            }
        });
        inputPanel.add(inputField, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.SOUTH);

        // Window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });

        // Center on screen
        setLocationRelativeTo(null);
    }

    /**
     * Sets up text styles for different message types.
     */
    private void setupStyles() {
        normalStyle = outputArea.addStyle("Normal", null);
        StyleConstants.setForeground(normalStyle, Color.WHITE);

        errorStyle = outputArea.addStyle("Error", null);
        StyleConstants.setForeground(errorStyle, Color.RED);
        StyleConstants.setBold(errorStyle, true);

        successStyle = outputArea.addStyle("Success", null);
        StyleConstants.setForeground(successStyle, Color.GREEN);

        infoStyle = outputArea.addStyle("Info", null);
        StyleConstants.setForeground(infoStyle, Color.CYAN);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void display() {
        setVisible(true);
        inputField.requestFocus();
    }

    @Override
    public void hide() {
        super.setVisible(false);
    }

    @Override
    public void showMessage(String message) {
        appendText(message + "\n", normalStyle);
    }

    @Override
    public void showError(String error) {
        appendText("ERROR: " + error + "\n", errorStyle);
    }

    /**
     * Shows a success message.
     *
     * @param message The success message
     */
    public void showSuccess(String message) {
        appendText(message + "\n", successStyle);
    }

    /**
     * Shows an info message.
     *
     * @param message The info message
     */
    public void showInfo(String message) {
        appendText(message + "\n", infoStyle);
    }

    /**
     * Appends text to the output area with a specific style.
     *
     * @param text The text to append
     * @param style The style to use
     */
    protected void appendText(String text, Style style) {
        try {
            document.insertString(document.getLength(), text, style);
            // Auto-scroll to bottom
            outputArea.setCaretPosition(document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the output area.
     */
    public void clear() {
        outputArea.setText("");
    }

    /**
     * Handles user input from the text field.
     * Override this method to process commands.
     *
     * @param input The user input
     */
    protected abstract void handleInput(String input);

    /**
     * Called when the window is closing.
     * Override this method to perform cleanup.
     */
    protected void onWindowClosing() {
        // Default: do nothing
    }

    /**
     * Enables or disables the input field.
     *
     * @param enabled True to enable, false to disable
     */
    public void setInputEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
    }

    /**
     * Gets the current text in the output area.
     *
     * @return The output text
     */
    public String getOutputText() {
        return outputArea.getText();
    }
}
