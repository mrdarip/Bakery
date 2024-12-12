package com.mrdarip.bakery.model;

import com.mrdarip.bakery.components.Card;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBPlateDAO;
import com.mrdarip.bakery.data.DAO.PlateDao;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.NavController;
import com.mrdarip.bakery.navigation.Navigable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mrdarip
 */
public class MainScreenController implements Initializable, Navigable {

    PlateDao dao = new MariaDBPlateDAO();

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
    private Stage stage;
    @FXML
    private StackPane bgStackPane;

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

        flowPaneScroll.onScrollProperty().set(ev -> {
            double v = flowPaneScroll.getVvalue();

            bgStackPane.setStyle("-fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #202215 0%, #202215 " + (90 - (v * 45.)) + "%, #963c3c " + (95 - (v * 15.)) + "%, #ca5a2e " + (98 - (v * 3.)) + "%, #dfd785 100%);");
        });
    }

    protected void initializeOrderCombo() {
        orderCombo.getItems().removeAll(orderCombo.getItems());
        orderCombo.getItems().addAll("Stars", "Name", "Duration", "Difficulty");
    }

    private void updateOrderToggleIcon() {
        if (orderToggle.isSelected()) {
            orderToggle.setText("↑");
        } else {
            orderToggle.setText("↓");
        }
    }

    private void updatePlatesFlowPane() {
        //platesFlowPane.getChildren().clear();
        platesFlowPane.getChildren().add(new Card(new ImageView(new Image("/img/icon/plus.png")), "New", "Plate", ev -> {
            NavController.navigateTo("/com/mrdarip/bakery/view/EditPlate.fxml", Plate.getEmptyPlate(), this);
        }));
        platesFlowPane.getChildren().addAll(dao.getPlatesPage(0, 0).stream().map(Plate::getAsCard).toList());
    }

    @Override
    public String getScreenTitle() {
        return "Bakery";
    }

    @Override
    public Image getScreenIcon() {
        return new Image("/img/icon/bread.png");
    }

    @Override
    public int getMinWidth() {
        return 400;
    }

    @Override
    public int getMinHeight() {
        return 300;
    }

    @Override
    public void setOrigin(Navigable origin) {

    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void rebuildUI() {

    }

    @Override
    public void close() {
        stage.close();
    }
}
