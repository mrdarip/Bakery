package com.mrdarip.bakery.data.DAO;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;

import java.util.List;

public abstract class InstructionDao {
    public abstract Instruction getInstructionById(int id);

    public abstract Instruction upsert(Instruction instruction);

    public abstract List<Instruction> getInstructionsByPlateId(int plateId);

    public List<Instruction> getInstructionsByPlate(Plate plate) {
        return getInstructionsByPlateId(plate.getId());
    }
}
