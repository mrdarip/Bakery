package com.mrdarip.bakery.navigation;

import com.mrdarip.bakery.data.entity.Plate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NavController {
    private static Navigable controller;
    private static Parent root;

    public static void navigateTo(String route, Plate plateContext) {
        loadFXML(route);

        if (plateContext != null && controller instanceof PlateDependantDestiny plateDependantDestiny) {
            plateDependantDestiny.setPlateContext(plateContext);
        }

        CreateScene(controller, root);
    }

    public static void navigateTo(String route) {
        loadFXML(route);
        CreateScene(controller, root);
    }

    private static void loadFXML(String route) {
        System.out.println("Navigating to " + route);

        FXMLLoader loader = new FXMLLoader(NavController.class.getResource(route));
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.out.println("Error loading the FXML file " + route + ", is the route correct?");
        }

        controller = loader.getController();
    }

    private static void CreateScene(Navigable controller, Parent root) {
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setScene(newScene);
        newStage.setTitle(controller.getScreenTitle());
        newStage.setMinWidth(controller.getMinWidth());
        newStage.setMinHeight(controller.getMinHeight());

        newStage.show();
    }
}
