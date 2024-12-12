/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mrdarip.bakery.model;

import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBInstructionDAO;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.Navigable;
import com.mrdarip.bakery.navigation.PlateDependantNavigable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mrdarip
 */
public class PreviewPlateController implements Initializable, PlateDependantNavigable {
    InstructionDao instructionDao = new MariaDBInstructionDAO();

    @FXML
    private HBox mainScrollPaneHBox;

    private Plate plateContext;
    private Stage scene;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void setPlateContext(Plate plateContext) {
        this.plateContext = plateContext;

        Plate requiredPlateToAdd = plateContext;
        do {
            mainScrollPaneHBox.getChildren().addFirst(requiredPlateToAdd.getAsScrollableArticle(instructionDao));

            requiredPlateToAdd = requiredPlateToAdd.getRequiredPlate();
        } while (requiredPlateToAdd != null);
    }

    @Override
    public void setPlateRequiredPlate(Plate requiredPlate) {

    }

    @Override
    public String getScreenTitle() {
        return plateContext.getName() + " recipe preview";
    }

    @Override
    public Image getScreenIcon() {
        if (plateContext != null) {
            return new Image(plateContext.getPreviewURI());
        }
        return new Image("/img/icon/preview.png");
    }

    @Override
    public int getMinWidth() {
        return 600;
    }

    @Override
    public int getMinHeight() {
        return 400;
    }

    @Override
    public void setOrigin(Navigable origin) {
    }

    @Override
    public void setStage(Stage stage) {
        this.scene = stage;
    }

    @Override
    public void rebuildUI() {

    }

    @Override
    public void close() {
        scene.close();
    }
}
