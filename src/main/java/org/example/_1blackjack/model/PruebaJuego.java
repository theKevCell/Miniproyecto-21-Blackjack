
package org.example._1blackjack.model;

import java.util.Scanner;

public class PruebaJuego {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        JuegoBlackjack juego = new JuegoBlackjack();

        System.out.println("===== 21 BLACK JACK =====");

        while (juego.puedeIniciarRonda()) {

            System.out.println("\nSaldo disponible: " + juego.getSaldo());
            System.out.print("Ingresa tu apuesta: ");

            int apuesta;

            try {
                apuesta = Integer.parseInt(scanner.nextLine());
                juego.iniciarRonda(apuesta);

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                continue;

            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }

            // Muestra las cartas iniciales
            System.out.println("\nTus cartas: "
                    + juego.getManoJugador().getCartas());

            System.out.println("Puntaje: "
                    + juego.getPuntajeJugador());

            if (juego.getManoJugador().esBlackjackInicial()) {
                System.out.println("¡BLACKJACK!");
            } else if (juego.getManoJugador().esVeinteYMedioInicial()) {
                System.out.println("¡VEINTE Y MEDIO!");
            }

            // Turno del jugador
            while (juego.puedePedirCarta()) {

                System.out.println("\n1. Pedir carta");
                System.out.println("2. Plantarse");
                System.out.print("Selecciona: ");

                String opcion = scanner.nextLine();

                if (opcion.equals("1")) {

                    Carta carta = juego.pedirCarta();

                    System.out.println("\nCarta recibida: " + carta);
                    System.out.println("Tus cartas: "
                            + juego.getManoJugador().getCartas());

                    System.out.println("Puntaje: "
                            + juego.getPuntajeJugador());

                } else if (opcion.equals("2")) {

                    juego.plantarse();

                } else {
                    System.out.println("Opcion no valida.");
                }
            }

            // Resultado de la ronda
            System.out.println("\n===== RESULTADO =====");

            System.out.println("Tus cartas: "
                    + juego.getManoJugador().getCartas());

            System.out.println("Tu puntaje: "
                    + juego.getPuntajeJugador());

            if (juego.getManoComputador().getCantidadCartas() > 0) {

                System.out.println("Cartas del computador: "
                        + juego.getManoComputador().getCartas());

                System.out.println("Puntaje del computador: "
                        + juego.getPuntajeComputador());

            } else {
                System.out.println("El computador no necesitó jugar.");
            }

            System.out.println("Resultado: " + juego.getResultado());
            System.out.println("Saldo final: " + juego.getSaldo());

            if (!juego.puedeIniciarRonda()) {
                break;
            }

            System.out.print("\n¿Deseas jugar otra ronda? (S/N): ");

            if (!scanner.nextLine().equalsIgnoreCase("S")) {
                break;
            }
        }

        System.out.println("\nGracias por jugar 21 Black Jack.");
        scanner.close();
    }
}
