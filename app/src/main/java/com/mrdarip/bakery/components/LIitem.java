package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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
        instructionName.textProperty().addListener((observable, oldValue, newValue) -> {
            instruction.setInstructionText(newValue);
        });
        VBox nameTitle = new VBox(instructionName);

        TextField instructionDuration = new TextField(String.valueOf(instruction.getDuration()));
        instructionDuration.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                instruction.setDuration(Integer.parseInt(newValue));
                instructionDuration.styleProperty().setValue("-fx-text-fill: black;");
            } catch (NumberFormatException e) {
                instruction.setDuration(0);
                instructionDuration.styleProperty().setValue("-fx-text-fill: red;");
            }
        });
        VBox durationTitle = new VBox(instructionDuration);

        TextField instructionDifficulty = new TextField(String.valueOf(instruction.getDifficulty()));
        instructionDifficulty.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                instruction.setDifficulty(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                instruction.setDifficulty(0);
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
