package com.mrdarip.bakery.components;

import com.mrdarip.bakery.data.entity.Instruction;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LIitemContainer extends VBox {

    public List<InstructionLI> instructionLIs = new ArrayList<>();

    public void addNewInstruction(Instruction i) {
        instructionLIs.addFirst(new InstructionLI(i, null, this));
        rebuild();
    }

    private void rebuild() {
        getChildren().clear();
        for (InstructionLI instructionLI : instructionLIs) {
            getChildren().addLast(instructionLI.rebuit(this));
        }
    }

    public void moveUp(InstructionLI instructionLI) {
        int index = instructionLIs.indexOf(instructionLI);
        if (index > 0) {
            InstructionLI temp = instructionLIs.get(index - 1).rebuit(this);
            instructionLIs.set(index - 1, instructionLI.rebuit(this));
            instructionLIs.set(index, temp);
            rebuild();
        }
    }

    public int positionOf(InstructionLI instructionLI) {
        return instructionLIs.indexOf(instructionLI);
    }
}
