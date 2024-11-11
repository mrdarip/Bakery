package com.mrdarip.bakery;

import com.mrdarip.bakery.data.DAO.PlateDao;
import com.mrdarip.bakery.data.DAO.RAMPlateDao;
import com.mrdarip.bakery.data.entity.Plate;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
    
    PlateDao dao = new RAMPlateDao();

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
        
        platesFlowPane.getChildren().addAll(
            dao.getPlatesPage(0, 0).
                stream().
                map(Plate::getAsCard).
                collect(Collectors.toList())
        );
        
    }
}
