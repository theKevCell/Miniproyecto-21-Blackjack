package org.example._1blackjack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        InputStream fuente = MainApp.class.getResourceAsStream("/org/example/_1blackjack/fonts/DoppioOne-Regular.ttf");
        Font.loadFont(fuente, 18);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/org/example/_1blackjack/view/menu-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/org/example/_1blackjack/images/Emblema dorado de blackjack 21.png")));
        stage.setTitle("21 Black Jack");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }
}
