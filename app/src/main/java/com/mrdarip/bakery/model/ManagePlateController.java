
package com.mrdarip.bakery.model;

import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBInstructionDAO;
import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.PlateDependantDestiny;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ManagePlateController implements Initializable, PlateDependantDestiny {
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

        fillTable();
    }

    private void fillTable() {
        if (plateContext != null) {
            List<Instruction> plateInstructions = instructionDao.getInstructionsByPlateId(plateContext.getId());

            TreeItem<Instruction> root = new TreeItem<>(new Instruction(0, null, 0, 0, "Root"));
            for (Instruction instruction : plateInstructions) {
                TreeItem<Instruction> item = createTreeItem(instruction);
                root.getChildren().add(item);
            }

            TreeTableColumn<Instruction, String> instructionColumn = new TreeTableColumn<>("Instruction");
            instructionColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("instructionText"));

            instructionTTV.getColumns().clear();
            instructionTTV.getColumns().add(instructionColumn);
            instructionTTV.setRoot(root);
            instructionTTV.setShowRoot(false);
        }
    }

    private TreeItem<Instruction> createTreeItem(Instruction instruction) {
        TreeItem<Instruction> item = new TreeItem<>(instruction);
        if (instruction.getSharperInstruction() != null) {
            item.getChildren().add(createTreeItem(instruction.getSharperInstruction()));
        }
        return item;
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
