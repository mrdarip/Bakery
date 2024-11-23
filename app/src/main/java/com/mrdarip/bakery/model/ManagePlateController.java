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
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ManagePlateController implements Initializable, PlateDependantNavigable {
    Plate plateContext;
    InstructionDao instructionDao = new MariaDBInstructionDAO();
    List<Instruction> plateInstructions;

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
            previewIV = plateContext.getPreviewImageViewCovering(borderPane.widthProperty(), 100);


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
        plateInstructions = instructionDao.getInstructionsByPlateId(plateContext.getId());

        TreeItem<Instruction> root = new TreeItem<>(new Instruction(0, null, 0, 0, "Root"));
        for (Instruction instruction : plateInstructions) {
            TreeItem<Instruction> item = createTreeBranch(instruction);
            root.getChildren().add(item);
        }

        TreeTableColumn<Instruction, String> instructionTitleColumn = new TreeTableColumn<>("Instruction");
        instructionTitleColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("instructionText"));
        instructionTitleColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        instructionTitleColumn.setOnEditCommit(event -> {
            event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow()).getValue().setInstructionText(event.getNewValue());
        });

        TreeTableColumn<Instruction, Integer> instructionDurationColumn = new TreeTableColumn<>("Duration");
        instructionDurationColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("duration"));
        instructionDurationColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new IntegerStringConverter()));
        instructionDurationColumn.setOnEditCommit(event -> {
            event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow()).getValue().setDuration(event.getNewValue());
        });

        TreeTableColumn<Instruction, Integer> instructionDifficultyColumn = new TreeTableColumn<>("Difficulty");
        instructionDifficultyColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("difficulty"));
        instructionDifficultyColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new IntegerStringConverter()));
        instructionDifficultyColumn.setOnEditCommit(event -> {
            event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow()).getValue().setDifficulty(event.getNewValue());
        });

        instructionTTV.getColumns().clear();
        instructionTTV.getColumns().addAll(instructionTitleColumn, instructionDurationColumn, instructionDifficultyColumn);
        instructionTTV.setRoot(root);
        instructionTTV.setShowRoot(false);
        instructionTTV.setEditable(true);
    }

    private TreeItem<Instruction> createTreeBranch(Instruction instruction) {
        TreeItem<Instruction> parentTI = new TreeItem<>(instruction);
        Instruction currentInstruction = instruction;
        while (currentInstruction.hasSharperInstruction()) {
            currentInstruction = currentInstruction.getSharperInstruction();
            TreeItem<Instruction> childTI = new TreeItem<>(currentInstruction);

            parentTI.getChildren().add(childTI);

        }

        Instruction addInstruction = new Instruction(-1, null, -1, -1, "Add");
        TreeItem<Instruction> addTI = new TreeItem<>(addInstruction);
        parentTI.getChildren().add(addTI);

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
        return 450;
    }

    @Override
    public int getMinHeight() {
        return 300;
    }

    public void OnSave(ActionEvent actionEvent) {
        //sout each instruction
        instructionTTV.getRoot().getChildren().forEach((instructionTreeItem) -> {
            instructionDao.upsert(instructionTreeItem.getValue());
        });
    }

    public void OnExit(ActionEvent actionEvent) {
    }
}
