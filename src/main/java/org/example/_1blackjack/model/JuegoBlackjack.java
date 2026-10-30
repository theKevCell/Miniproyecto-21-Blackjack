package org.example._1blackjack.model;

public class JuegoBlackjack {

    // Estados posibles de la partida
    public enum EstadoRonda {
        LISTA, TURNO_JUGADOR, TURNO_COMPUTADOR, FINALIZADA
    }

    // Resultados posibles
    public enum ResultadoRonda {
        NINGUNO, VICTORIA, VICTORIA_BLACKJACK, DERROTA, EMPATE
    }

    // Componentes del juego
    private Mazo mazo;
    private Mano manoJugador;
    private Mano manoComputador;

    // Administración de fichas
    private int saldo;
    private int apuestaActual;

    // Control de la partida
    private EstadoRonda estado;
    private ResultadoRonda resultado;
    private boolean apuestaLiquidada;

    // Constructor
    public JuegoBlackjack() {

        mazo = new Mazo();
        manoJugador = new Mano();
        manoComputador = new Mano();

        saldo = 1000;
        apuestaActual = 0;

        estado = EstadoRonda.LISTA;
        resultado = ResultadoRonda.NINGUNO;

        apuestaLiquidada = false;
    }

    // Inicia una ronda y valida la apuesta
    public void iniciarRonda(int apuesta) {

        if (estado != EstadoRonda.LISTA &&
                estado != EstadoRonda.FINALIZADA) {
            throw new IllegalStateException("Ya existe una ronda activa.");
        }

        if (saldo < 10) {
            throw new IllegalStateException("No tienes fichas suficientes.");
        }

        if (apuesta < 10 || apuesta % 10 != 0 || apuesta > saldo) {
            throw new IllegalArgumentException("La apuesta no es válida.");
        }

        // Prepara una nueva ronda
        mazo.reiniciarMazo();
        manoJugador.limpiar();
        manoComputador.limpiar();

        apuestaActual = apuesta;
        saldo -= apuesta;

        resultado = ResultadoRonda.NINGUNO;
        apuestaLiquidada = false;

        estado = EstadoRonda.TURNO_JUGADOR;

        // El jugador recibe sus dos primeras cartas
        manoJugador.agregarCarta(mazo.repartirCarta());
        manoJugador.agregarCarta(mazo.repartirCarta());

        // Si obtiene 21 o Blackjack, comienza el computador
        if (manoJugador.esBlackjackInicial()
                || manoJugador.calcularPuntaje() == 21) {
            jugarTurnoComputador();
        }
    }

    // El jugador solicita una carta
    public Carta pedirCarta() {

        if (estado != EstadoRonda.TURNO_JUGADOR) {
            throw new IllegalStateException("No es tu turno.");
        }

        Carta carta = mazo.repartirCarta();
        manoJugador.agregarCarta(carta);

        // Comprueba si el jugador se pasó
        if (manoJugador.estaPasado()) {
            finalizarRonda(ResultadoRonda.DERROTA);
        }

        // Si llega a 21, comienza el turno del computador
        else if (manoJugador.calcularPuntaje() == 21) {
            jugarTurnoComputador();
        }

        return carta;
    }

    // El jugador decide plantarse
    public void plantarse() {

        if (estado != EstadoRonda.TURNO_JUGADOR) {
            throw new IllegalStateException("No es tu turno.");
        }

        jugarTurnoComputador();
    }

