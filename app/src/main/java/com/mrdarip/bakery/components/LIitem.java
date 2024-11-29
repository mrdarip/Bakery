package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.navigation.NavController;
import com.mrdarip.bakery.navigation.Navigable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LIitem extends VBox {
    public static final int CARD_HEIGHT = 75;
    private static final String CARD_STYLE = "-fx-background-color: gray;";

    public LIitem(Instruction instruction, int level, Navigable origin) {
        super();

        setMargin(this, new Insets(0, 0, 0, level == 1 ? 16 : 0));
        setPrefHeight(VBox.USE_COMPUTED_SIZE);
        setStyle(CARD_STYLE);

        //from 128 to 255
        styleProperty().setValue("-fx-background-color: #" + Color.gray(1 - ((level + 2) * 0.1)).toString().substring(2, 8) + ";");


        TextField instructionName = new TextField(instruction.getInstructionText());
        instructionName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionName.setText(instruction.getInstructionText());
            } else {
                instruction.setInstructionText(instructionName.getText());
            }
        });
        VBox nameTitle = new VBox(instructionName);

        Spinner<Integer> instructionDuration = new Spinner<>(0, Integer.MAX_VALUE, instruction.getDuration());
        instructionDuration.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionDuration.getValueFactory().setValue(instruction.getDuration());
            } else {
                instruction.setDuration(instructionDuration.getValue());
            }
        });
        VBox durationTitle = new VBox(instructionDuration);

        Spinner<Integer> instructionDifficulty = new Spinner<>(0, 3, instruction.getDifficulty());
        instructionDifficulty.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionDifficulty.getValueFactory().setValue(instruction.getDifficulty());
            } else {
                instruction.setDifficulty(instructionDifficulty.getValue());
            }
        });
        VBox difficultyTitle = new VBox(instructionDifficulty);

        Button addSubInstructionButton = new Button("+");
        addSubInstructionButton.setOnAction((ev) -> {
            NavController.navigateTo("/com/mrdarip/bakery/view/SelectElement.fxml", instruction, origin);
        });

        if (level == 0) {
            nameTitle.getChildren().addFirst(new Label("Name"));
            durationTitle.getChildren().addFirst(new Label("Duration"));
            difficultyTitle.getChildren().addFirst(new Label("Difficulty"));
        }

        HBox fields = new HBox(nameTitle, durationTitle, difficultyTitle, addSubInstructionButton);
        fields.setAlignment(Pos.BOTTOM_LEFT);
        getChildren().add(
                fields
        );

        if (instruction.hasSharperInstruction()) {
            getChildren().add(new LIitem(instruction.getSharperInstruction(), level + 1, origin));
        }
    }
}
