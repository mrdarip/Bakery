package com.mrdarip.bakery.model;

import com.mrdarip.bakery.components.LIitem;
import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBInstructionDAO;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBPlateDAO;
import com.mrdarip.bakery.data.DAO.PlateDao;
import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.NavController;
import com.mrdarip.bakery.navigation.Navigable;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class EditPlateController implements Initializable, PlateDependantNavigable {
    private Plate plateContext;
    private final InstructionDao instructionDao = new MariaDBInstructionDAO();
    private final PlateDao plateDao = new MariaDBPlateDAO();
    private List<Instruction> plateInstructions = new ArrayList<>();
    private Navigable origin;
    private Stage stage;

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

    @FXML
    private VBox scrollVBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //on text change, update plateContext.
        plateNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (plateContext != null) {
                plateContext.setName(newValue);
            }
        });
    }

    @FXML
    private void DeleteThisPlate(ActionEvent event) {
        plateDao.delete(plateContext);

        this.stage.close();
    }

    @Override
    public void setPlateContext(Plate plateContext) {
        this.plateContext = plateContext;

        if (plateContext != null) {
            System.out.println("Setting plate context: " + plateContext);


            updateRequiredPlateButton();


            //borderpane width x 100
            previewIV = plateContext.getPreviewImageViewCovering(borderPane.widthProperty(), 100);
            previewStackPane.getChildren().addFirst(previewIV);

        }


        fillTable();

        fillPlateInfo();
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
            plateInstructions = instructionDao.getInstructionsByPlateId(plateContext.getId());
        }
        TreeItem<Instruction> root = new TreeItem<>(new Instruction(0, null, 0, 0, "Root"));

        //database instructions
        for (Instruction instruction : plateInstructions) {
            TreeItem<Instruction> item = createTreeBranch(instruction);
            root.getChildren().add(item);

            scrollVBox.getChildren().add(new LIitem(instruction, 0));
        }
        TreeItem<Instruction> insertNewTI = new TreeItem<>(new Instruction(-2, null, -1, -1, "Add"));
        root.getChildren().add(insertNewTI);

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

        TreeTableColumn<Instruction, Void> colBtn = new TreeTableColumn<>("Button Column");

        Callback<TreeTableColumn<Instruction, Void>, TreeTableCell<Instruction, Void>> cellFactory = new Callback<TreeTableColumn<Instruction, Void>, TreeTableCell<Instruction, Void>>() {
            @Override
            public TreeTableCell<Instruction, Void> call(final TreeTableColumn<Instruction, Void> param) {
                final TreeTableCell<Instruction, Void> cell = new TreeTableCell<Instruction, Void>() {

                    private final Button btn = new Button("+");

                    {

                        btn.setOnAction((ActionEvent event) -> {
                            btn.setText("ID " + getIndex());
                            Instruction data = getTreeTableView().getTreeItem(getIndex()).getValue();
                            NavController.navigateTo("/com/mrdarip/bakery/view/SelectElement.fxml", data, EditPlateController.this);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);




        instructionTTV.getColumns().clear();
        instructionTTV.getColumns().addAll(instructionTitleColumn, instructionDurationColumn, instructionDifficultyColumn, colBtn);
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

    @Override
    public void setOrigin(Navigable origin) {
        this.origin = origin;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void OnSave(ActionEvent actionEvent) {
        instructionTTV.getRoot().getChildren().forEach((rootInstruction) -> {
            instructionDao.upsert(rootInstruction.getValue());

            //for each except last
            rootInstruction.getChildren().subList(
                    0,
                    rootInstruction.getChildren() != null ?
                            Math.max(rootInstruction.getChildren().size() - 1, 0) :
                            0
            ).forEach((leafInstruction) -> {
                instructionDao.upsert(leafInstruction.getValue());
            });
        });

        this.plateContext = plateDao.upsert(plateContext);

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
}
