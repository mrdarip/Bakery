package com.mrdarip.bakery.navigation;

import com.mrdarip.bakery.data.entity.Instruction;

public interface InstructionDependantNavigable extends Navigable {
    void setInstructionContext(Instruction instructionContext);
}