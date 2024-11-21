package com.mrdarip.bakery.model;

import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBInstructionDAO;
import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.NavController;
import com.mrdarip.bakery.navigation.PlateDependantNavigable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ManagePlateController implements Initializable, PlateDependantNavigable {
    Plate plateContext;
    InstructionDao instructionDao = new MariaDBInstructionDAO();

    @FXML
    private Button requiredPlateButton;

    @FXML
    private TreeTableView<Instruction> instructionTTV;

    @FXML
    private StackPane previewStackPane;

    @FXML
    private ImageView previewIV;

    @FXML
    private TextField plateNameTF;

    @FXML
    private Label starsLbl;

    @FXML
    private BorderPane borderPane;

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
            if (plateContext.getRequiredPlate() != null) {
                requiredPlateButton.setText(plateContext.getRequiredPlate().getName());

                requiredPlateButton.setOnAction((event) -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/ManagePlate.fxml", plateContext.getRequiredPlate());
                });
            }

            //borderpane width x 100
            previewIV = plateContext.getPreviewImageViewCovering(borderPane.widthProperty(), previewStackPane.heightProperty());


            previewStackPane.getChildren().addFirst(previewIV);


            fillTable();

            fillPlateInfo();
        }
    }

    private void fillPlateInfo() {
        //fill stars
        String n = "";
        for (int i = 0; i < plateContext.getValoration(); i++) {
            n += "★";
        }
        starsLbl.setText(n);

        //fill name
        plateNameTF.setText(plateContext.getName());
    }

    private void fillTable() {
        List<Instruction> plateInstructions = instructionDao.getInstructionsByPlateId(plateContext.getId());

        TreeItem<Instruction> root = new TreeItem<>(new Instruction(0, null, 0, 0, "Root"));
        for (Instruction instruction : plateInstructions) {
            TreeItem<Instruction> item = createTreeItem(instruction);
            root.getChildren().add(item);
        }

        TreeTableColumn<Instruction, String> instructionTitleColumn = new TreeTableColumn<>("Instruction");
        instructionTitleColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("instructionText"));

        TreeTableColumn<Instruction, Integer> instructionDurationColumn = new TreeTableColumn<>("Duration");
        instructionDurationColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("duration"));

        TreeTableColumn<Instruction, Integer> instructionDifficultyColumn = new TreeTableColumn<>("Difficulty");
        instructionDifficultyColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("difficulty"));



        instructionTTV.getColumns().clear();
        instructionTTV.getColumns().addAll(instructionTitleColumn, instructionDurationColumn, instructionDifficultyColumn);
        instructionTTV.setRoot(root);
        instructionTTV.setShowRoot(false);
    }

    private TreeItem<Instruction> createTreeItem(Instruction instruction) {
        TreeItem<Instruction> parentTI = new TreeItem<>(instruction);
        Instruction currentInstruction = instruction;
        while (currentInstruction.hasSharperInstruction()) {
            currentInstruction = currentInstruction.getSharperInstruction();
            TreeItem<Instruction> childTI = new TreeItem<>(currentInstruction);

            parentTI.getChildren().add(childTI);

        }
        return parentTI;
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
        return 0;
    }

    @Override
    public int getMinHeight() {
        return 100;
    }
}
