package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.navigation.Navigable;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LIitemContainer extends VBox {

    public List<Instruction> instructions = new ArrayList<>();
    private Navigable origin;

    public void addNewInstruction(Instruction i) {
        instructions.addFirst(searchInTreeFor(i));
        rebuild();
    }

    public Navigable getOrigin() {
        return origin;
    }

    public void setOrigin(Navigable origin) {
        this.origin = origin;
    }

    public Instruction searchInTreeFor(Instruction i) {
        for (Instruction branch : instructions) {
            Instruction item = branch;
            do {
                if (item.equals(i)) {
                    System.out.println("Instruction found in tree");
                    return item;
                }
                item = item.getSharperInstruction();
            } while (item != null);
        }
        System.out.println("Instruction not found in tree");
        return i;
    }

    public void rebuild() {
        getChildren().clear();
        for (Instruction i : instructions) {
            getChildren().add(new InstructionLI(i, null, this));
        }
    }

    public void moveUp(Instruction instruction) {
        int index = instructions.indexOf(instruction);
        if (index > 0) {
            Instruction temp = instructions.get(index - 1);
            instructions.set(index - 1, instruction);
            instructions.set(index, temp);
            rebuild();
        }
    }

    public int positionOf(InstructionLI instructionLI) {
        return instructions.indexOf(instructionLI);
    }

    public void moveDownInTree(Instruction mvI) {
        List<Instruction> seenInstructions = new ArrayList<>();

        Instruction mvC = mvI.getSharperInstruction();
        Instruction mvCC = mvC.getSharperInstruction();
        mvC.setSharperInstruction(mvI);
        mvI.setSharperInstruction(mvCC);

        if (instructions.contains(mvI)) {
            instructions.replaceAll(instruction -> {
                //replace mvI with mvC
                if (instruction.equals(mvI)) {
                    return mvC;
                } else {
                    return instruction;
                }
            });
        }

        for (Instruction branch : instructions) {
            Instruction branchInstruction = branch;
            int level = 0;

            do {
                if (branchInstruction.isParentOf(mvI) && !seenInstructions.contains(branchInstruction)) {
                    branchInstruction.setSharperInstruction(mvC);
                }

                seenInstructions.add(branchInstruction);


                branchInstruction = branchInstruction.getSharperInstruction();
                level++;

                if (level > 1000000) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Infinite loop detected");
                    alert.setContentText("An infinite loop was detected in the instructions. Please check the instructions and try again.");
                    alert.showAndWait();
                    this.origin.close();
                    break;
                }
            } while (branchInstruction != null && !branchInstruction.isParentOf(mvI));
        }
        rebuild();
    }
}
