package org.example._1blackjack.model;

import java.util.LinkedList;
import java.util.List;

public class Mano {

    // Lista enlazada que almacena las cartas
    private LinkedList<Carta> cartas;

    // Constructor
    public Mano() {
        cartas = new LinkedList<>();
    }

    // Agrega una carta a la mano
    public void agregarCarta(Carta carta) {
        if (carta == null) {
            throw new IllegalArgumentException("La carta no puede ser nula.");
        }

        cartas.add(carta);
    }

    // Devuelve las cartas de la mano
    public List<Carta> getCartas() {
        return new LinkedList<>(cartas);
    }

    // Calcula el puntaje ajustando automáticamente los ases
    public int calcularPuntaje() {

        int puntaje = 0;
        int cantidadAses = 0;

        for (Carta carta : cartas) {

            puntaje += carta.getPuntos();

            if (carta.esAs()) {
                cantidadAses++;
            }
        }

        // Convierte los ases de 11 a 1 si supera 21
        while (puntaje > 21 && cantidadAses > 0) {
            puntaje -= 10;
            cantidadAses--;
        }

        return puntaje;
    }

    // Comprueba si las dos cartas iniciales forman Blackjack
    public boolean esBlackjackInicial() {

        if (cartas.size() != 2) {
            return false;
        }

        boolean tieneAs = false;
        boolean tieneDiez = false;

        for (Carta carta : cartas) {

            if (carta.esAs()) {
                tieneAs = true;
            }

            if (carta.getPuntos() == 10) {
                tieneDiez = true;
            }
        }

        return tieneAs && tieneDiez;
    }

    // Reconoce Veinte y medio en las dos primeras cartas
    public boolean esVeinteYMedioInicial() {

        if (cartas.size() < 2) {
            return false;
        }

        Carta primera = cartas.get(0);
        Carta segunda = cartas.get(1);

        return (esFigura(primera) && segunda.getValor().equals("4"))
                || (esFigura(segunda) && primera.getValor().equals("4"));
    }

    // Comprueba si una carta es J, Q o K
    private boolean esFigura(Carta carta) {

        return carta.getValor().equals("J")
                || carta.getValor().equals("Q")
                || carta.getValor().equals("K");
    }

    // Comprueba si el participante superó los 21 puntos
    public boolean estaPasado() {
        return calcularPuntaje() > 21;
    }

    // Elimina todas las cartas de la mano
    public void limpiar() {
        cartas.clear();
    }

    // Devuelve la cantidad de cartas
    public int getCantidadCartas() {
        return cartas.size();
    }
}
