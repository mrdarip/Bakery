package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.navigation.Navigable;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LIitemContainer extends VBox {

    public List<Instruction> instructions = new ArrayList<>();
    private Navigable origin;

    public void addNewInstruction(Instruction i) {
        instructions.addFirst(searchDuplicate(i));
        rebuild();
    }

    public Navigable getOrigin() {
        return origin;
    }

    public void setOrigin(Navigable origin) {
        this.origin = origin;
    }

    public Instruction searchDuplicate(Instruction i) {
        for (Instruction branch : instructions) {
            if (branch.equals(i)) {
                return branch;
            }
        }
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
}
