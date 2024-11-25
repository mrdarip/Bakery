/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mrdarip.bakery.model;

import com.mrdarip.bakery.composables.Card;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBPlateDAO;
import com.mrdarip.bakery.data.DAO.PlateDao;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.PlateDependantNavigable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mrdarip
 */
public class SelectPlateController implements Initializable, PlateDependantNavigable {

    private final PlateDao plateDao = new MariaDBPlateDAO();
    private Plate plateContext;
    @FXML
    private VBox PlatesListVBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void setPlateContext(Plate plateContext) {
        this.plateContext = plateContext;

        plateDao.getPlatesPage(0, 99999).forEach(plate -> {
            PlatesListVBox.getChildren().add(
                    new Card(
                            new Image(plate.getPreviewURI()),
                            plate.getName(),
                            ev -> {
                                System.out.println("Selected plate: " + plate.getName());
                            }
                    )
            );
        });
    }

    @Override
    public String getScreenTitle() {
        return "Select Plate for " + plateContext.getName();
    }

    @Override
    public int getMinWidth() {
        return 300;
    }

    @Override
    public int getMinHeight() {
        return 500;
    }
}
