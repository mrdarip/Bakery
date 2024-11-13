package com.mrdarip.bakery.navigation;

import com.mrdarip.bakery.data.entity.Plate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NavController {

    public static void navigateTo(String route, Plate plateContext) {
        System.out.println("Navigating to " + route);

        Parent root = null;
        FXMLLoader loader = new FXMLLoader(NavController.class.getResource(route));
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.out.println("Error loading the FXML file " + route + ", is the route correct?");
        }

        Navigable controller = loader.getController();

        if (plateContext != null && controller instanceof PlateDependantDestiny plateDependantDestiny) {
            plateDependantDestiny.setPlateContext(plateContext);
        }

        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL); //MODAL!!
        newStage.setScene(newScene);
        newStage.setTitle(controller.getScreenTitle());
        newStage.setMinWidth(controller.getMinWidth());
        newStage.setMinHeight(controller.getMinHeight());

        newStage.show();
    }


    public static void navigateTo(String route) {
        System.out.println("Navigating to " + route);

        Parent root = null;
        FXMLLoader loader = new FXMLLoader(NavController.class.getResource(route));
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.out.println("Error loading the FXML file " + route + ", is the route correct?");
        }

        Navigable controller = loader.getController();

        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL); //MODAL!!
        newStage.setScene(newScene);
        newStage.setTitle(controller.getScreenTitle());
        newStage.setMinWidth(controller.getMinWidth());
        newStage.setMinHeight(controller.getMinHeight());

        newStage.show();
    }

}
