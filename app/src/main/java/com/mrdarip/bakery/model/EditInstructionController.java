/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mrdarip.bakery.model;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.navigation.InstructionDependantNavigable;
import com.mrdarip.bakery.navigation.Navigable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditInstructionController implements Initializable, InstructionDependantNavigable {
    private Instruction instructionContext;
    private Stage stage;

    @FXML
    private VBox container;

    @FXML
    void OnExit(ActionEvent event) {

    }

    @FXML
    void OnSave(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void setInstructionContext(Instruction instructionContext) {
        this.instructionContext = instructionContext;
    }

    @Override
    public String getScreenTitle() {
        return "Edit Instruction " + instructionContext.getInstructionText();
    }

    @Override
    public Image getScreenIcon() {
        return new Image("/img/icon/select.png");
    }

    @Override
    public int getMinWidth() {
        return 400;
    }

    @Override
    public int getMinHeight() {
        return 600;
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
