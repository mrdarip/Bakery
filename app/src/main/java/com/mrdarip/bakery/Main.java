package com.mrdarip.bakery;

import com.mrdarip.bakery.navigation.NavController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primeraEscena) {
        NavController.navigateTo("/com/mrdarip/bakery/view/MainScreen.fxml", null);
    }
}
