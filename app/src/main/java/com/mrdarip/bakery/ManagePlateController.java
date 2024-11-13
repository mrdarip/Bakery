
package com.mrdarip.bakery;

import java.net.URL;
import java.util.ResourceBundle;

import com.mrdarip.bakery.navigation.NavController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableView;


public class ManagePlateController implements Initializable {

    @FXML
    private Button requiredPlateButton;

    @FXML
    private TreeTableView<?> instructionTTV;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void NavigateToRequiredPlate(ActionEvent event) {
    }

    @FXML
    private void DeleteThisPlate(ActionEvent event) {
    }
    
}
