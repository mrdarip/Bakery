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

import java.util.function.Consumer;

public class InstructionLI extends VBox {

    public Instruction instruction;

    public InstructionLI(Instruction instruction, EventHandler<ActionEvent> onDeleteClick, Consumer<InstructionLI> onUnassignClick, Consumer<InstructionLI> onMoveUp) {
        super();
        this.instruction = instruction;

        setPrefHeight(VBox.USE_COMPUTED_SIZE);
        getStyleClass().add("instruction-li");

        TextField instructionName = new TextField(this.instruction.getInstructionText());
        instructionName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionName.setText(this.instruction.getInstructionText());
            } else {
                this.instruction.setInstructionText(instructionName.getText());
            }
        });
        VBox nameTitle = new VBox(new Label("Name"), instructionName);

        Spinner<Integer> instructionDuration = new Spinner<>(0, Integer.MAX_VALUE, this.instruction.getDuration());
        instructionDuration.prefWidth(Spinner.USE_COMPUTED_SIZE);
        instructionDuration.minWidth(Spinner.USE_COMPUTED_SIZE);
        instructionDuration.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionDuration.getValueFactory().setValue(this.instruction.getDuration());
            } else {
                this.instruction.setDuration(instructionDuration.getValue());
            }
        });
        VBox durationTitle = new VBox(new Label("Duration"), instructionDuration);

        Spinner<Integer> instructionDifficulty = new Spinner<>(0, 3, this.instruction.getDifficulty());
        instructionDifficulty.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                instructionDifficulty.getValueFactory().setValue(this.instruction.getDifficulty());
            } else {
                this.instruction.setDifficulty(instructionDifficulty.getValue());
            }
        });
        VBox difficultyTitle = new VBox(new Label("Difficulty"), instructionDifficulty);

        instructionDuration.setPrefWidth(80); // Ancho preferido explícito
        instructionDuration.setMinWidth(50);  // Ancho mínimo explícito
        instructionDifficulty.setPrefWidth(60);
        instructionDifficulty.setMinWidth(50);
        instructionName.setPrefWidth(500); // Ancho preferido automático


        Button deleteInstructionButton = new Button("\uD83D\uDDD1");
        deleteInstructionButton.setOnAction(onDeleteClick);

        Button unAssignInstructionButton = new Button("❌");
        unAssignInstructionButton.setOnAction(ev -> {
            onUnassignClick.accept(this);
        });

        Button moveUpB = new Button("↑");
        moveUpB.setOnAction(ev -> {
            onMoveUp.accept(this);
        });

        HBox deleteUnassign = new HBox(deleteInstructionButton, unAssignInstructionButton);
        deleteUnassign.setAlignment(Pos.CENTER_RIGHT);
        deleteUnassign.setSpacing(4);

        HBox fields = new HBox(nameTitle, durationTitle, difficultyTitle);
        fields.setSpacing(4);

        HBox HSegments = new HBox(moveUpB, fields, deleteUnassign);

        HSegments.setAlignment(Pos.CENTER_LEFT);
        HSegments.setSpacing(16);

        getChildren().add(
                HSegments
        );

        this.instruction.addOnChange(() -> {
            instructionName.setText(this.instruction.getInstructionText());
            instructionDuration.getValueFactory().setValue(this.instruction.getDuration());
            instructionDifficulty.getValueFactory().setValue(this.instruction.getDifficulty());
        });


    }
}