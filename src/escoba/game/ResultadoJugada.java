package escoba.game;

import escoba.model.Card;
import java.util.List;


public class ResultadoJugada {
    private final boolean exito;
    private final String mensaje;
    private final boolean juegoTerminado;
    private final boolean esEscoba;

    public ResultadoJugada(boolean exito, String mensaje, boolean juegoTerminado, boolean esEscoba) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.juegoTerminado = juegoTerminado;
        this.esEscoba = esEscoba;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public boolean isEsEscoba() {
        return esEscoba;
    }

    // Métodos estáticos para crear resultados comunes
    public static ResultadoJugada exitoSimple(String mensaje) {
        return new ResultadoJugada(true, mensaje, false, false);
    }

    public static ResultadoJugada exitoConEscoba(String mensaje) {
        return new ResultadoJugada(true, mensaje, false, true);
    }

    public static ResultadoJugada exitoFinJuego(String mensaje) {
        return new ResultadoJugada(true, mensaje, true, false);
    }

    public static ResultadoJugada error(String mensaje) {
        return new ResultadoJugada(false, mensaje, false, false);
    }
}
