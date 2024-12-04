package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.navigation.NavController;
import javafx.geometry.Insets;
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


    int level;
    int index;
    public Instruction instruction;
    InstructionLI parent;
    InstructionLI child;

    public InstructionLI(Instruction instruction, InstructionLI parent, LIitemContainer container) {
        super();
        this.instruction = instruction;
        this.parent = parent;
        this.level = parent == null ? 0 : parent.level + 1;
        this.index = container.instructions.size();

        InstructionLI topLi = this.parent;
        while (topLi != null) {
            this.index = topLi.index;
            topLi = topLi.parent;
        }

        setMargin(this, new Insets(0, 0, 0, level == 1 ? 16 : 0));
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

        Button addSubInstructionButton = new Button("+");
        addSubInstructionButton.setOnAction((ev) -> {
            NavController.navigateTo("/com/mrdarip/bakery/view/SelectElement.fxml", this.instruction, container.getOrigin());
        });

        if (level == 0) {
            nameTitle.getChildren().addFirst(new Label("Name"));
            durationTitle.getChildren().addFirst(new Label("Duration"));
            difficultyTitle.getChildren().addFirst(new Label("Difficulty"));
        }

        HBox fields = new HBox(nameTitle, durationTitle, difficultyTitle, addSubInstructionButton);




        Button moveUpRootB = new Button("↑");

        moveUpRootB.setOnAction((ev) -> {
            container.moveUp(this.instruction);
        });

        if (level == 0 && this.index > 0) {
            fields.getChildren().add(moveUpRootB);
        }

        fields.setAlignment(Pos.BOTTOM_LEFT);
        getChildren().add(
                fields
        );

        setChildIfExists(container);

        this.instruction.addOnChange(() -> {
            instructionName.setText(this.instruction.getInstructionText());
            instructionDuration.getValueFactory().setValue(this.instruction.getDuration());
            instructionDifficulty.getValueFactory().setValue(this.instruction.getDifficulty());
        });


        if (this.child != null) {
            Button moveDownB = new Button("↓");
            moveDownB.styleProperty().setValue("-fx-background-color: #ff0000;");
            moveDownB.setOnAction((ev) -> {
                System.out.println("Moving down " + this.instruction.getInstructionText() + " hasCHild " + this.instruction.hasSharperInstruction());
                container.moveDownInTree(this.instruction);
            });

            fields.getChildren().addAll(moveDownB, new Label(this.instruction.getId() + ""));
        }
    }


    private void setChildIfExists(LIitemContainer container) {
        if (this.getChildren().removeIf(node -> node instanceof InstructionLI)) {
            this.child = null;
        }

        if (this.instruction.getSharperInstruction() != null) {
            InstructionLI child = new InstructionLI(container.searchInTreeFor(this.instruction.getSharperInstruction()), this, container);
            this.child = child;
            getChildren().add(child);
        }
    }

    public InstructionLI rebuit(LIitemContainer container) {
        return new InstructionLI(this.instruction, parent, container);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InstructionLI li) {
            return li.instruction.equals(this.instruction) && li.level == this.level;
        }
        return false;
    }
}