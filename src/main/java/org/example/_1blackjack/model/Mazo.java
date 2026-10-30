package org.example._1blackjack.model;

import java.util.Random;

public class Mazo {

    // Arreglo que almacena las 52 cartas
    private Carta[] cartas;

    // Indica cuál es la siguiente carta que se va a repartir
    private int indiceSiguienteCarta;

    // Generador de números aleatorios
    private final Random random = new Random();

    // Constructor
    public Mazo() {
        reiniciarMazo();
    }

    // Crea las 52 cartas y las mezcla
    public void reiniciarMazo() {

        cartas = new Carta[52];
        indiceSiguienteCarta = 0;

        String[] valores = {
                "A", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "J", "Q", "K"
        };

        String[] palos = {
                "CORAZONES", "DIAMANTES",
                "TREBOLES", "PICAS"
        };

        int posicion = 0;

        // Combina cada valor con los cuatro palos
        for (String palo : palos) {
            for (String valor : valores) {

                cartas[posicion] = new Carta(valor, palo);
                posicion++;
            }
        }

        mezclar();
    }

    // Mezcla las cartas aleatoriamente
    private void mezclar() {

        for (int i = cartas.length - 1; i > 0; i--) {

            int aleatorio = random.nextInt(i + 1);

            // Intercambia dos posiciones del arreglo
            Carta temporal = cartas[i];
            cartas[i] = cartas[aleatorio];
            cartas[aleatorio] = temporal;
        }
    }

    // Reparte una carta y avanza a la siguiente posición
    public Carta repartirCarta() {

        if (indiceSiguienteCarta >= cartas.length) {
            throw new IllegalStateException("No quedan cartas en el mazo.");
        }

        return cartas[indiceSiguienteCarta++];
    }

    // Indica cuántas cartas quedan disponibles
    public int getCartasRestantes() {
        return cartas.length - indiceSiguienteCarta;
    }

    // Comprueba si todavía hay cartas disponibles
    public boolean hayCartasDisponibles() {
        return indiceSiguienteCarta < cartas.length;
    }
}