    // Ejecuta automáticamente el turno del computador
    private void jugarTurnoComputador() {

        estado = EstadoRonda.TURNO_COMPUTADOR;

        // El computador recibe dos cartas
        manoComputador.agregarCarta(mazo.repartirCarta());
        manoComputador.agregarCarta(mazo.repartirCarta());

        boolean blackjackJugador = manoJugador.esBlackjackInicial();
        boolean blackjackComputador = manoComputador.esBlackjackInicial();

        // Comprueba si ambos tienen Blackjack
        if (blackjackJugador) {

            if (blackjackComputador) {
                finalizarRonda(ResultadoRonda.EMPATE);
            } else {
                finalizarRonda(ResultadoRonda.VICTORIA_BLACKJACK);
            }

            return;
        }

        // Blackjack del computador
        if (blackjackComputador) {
            finalizarRonda(ResultadoRonda.DERROTA);
            return;
        }

        // El computador pide mientras tenga menos de 17
        while (manoComputador.calcularPuntaje() < 17) {

            manoComputador.agregarCarta(mazo.repartirCarta());
        }

        // Compara las dos manos
        compararManos();
    }

    // Determina quién gana la ronda
    private void compararManos() {

        int puntosJugador = manoJugador.calcularPuntaje();
        int puntosComputador = manoComputador.calcularPuntaje();

        // El computador se pasa
        if (manoComputador.estaPasado()) {
            finalizarRonda(ResultadoRonda.VICTORIA);
            return;
        }

        // Ambos tienen 21
        if (puntosJugador == 21 && puntosComputador == 21) {
            finalizarRonda(ResultadoRonda.EMPATE);
            return;
        }

        // El jugador tiene 21
        if (puntosJugador == 21) {
            finalizarRonda(ResultadoRonda.VICTORIA);
            return;
        }

        // El computador tiene 21
        if (puntosComputador == 21) {
            finalizarRonda(ResultadoRonda.DERROTA);
            return;
        }

        // Comprueba Veinte y medio
        boolean veinteJugador = manoJugador.esVeinteYMedioInicial();
        boolean veinteComputador = manoComputador.esVeinteYMedioInicial();

        if (veinteJugador && veinteComputador) {
            finalizarRonda(ResultadoRonda.EMPATE);
        } else if (veinteJugador) {
            finalizarRonda(ResultadoRonda.VICTORIA);
        } else if (veinteComputador) {
            finalizarRonda(ResultadoRonda.DERROTA);
        } else if (puntosJugador > puntosComputador) {
            finalizarRonda(ResultadoRonda.VICTORIA);
        } else if (puntosJugador < puntosComputador) {
            finalizarRonda(ResultadoRonda.DERROTA);
        } else {
            finalizarRonda(ResultadoRonda.EMPATE);
        }
    }

    // Finaliza la ronda y liquida la apuesta una sola vez
    private void finalizarRonda(ResultadoRonda resultado) {

        if (apuestaLiquidada) {
            return;
        }

        this.resultado = resultado;

        switch (resultado) {

            case VICTORIA:
                saldo += apuestaActual * 2;
                break;

            case VICTORIA_BLACKJACK:
                saldo += (apuestaActual * 5) / 2;
                break;

            case EMPATE:
                saldo += apuestaActual;
                break;

            case DERROTA:
                // No se devuelve ninguna ficha
                break;
        }

        apuestaLiquidada = true;
        estado = EstadoRonda.FINALIZADA;
    }

    // Getters para consultar información desde el controlador
    public int getSaldo() {
        return saldo;
    }

    public int getApuestaActual() {
        return apuestaActual;
    }

    public Mano getManoJugador() {
        return manoJugador;
    }

    public Mano getManoComputador() {
        return manoComputador;
    }

    public int getPuntajeJugador() {
        return manoJugador.calcularPuntaje();
    }

    public int getPuntajeComputador() {
        return manoComputador.calcularPuntaje();
    }

    public EstadoRonda getEstado() {
        return estado;
    }

    public ResultadoRonda getResultado() {
        return resultado;
    }

    public boolean puedeIniciarRonda() {
        return (estado == EstadoRonda.LISTA
                || estado == EstadoRonda.FINALIZADA)
                && saldo >= 10;
    }

    public boolean puedePedirCarta() {
        return estado == EstadoRonda.TURNO_JUGADOR;
    }

    public boolean puedePlantarse() {
        return estado == EstadoRonda.TURNO_JUGADOR;
    }
}
