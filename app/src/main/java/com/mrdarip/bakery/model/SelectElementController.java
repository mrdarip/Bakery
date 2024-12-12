/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mrdarip.bakery.model;

import com.mrdarip.bakery.components.Card;
import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBInstructionDAO;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBPlateDAO;
import com.mrdarip.bakery.data.DAO.MariaDB.MariaDBPlateInstructionCRDAO;
import com.mrdarip.bakery.data.DAO.PlateDao;
import com.mrdarip.bakery.data.DAO.PlateInstructionCRDAO;
import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;
import com.mrdarip.bakery.navigation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mrdarip
 */
public class SelectElementController implements Initializable, PlateInstructionDependantNavigable {

    private final PlateDao plateDao = new MariaDBPlateDAO();
    private final InstructionDao instructionDao = new MariaDBInstructionDAO();
    private final PlateInstructionCRDAO plateInstructionCRDAO = new MariaDBPlateInstructionCRDAO();

    private Plate plateContext;
    private Instruction instructionContext;

    private Navigable origin;
    private Stage stage;

    @FXML
    private VBox ElementsListVBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void setPlateContext(Plate plateContext) {
        this.plateContext = plateContext;
        ElementsListVBox.getChildren().addAll(
                new Card(new ImageView(new Image("/img/icon/plus.png")), "New", "Plate", ev -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/EditPlate.fxml", Plate.getEmptyPlate(), origin);
                    endSelection();
                }),

                new Card(new ImageView(new Image("/img/icon/cross.png")), "No", "Plate", ev -> {
                    if (origin instanceof PlateDependantNavigable plateDependantNavigable) {
                        plateDependantNavigable.setPlateRequiredPlate(null);
                        endSelection();
                    }
                })
        );

        plateDao.getPlatesPage(0, 99999).forEach(plate -> { //TODO: set custom query to remove cycles and itself
            ElementsListVBox.getChildren().add(
                    new Card(
                            new Image(plate.getPreviewURI()),
                            plate.getName(),
                            ev -> {
                                if (origin instanceof PlateDependantNavigable plateDependantNavigable) {
                                    plateDependantNavigable.setPlateRequiredPlate(plate);
                                    endSelection();
                                }
                            }
                    )
            );
        });
    }

    @Override
    public void setPlateRequiredPlate(Plate requiredPlate) {

    }

    @Override
    public String getScreenTitle() {
        String title = "Select Element screen";
        if (plateContext != null) {
            title = "Select Plate for " + plateContext.getName();
        }

        if (instructionContext != null) {
            title = "Select Instruction for " + instructionContext.getInstructionText();
        }

        return title;
    }

    @Override
    public Image getScreenIcon() {
        if (plateContext != null) {
            return new Image(plateContext.getPreviewURI());
        }
        return new Image("/img/icon/select.png");
    }

    @Override
    public int getMinWidth() {
        return 300;
    }

    @Override
    public int getMinHeight() {
        return 500;
    }

    @Override
    public void setOrigin(Navigable origin) {
        this.origin = origin;
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

    private void endSelection() {
        stage.close();
    }

    @Override
    public void setInstructionContext(Instruction instructionContext) {
        this.instructionContext = instructionContext;

        ElementsListVBox.getChildren().addAll(
                new Label("TODO: Implement this"), //TODO: delete setInstructionContext if not accessible from the UI

                new Card(new ImageView(new Image("/img/icon/plus.png")), "New", "Instruction", ev -> {
                    System.out.println("No use case for this");
                    origin.rebuildUI();
                    endSelection();
                })
        );

        instructionDao.getAll().forEach(instruction -> {
            ElementsListVBox.getChildren().add(
                    new Card(
                            instruction.getInstructionText(),
                            ev -> {
                                System.out.println("No use case for this!!");
                                origin.rebuildUI();
                                endSelection();
                            }
                    )
            );
        });


    }

    @Override
    public void setPlateInstructionContext(Plate plateContext, Instruction instructionContext) {
        this.instructionContext = instructionContext;
        this.plateContext = plateContext;

        ElementsListVBox.getChildren().addAll(
                new Card(new ImageView(new Image("/img/icon/plus.png")), "New", "Instruction", ev -> {
                    if (origin instanceof InstructionDependantNavigable instructionDependantNavigable) {
                        instructionDependantNavigable.setInstructionContext(Instruction.getEmptyInstruction());
                    }
                    endSelection();
                })
        );

        instructionDao.getAll().forEach(instruction -> {
            ElementsListVBox.getChildren().add(
                    new Card(
                            instruction.getInstructionText(),
                            ev -> {
                                if (origin instanceof InstructionDependantNavigable instructionDependantNavigable) {
                                    instructionDependantNavigable.setInstructionContext(instruction);
                                }
                                endSelection();
                            }
                    )
            );
        });

    }
}
