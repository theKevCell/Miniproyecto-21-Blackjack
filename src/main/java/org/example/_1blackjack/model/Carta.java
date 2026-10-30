package org.example._1blackjack.model;

    public class Carta {

        // Atributos: valor y palo de la carta
        private String valor;
        private String palo;

        // Constructor: recibe el valor y el palo
        public Carta(String valor, String palo) {

            if (valor == null || palo == null) {
                throw new IllegalArgumentException("La carta debe tener valor y palo.");
            }

            this.valor = valor.trim().toUpperCase();
            this.palo = palo.trim().toUpperCase();

            if (!esValorValido(this.valor)) {
                throw new IllegalArgumentException("Valor de carta no válido.");
            }

            if (!esPaloValido(this.palo)) {
                throw new IllegalArgumentException("Palo de carta no válido.");
            }
        }

        // Valida los valores permitidos en el Blackjack
        private boolean esValorValido(String valor) {
            return switch (valor) {
                case "A", "2", "3", "4", "5", "6", "7",
                     "8", "9", "10", "J", "Q", "K" -> true;
                default -> false;
            };
        }

        // Valida los cuatro palos de la baraja
        private boolean esPaloValido(String palo) {
            return switch (palo) {
                case "CORAZONES", "DIAMANTES",
                     "TREBOLES", "PICAS" -> true;
                default -> false;
            };
        }

        // Devuelve el puntaje de la carta
        public int getPuntos() {
            return switch (valor) {
                case "A" -> 11;
                case "J", "Q", "K" -> 10;
                default -> Integer.parseInt(valor);
            };
        }

        // Indica si la carta es un As
        public boolean esAs() {
            return valor.equals("A");
        }

        // Getters: permiten consultar los atributos
        public String getValor() {
            return valor;
        }

        public String getPalo() {
            return palo;
        }

        // Representación textual de la carta
        @Override
        public String toString() {
            return valor + " de " + palo;
        }
    }


