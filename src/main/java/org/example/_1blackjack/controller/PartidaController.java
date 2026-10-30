
package org.example._1blackjack.controller;

import javafx.scene.layout.Pane;
import org.example._1blackjack.model.Carta;
import org.example._1blackjack.model.Mano;
import org.example._1blackjack.model.JuegoBlackjack;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PartidaController {

    // Imágenes del computador
    @FXML private ImageView reverseCardAI1;
    @FXML private ImageView cardFinalAI1;
    @FXML private ImageView reverseCardAI2;
    @FXML private ImageView cardFinalAI2;

    // Imágenes del jugador
    @FXML private ImageView reverseCardP1;
    @FXML private ImageView cardFinalP1;
    @FXML private ImageView reverseCardP2;
    @FXML private ImageView cardFinalP2;

    // Contenedores de cartas
    @FXML private Pane jugadorCartasPane;
    @FXML private Pane computadorCartasPane;

    // Campos de información
    @FXML private TextField puntajePlayer;
    @FXML private TextField puntajeIA;
    @FXML private TextField saldoText;
    @FXML private TextField apuestaText;

    // Botones
    @FXML private Button rondaButton;
    @FXML private Button pedirButton;
    @FXML private Button plantarButton;
    @FXML private Button backButton;

    // Mensajes
    @FXML private Label mensajeLabel;

    // Motor del juego
    private JuegoBlackjack juego;

    // Control de cartas adicionales
    private final List<ImageView> cartasAdicionalesJugador = new ArrayList<>();
    private final List<ImageView> cartasAdicionalesIA = new ArrayList<>();

    // Ubicación de las imágenes de las cartas
    private static final String RUTA_CARTAS =
            "/org/example/_1blackjack/images/cards/";


    // Inicialización de la pantalla
    @FXML
    private void initialize() {

        juego = new JuegoBlackjack();

        // Campos que el jugador no puede modificar
        saldoText.setEditable(false);
        puntajePlayer.setEditable(false);
        puntajeIA.setEditable(false);

        // Permite escribir únicamente números enteros
        TextFormatter<String> filtro = new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*")
                        ? change : null
        );

        apuestaText.setTextFormatter(filtro);

        // Estado inicial
        limpiarCartas();
        actualizarVista();

        mensajeLabel.setText("Ingresa tu apuesta para comenzar.");
    }


    // Inicia una nueva ronda
    @FXML
    private void iniciarRonda() {

        int apuesta;

        try {
            apuesta = Integer.parseInt(apuestaText.getText().trim());

        } catch (NumberFormatException e) {
            mensajeLabel.setText("Ingresa una apuesta válida.");
            return;
        }

        try {

            juego.iniciarRonda(apuesta);

            actualizarVista();

            if (juego.getEstado() ==
                    JuegoBlackjack.EstadoRonda.FINALIZADA) {

                mostrarResultado();

            } else if (juego.getManoJugador().esVeinteYMedioInicial()) {

                mensajeLabel.setText("¡Veinte y medio! Puedes pedir o plantarte.");

            } else {

                mensajeLabel.setText("Tu turno. Puedes pedir una carta o plantarte.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {

            mensajeLabel.setText(e.getMessage());
        }
    }


    // El jugador pide una carta
    @FXML
    private void pedirCarta() {

        try {

            Carta carta = juego.pedirCarta();

            actualizarVista();

            if (juego.getEstado() ==
                    JuegoBlackjack.EstadoRonda.FINALIZADA) {

                mostrarResultado();

            } else {

                mensajeLabel.setText("Recibiste: " + carta);
            }

        } catch (IllegalStateException e) {

            mensajeLabel.setText(e.getMessage());
        }
    }


    // El jugador se planta
    @FXML
    private void plantarse() {

        try {

            juego.plantarse();

            actualizarVista();
            mostrarResultado();

        } catch (IllegalStateException e) {

            mensajeLabel.setText(e.getMessage());
        }
    }


    // Actualiza los datos y elementos visuales
    private void actualizarVista() {

        saldoText.setText(String.valueOf(juego.getSaldo()));

        apuestaText.setText(
                juego.getApuestaActual() > 0
                        ? String.valueOf(juego.getApuestaActual())
                        : ""
        );

        // Puntajes
        puntajePlayer.setText(
                juego.getManoJugador().getCantidadCartas() > 0
                        ? String.valueOf(juego.getPuntajeJugador())
                        : "--"
        );

        puntajeIA.setText(
                juego.getManoComputador().getCantidadCartas() > 0
                        ? String.valueOf(juego.getPuntajeComputador())
                        : "--"
        );

        // Cartas del jugador
        renderizarMano(
                juego.getManoJugador(),
                jugadorCartasPane,
                new ImageView[]{cardFinalP1, cardFinalP2},
                new ImageView[]{reverseCardP1, reverseCardP2},
                cartasAdicionalesJugador
        );

// Cartas del computador
        renderizarMano(
                juego.getManoComputador(),
                computadorCartasPane,
                new ImageView[]{cardFinalAI1, cardFinalAI2},
                new ImageView[]{reverseCardAI1, reverseCardAI2},
                cartasAdicionalesIA
        );

        // Determinar qué acciones están disponibles
        boolean rondaActiva =
                juego.getEstado() == JuegoBlackjack.EstadoRonda.TURNO_JUGADOR
                        || juego.getEstado() == JuegoBlackjack.EstadoRonda.TURNO_COMPUTADOR;

        apuestaText.setEditable(!rondaActiva);

        rondaButton.setDisable(!juego.puedeIniciarRonda());
        pedirButton.setDisable(!juego.puedePedirCarta());
        plantarButton.setDisable(!juego.puedePlantarse());
    }


    // Muestra las cartas de cada jugador
    private void renderizarMano(
            Mano mano,
            Pane contenedor,
            ImageView[] frentes,
            ImageView[] reversos,
            List<ImageView> adicionales) {

        List<Carta> cartas = mano.getCartas();

        // Elimina únicamente las cartas adicionales anteriores
        contenedor.getChildren().removeAll(adicionales);
        adicionales.clear();

        // Mostrar las dos primeras cartas
        for (int i = 0; i < 2; i++) {

            reversos[i].setVisible(false);

            if (i < cartas.size()) {

                Image imagen = cargarImagenCarta(cartas.get(i));

                frentes[i].setImage(imagen);
                frentes[i].setVisible(imagen != null);

            } else {

                frentes[i].setImage(null);
                frentes[i].setVisible(false);
            }
        }

        // Mostrar cartas adicionales
        for (int i = 2; i < cartas.size(); i++) {

            Image imagen = cargarImagenCarta(cartas.get(i));

            if (imagen == null) continue;

            ImageView adicional = new ImageView(imagen);

            adicional.setFitWidth(78);
            adicional.setFitHeight(115);
            adicional.setPreserveRatio(true);
            adicional.setSmooth(true);

            adicional.setLayoutX(Math.min(190 + (i - 2) * 16, 218));
            adicional.setLayoutY(72);

            contenedor.getChildren().add(adicional);
            adicionales.add(adicional);
        }
    }


    // Busca la imagen correspondiente a cada carta
    private Image cargarImagenCarta(Carta carta) {

        String valor = switch (carta.getValor()) {
            case "A" -> "ace";
            case "J" -> "jack";
            case "Q" -> "queen";
            case "K" -> "king";
            default -> carta.getValor();
        };

        String palo = switch (carta.getPalo()) {
            case "CORAZONES" -> "hearts";
            case "DIAMANTES" -> "diamonds";
            case "TREBOLES" -> "clubs";
            case "PICAS" -> "spades";
            default -> "";
        };

        // Las figuras y el As de picas tienen sufijo 2
        boolean sufijo2 =
                (valor.equals("ace") && palo.equals("spades"))
                        || valor.equals("jack")
                        || valor.equals("queen")
                        || valor.equals("king");

        String archivo = valor + "_of_" + palo
                + (sufijo2 ? "2" : "") + ".png";

        URL recurso = getClass().getResource(RUTA_CARTAS + archivo);

        if (recurso == null) {
            System.err.println("No se encontró la imagen: " + archivo);
            return null;
        }

        return new Image(recurso.toExternalForm());
    }


    // Limpia las cartas de la pantalla
    private void limpiarCartas() {

        cardFinalP1.setImage(null);
        cardFinalP2.setImage(null);
        cardFinalAI1.setImage(null);
        cardFinalAI2.setImage(null);

        reverseCardP1.setVisible(false);
        reverseCardP2.setVisible(false);
        reverseCardAI1.setVisible(false);
        reverseCardAI2.setVisible(false);

        cardFinalP1.setVisible(false);
        cardFinalP2.setVisible(false);
        cardFinalAI1.setVisible(false);
        cardFinalAI2.setVisible(false);
    }


    // Presenta el resultado de la ronda
    private void mostrarResultado() {

        switch (juego.getResultado()) {

            case VICTORIA ->
                    mensajeLabel.setText("¡Ganaste la ronda!");

            case VICTORIA_BLACKJACK ->
                    mensajeLabel.setText("¡BLACKJACK! ¡Victoria especial!");

            case DERROTA ->
                    mensajeLabel.setText("Perdiste la ronda.");

            case EMPATE ->
                    mensajeLabel.setText("Empate. Tu apuesta ha sido devuelta.");

            default ->
                    mensajeLabel.setText("Partida finalizada.");
        }
    }


    // Regresa al menú principal
    @FXML
    private void volverMenu() {

        boolean rondaActiva =
                juego.getEstado() == JuegoBlackjack.EstadoRonda.TURNO_JUGADOR
                        || juego.getEstado() == JuegoBlackjack.EstadoRonda.TURNO_COMPUTADOR;

        if (rondaActiva) {

            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

            alerta.setTitle("Volver al menú");
            alerta.setHeaderText("Tienes una ronda en curso.");
            alerta.setContentText("¿Deseas abandonar la partida?");

            if (alerta.showAndWait().orElse(ButtonType.CANCEL)
                    != ButtonType.OK) {
                return;
            }
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/example/_1blackjack/view/menu-view.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();

            stage.setScene(new Scene(root, 800, 600));
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (IOException e) {

            e.printStackTrace();
            mensajeLabel.setText("No se pudo regresar al menú.");
        }
    }
}
