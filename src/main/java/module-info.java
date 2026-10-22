module org.example._1blackjack {

    requires javafx.controls;
    requires javafx.fxml;

    opens org.example._1blackjack to javafx.fxml;
    exports org.example._1blackjack;
}