
package com.mrdarip.bakery;

import java.net.URL;
import java.util.ResourceBundle;

import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.NavController;
import com.mrdarip.bakery.navigation.PlateDependantDestiny;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


public class ManagePlateController implements Initializable, PlateDependantDestiny {
    Plate plateContext;
    @FXML
    private Button requiredPlateButton;

    @FXML
    private TreeTableView<?> instructionTTV;

    @FXML
    private StackPane previewStackPane;

    @FXML
    private ImageView previewIV;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void NavigateToRequiredPlate(ActionEvent event) {
    }

    @FXML
    private void DeleteThisPlate(ActionEvent event) {
    }

    @Override
    public void setPlateContext(Plate plateContext) {
        this.plateContext = plateContext;

        if (plateContext != null) {
            if(plateContext.getRequiredPlate() != null) {
                requiredPlateButton.setText(plateContext.getRequiredPlate().getName());
            }
            ImageView previewIV = new ImageView(plateContext.getPreviewURI());


            previewStackPane.getChildren().addFirst(previewIV);
        }
    }

    @Override
    public String getScreenTitle() {
        if(plateContext == null) {
            return "New Plate";
        } else {
            return "Plate " + plateContext.getName();
        }
    }

    @Override
    public int getMinWidth() {
        return 500;
    }

    @Override
    public int getMinHeight() {
        return 100;
    }
}
