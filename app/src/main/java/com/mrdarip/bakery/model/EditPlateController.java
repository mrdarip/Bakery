package com.mrdarip.bakery.model;

import com.mrdarip.bakery.components.LIitemContainer;
import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBInstructionDAO;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBPlateDAO;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBPlateInstructionCRDAO;
import com.mrdarip.bakery.data.DAO.PlateDao;
import com.mrdarip.bakery.data.DAO.PlateInstructionCRDAO;
import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.NavController;
import com.mrdarip.bakery.navigation.Navigable;
import com.mrdarip.bakery.navigation.PlateDependantNavigable;
import com.mrdarip.bakery.navigation.PlateInstructionDependantNavigable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;


public class EditPlateController implements Initializable, PlateInstructionDependantNavigable {
    private final PlateInstructionCRDAO plateInstructionCRDAO = new MariaDBPlateInstructionCRDAO();
    private final InstructionDao instructionDao = new MariaDBInstructionDAO();
    private final PlateDao plateDao = new MariaDBPlateDAO();
    private final List<Instruction> plateInstructions = new ArrayList<>();
    private Plate plateContext;
    private Navigable origin;
    private Stage stage;

    @FXML
    private Button requiredPlateButton;


    @FXML
    private StackPane previewStackPane;

    @FXML
    private TextField plateNameTF;

    @FXML
    private Label starsLbl;

    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox scrollVBox;

    @FXML
    private LIitemContainer instructionsVBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //on text change, update plateContext.
        plateNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (plateContext != null) {
                plateContext.setName(newValue);
            }
        });

        instructionsVBox.setOrigin(this);
    }

    @FXML
    private void DeleteThisPlate(ActionEvent event) {
        plateDao.delete(plateContext);

        this.stage.close();
    }


    @FXML
    void AddInstructionToPlate(ActionEvent event) {
        NavController.navigateTo("/com/mrdarip/bakery/view/SelectElement.fxml", this.plateContext, null, this);
    }

    @FXML
    void ChangePreviewImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );

        fileChooser.setTitle("Select an image for the plate");
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(stage);

        //convert it to Base64
        byte[] fileContent = FileUtils.readFileToByteArray(file);

        // Encode the byte array to a Base64 string
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        plateContext.setPreviewURI("data:image/png;base64," + encodedString);

    }

    @Override
    public void setPlateContext(Plate plateContext) {
        this.plateContext = plateContext;

        rebuildUI();
    }

    @Override
    public void setPlateRequiredPlate(Plate requiredPlate) {
        this.plateContext.setRequiredPlate(requiredPlate);

        updateRequiredPlateButton();
    }

    private void updateRequiredPlateButton() {
        if (plateContext != null && plateContext.getRequiredPlate() != null) {
            requiredPlateButton.setText(plateContext.getRequiredPlate().getName());

            requiredPlateButton.setOnAction((event) -> {
                NavController.navigateTo("/com/mrdarip/bakery/view/EditPlate.fxml", plateContext.getRequiredPlate(), this);
            });
        } else {
            requiredPlateButton.setText("Set Required Plate");

            requiredPlateButton.setOnAction((event) -> {
                NavController.navigateTo("/com/mrdarip/bakery/view/EditPlate.fxml", Plate.getEmptyPlate(), this);
            });
        }
    }

    private void fillPlateInfo() {
        //fill stars
        if (plateContext != null) {
            starsLbl.setText("★".repeat(Math.max(0, plateContext.getValoration())));

            //fill name
            plateNameTF.setText(plateContext.getName());
        }
    }

    private void fillTable() {
        if (plateContext != null) {
            clearPlateInstructions();
            instructionDao.getInstructionsByPlateId(plateContext.getId()).forEach(this::addInstructionToPlate);
        }
    }

    private void clearPlateInstructions() {
        plateInstructions.clear();

        instructionsVBox.getChildren().clear();
    }

    @Override
    public String getScreenTitle() {
        if (plateContext == null) {
            return "New Plate";
        } else {
            return "Plate " + plateContext.getName();
        }
    }

    @Override
    public int getMinWidth() {
        return 450;
    }

    @Override
    public int getMinHeight() {
        return 300;
    }

    @Override
    public void setOrigin(Navigable origin) {
        this.origin = origin;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;

        stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Focused " + getScreenTitle());
            }
        });
    }

    @Override
    public void rebuildUI() {
        if (plateContext != null) {
            updateRequiredPlateButton();

            //remove all nodes of type imageview
            previewStackPane.getChildren().removeIf(node -> node instanceof ImageView);

            //borderpane width x 100
            ImageView previewIV = plateContext.getPreviewImageViewCovering(borderPane.widthProperty(), 100);
            previewStackPane.getChildren().addFirst(previewIV);

        }
        fillTable();
        fillPlateInfo();
    }

    @Override
    public void close() {
        stage.close();
    }

    public void OnSave(ActionEvent actionEvent) {
        for (int i = instructionsVBox.instructions.size() - 1; i >= 0; i--) {
            plateInstructions.set(
                    i,
                    instructionDao.upsert(instructionsVBox.instructions.get(i))
            );
        }

        this.plateContext = plateDao.upsert(plateContext);

        plateInstructionCRDAO.bind(plateContext, plateInstructions, false);

        if (origin instanceof PlateDependantNavigable plateDependantNavigable) {
            plateDependantNavigable.setPlateRequiredPlate(this.plateContext);
        }

        this.stage.close();
    }

    public void OnExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not implemented");
        alert.showAndWait();
    }

    @FXML
    void OnClickEditRequirePlate(ActionEvent event) {
        NavController.navigateTo("/com/mrdarip/bakery/view/SelectElement.fxml", plateContext, this);
    }

    @Override
    public void setInstructionContext(Instruction instructionContext) {
        addInstructionToPlate(instructionContext);
    }

    private void addInstructionToPlate(Instruction instruction) {
        //add to plateInstructions, if it is already there, add it pointing to the same object
        if (plateInstructions.contains(instruction)) {
            Instruction finalInstruction = instruction;
            instruction = plateInstructions.stream()
                    .filter(i -> i.equals(finalInstruction))
                    .findFirst()
                    .orElseThrow();
        }
        plateInstructions.add(instruction);

        instructionsVBox.addNewInstruction(instruction);
    }

    @Override
    public void setPlateInstructionContext(Plate plateContext, Instruction instructionContext) {

    }
}
