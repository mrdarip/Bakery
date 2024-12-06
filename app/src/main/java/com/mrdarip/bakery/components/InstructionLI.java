package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InstructionLI extends VBox {
    public static final int CARD_HEIGHT = 75;
    private static final String CARD_STYLE = "-fx-background-color: gray;";

    public Instruction instruction;

    public InstructionLI(Instruction instruction, EventHandler<ActionEvent> onDeleteClick, EventHandler<ActionEvent> onMoveDown) {
        super();
        this.instruction = instruction;

        setPrefHeight(VBox.USE_COMPUTED_SIZE);
        setStyle(CARD_STYLE);

        //from 128 to 255
        //styleProperty().setValue("-fx-background-color: #" + Color.gray(1 - ((level + 2) * 0.1)).toString().substring(2, 8) + ";");


        TextField instructionName = new TextField(this.instruction.getInstructionText());
        instructionName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionName.setText(this.instruction.getInstructionText());
            } else {
                this.instruction.setInstructionText(instructionName.getText());
            }
        });
        VBox nameTitle = new VBox(instructionName);

        Spinner<Integer> instructionDuration = new Spinner<>(0, Integer.MAX_VALUE, this.instruction.getDuration());
        instructionDuration.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionDuration.getValueFactory().setValue(this.instruction.getDuration());
            } else {
                this.instruction.setDuration(instructionDuration.getValue());
            }
        });
        VBox durationTitle = new VBox(instructionDuration);

        Spinner<Integer> instructionDifficulty = new Spinner<>(0, 3, this.instruction.getDifficulty());
        instructionDifficulty.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionDifficulty.getValueFactory().setValue(this.instruction.getDifficulty());
            } else {
                this.instruction.setDifficulty(instructionDifficulty.getValue());
            }
        });
        VBox difficultyTitle = new VBox(instructionDifficulty);

        Button addSubInstructionButton = new Button("❌");
        addSubInstructionButton.setOnAction(onDeleteClick);

        Button moveDownB = new Button("↓");
        moveDownB.setOnAction(onMoveDown);

        nameTitle.getChildren().addFirst(new Label("Name"));
        durationTitle.getChildren().addFirst(new Label("Duration"));
        difficultyTitle.getChildren().addFirst(new Label("Difficulty"));

        HBox fields = new HBox(nameTitle, durationTitle, difficultyTitle,
                new VBox(
                        addSubInstructionButton,
                        moveDownB)
        );

        fields.setAlignment(Pos.BOTTOM_LEFT);
        getChildren().add(
                fields
        );

        this.instruction.addOnChange(() -> {
            instructionName.setText(this.instruction.getInstructionText());
            instructionDuration.getValueFactory().setValue(this.instruction.getDuration());
            instructionDifficulty.getValueFactory().setValue(this.instruction.getDifficulty());
        });


    }
}