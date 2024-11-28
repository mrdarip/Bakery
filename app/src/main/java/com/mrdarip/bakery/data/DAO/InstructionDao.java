package com.mrdarip.bakery.data.DAO;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;

import java.util.List;

public abstract class InstructionDao {
    public abstract Instruction getInstructionById(int id);

    public abstract void delete(Instruction instruction);

    public abstract void deleteById(int id);

    public abstract Instruction insert(Instruction instruction);

    public abstract Instruction update(Instruction instruction);

    public abstract Instruction upsert(Instruction instruction);

    public abstract List<Instruction> getInstructionsByPlateId(int plateId);

    public List<Instruction> getInstructionsByPlate(Plate plate) {
        return getInstructionsByPlateId(plate.getId());
    }

    public abstract List<Instruction> getAll();
}
