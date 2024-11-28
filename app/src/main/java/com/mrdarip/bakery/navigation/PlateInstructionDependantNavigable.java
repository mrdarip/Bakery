package com.mrdarip.bakery.navigation;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;

public interface PlateInstructionDependantNavigable extends Navigable, PlateDependantNavigable, InstructionDependantNavigable {
    void setPlateInstructionContext(Plate plateContext, Instruction instructionContext);
}
