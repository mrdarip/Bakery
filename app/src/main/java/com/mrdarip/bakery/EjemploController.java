package com.mrdarip.bakery;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;

/**
 * FXML Controller class
 *
 * @author mrdarip
 */
public class EjemploController implements Initializable {

    @FXML
    private ComboBox<String> orderCombo;
    @FXML
    private TextField queryPlateTF;
    @FXML
    private FlowPane platesFlowPane;
    @FXML
    private ToggleButton orderToggle;
    @FXML
    private ScrollPane flowPaneScroll;
    
    @FXML
    void onOrderToggleChange(ActionEvent event) {
        updateOrderToggleIcon();
    }
    
    @FXML
    void navigateToTop(ActionEvent event) {
        flowPaneScroll.setVvalue(flowPaneScroll.hvalueProperty().doubleValue());
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeOrderCombo();
        
        updatePlatesFlowPane();
        
        updateOrderToggleIcon();
    }    

    protected void initializeOrderCombo() {
        orderCombo.getItems().removeAll(orderCombo.getItems());
        orderCombo.getItems().addAll("Stars", "Name", "Duration", "Difficulty");
        orderCombo.getSelectionModel().select(0);
    }
    
    private void updateOrderToggleIcon(){
        if (orderToggle.isSelected()) {
            orderToggle.setText("↑");
        } else {
            orderToggle.setText("↓");
        }
    }

    private void updatePlatesFlowPane() {
        //platesFlowPane.getChildren().clear();
        
        
    }
}
