
package org.example._1blackjack.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MenuController {

    // Botones del menú
    @FXML
    private Button partidaButton;

    @FXML
    private Button instruccionesButton;

    @FXML
    private Button creditosButton;


    // Abre la pantalla de partida
    @FXML
    private void abrirPartida() {
        cambiarVista("partida-view.fxml", partidaButton);
    }

    // Abre la pantalla de instrucciones
    @FXML
    private void abrirInstrucciones() {
        cambiarVista("instrucciones-view.fxml", instruccionesButton);
    }

    // Abre la pantalla de créditos
    @FXML
    private void abrirCreditos() {
        cambiarVista("creditos-view.fxml", creditosButton);
    }


    // Cambia la vista dentro de la misma ventana
    private void cambiarVista(String archivo, Button botonOrigen) {

        String ruta = "/org/example/_1blackjack/view/" + archivo;
        URL recurso = getClass().getResource(ruta);

        if (recurso == null) {
            mostrarError("No se encontró el archivo: " + archivo);
            return;
        }

        try {

            Parent root = FXMLLoader.load(recurso);

            Stage stage = (Stage) botonOrigen.getScene().getWindow();

            Scene scene = new Scene(root, 800, 600);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (IOException e) {

            e.printStackTrace();
            mostrarError("No se pudo cargar la pantalla: " + archivo);
        }
    }


    // Muestra errores de navegación
    private void mostrarError(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
