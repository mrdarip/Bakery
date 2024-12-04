package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LIitemContainer extends VBox {

    public List<Instruction> instructions = new ArrayList<>();

    public void addNewInstruction(Instruction i) {
        instructions.addFirst(i);
        rebuild();
    }

    private void rebuild() {
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
        Instruction mvC = mvI.getSharperInstruction();
        Instruction mvCC = mvC.getSharperInstruction();
        mvC.setSharperInstruction(mvI);
        mvI.setSharperInstruction(mvCC);


        for (Instruction branch : instructions) {
            Instruction possibleParent = branch;
            int level = 0;

            do {
                if (possibleParent.isParentOf(mvI)) {
                    possibleParent.setSharperInstruction(mvC);
                }
                possibleParent = possibleParent.getSharperInstruction();
                level++;
            } while (possibleParent != null && !possibleParent.isParentOf(mvI));
        }
        rebuild();
    }
}
