/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mrdarip.bakery.model;

import com.mrdarip.bakery.composables.Card;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBPlateDAO;
import com.mrdarip.bakery.data.DAO.PlateDao;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.NavController;
import com.mrdarip.bakery.navigation.Navigable;
import com.mrdarip.bakery.navigation.PlateDependantNavigable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Navigable origin;
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
        PlatesListVBox.getChildren().addAll(
                new Card(new ImageView(new Image("/img/icon/plus.png")), "New", "Plate", ev -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/ManagePlate.fxml", Plate.EMPTY_PLATE, this);
                }),

                new Card(new ImageView(new Image("/img/icon/cross.png")), "No", "Plate", ev -> {
                    if (origin instanceof PlateDependantNavigable plateDependantNavigable) {
                        plateContext.setRequiredPlate(null);
                        plateDependantNavigable.setSecondaryPlateContext(plateContext);
                    }
                })
        );

        plateDao.getPlatesPage(0, 99999).forEach(plate -> { //TODO: set custom query to remove cycles and itself
            PlatesListVBox.getChildren().add(
                    new Card(
                            new Image(plate.getPreviewURI()),
                            plate.getName(),
                            ev -> {
                                if (origin instanceof PlateDependantNavigable plateDependantNavigable) {
                                    plateContext.setRequiredPlate(plate);
                                    plateDependantNavigable.setSecondaryPlateContext(plateContext);
                                }
                            }
                    )
            );
        });
    }

    @Override
    public void setSecondaryPlateContext(Plate plateContext) {

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

    @Override
    public void setOrigin(Navigable origin) {
        this.origin = origin;
    }
}
