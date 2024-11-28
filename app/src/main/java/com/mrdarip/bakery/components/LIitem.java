package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LIitem extends VBox {
    public static final int CARD_HEIGHT = 75;
    private static final String CARD_STYLE = "-fx-background-color: gray;";

    public LIitem(Instruction instruction, int level) {
        super();

        setMargin(this, new Insets(0, 0, 0, level == 1 ? 16 : 0));
        setPrefHeight(CARD_HEIGHT);
        setStyle(CARD_STYLE);


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
                try {
                    instruction.setDuration(instructionDuration.getValue());
                    instructionDuration.styleProperty().setValue("-fx-text-fill: black;");
                } catch (NumberFormatException e) {
                    instruction.setDuration(0);
                    instructionDuration.styleProperty().setValue("-fx-text-fill: red;");
                }
            }
        });
        VBox durationTitle = new VBox(instructionDuration);

        Spinner<Integer> instructionDifficulty = new Spinner<>(0, 3, instruction.getDifficulty());
        instructionDifficulty.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionDifficulty.getValueFactory().setValue(instruction.getDifficulty());
            } else {
                instruction.setDifficulty(instructionDifficulty.getValue());
                instructionDifficulty.styleProperty().setValue("-fx-text-fill: black;");
            }
        });


        VBox difficultyTitle = new VBox(instructionDifficulty);

        if (level == 0) {
            nameTitle.getChildren().addFirst(new Label("Name"));
            durationTitle.getChildren().addFirst(new Label("Duration"));
            difficultyTitle.getChildren().addFirst(new Label("Difficulty"));
        }


        getChildren().add(
                new HBox(nameTitle, durationTitle, difficultyTitle)
        );

        if (instruction.hasSharperInstruction()) {
            getChildren().add(new LIitem(instruction.getSharperInstruction(), level + 1));
        }
    }
}
