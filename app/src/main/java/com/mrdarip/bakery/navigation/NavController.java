package com.mrdarip.bakery.navigation;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NavController {

    public static void navigateTo(String route, Plate plateContext, Navigable origin) {
        System.out.println("Navigating to " + route + " with plate context " + plateContext);
        FXMLLoader loader = loadFXML(route);
        Parent root = loadLoader(loader, route);
        Navigable controller = loader.getController();

        if (controller instanceof PlateDependantNavigable plateDependantNavigable) {
            plateDependantNavigable.setPlateContext(plateContext);
            plateDependantNavigable.setOrigin(origin);
        }

        CreateScene(controller, root);
    }

    public static void navigateTo(String route, Instruction instructionContext, Navigable origin) {
        System.out.println("Navigating to " + route + " with instruction context " + instructionContext);
        FXMLLoader loader = loadFXML(route);
        Parent root = loadLoader(loader, route);
        Navigable controller = loader.getController();

        if (controller instanceof InstructionDependantNavigable instructionDependantNavigable) {
            instructionDependantNavigable.setInstructionContext(instructionContext);
            instructionDependantNavigable.setOrigin(origin);
        }

        CreateScene(controller, root);
    }

    public static void navigateTo(String route, Plate plateContext, Instruction instructionContext, Navigable origin) {
        System.out.println("Navigating to " + route + " with instruction context " + instructionContext);
        FXMLLoader loader = loadFXML(route);
        Parent root = loadLoader(loader, route);
        Navigable controller = loader.getController();

        if (controller instanceof PlateInstructionDependantNavigable plateInstructionDependantNavigable) {
            plateInstructionDependantNavigable.setPlateInstructionContext(plateContext, instructionContext);
            plateInstructionDependantNavigable.setOrigin(origin);
        }

        CreateScene(controller, root);
    }

    public static void navigateTo(String route, Navigable origin) {
        System.out.println("Navigating to " + route + " without context");
        FXMLLoader loader = loadFXML(route);
        Parent root = loadLoader(loader, route);
        Navigable controller = loader.getController();
        controller.setOrigin(origin);
        CreateScene(controller, root);
    }

    private static FXMLLoader loadFXML(String route) {
        return new FXMLLoader(NavController.class.getResource(route));
    }

    private static Parent loadLoader(FXMLLoader loader, String route) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception e) {
            System.out.println("Error loading the FXML file " + route + " Is the route correct?");
        }
        return root;
    }

    private static void CreateScene(Navigable controller, Parent root) {
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();

        root.getStylesheets().add("/css/style.css");
        newStage.getIcons().add(controller.getScreenIcon());

        controller.setStage(newStage);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setScene(newScene);
        newStage.setTitle(controller.getScreenTitle());
        newStage.setMinWidth(controller.getMinWidth());
        newStage.setMinHeight(controller.getMinHeight());

        newStage.show();
    }
}
