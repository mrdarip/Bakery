package com.mrdarip.bakery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primeraEscena) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ejemplo.fxml"));

        Scene scene = new Scene(root);
        
        primeraEscena.setMinHeight(300.);
        primeraEscena.setMinWidth(400.);
        primeraEscena.setScene(scene);
        primeraEscena.setTitle("Bakery");
        primeraEscena.show();

    }
}
