package com.mrdarip.bakery.data.DAO;

import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;

public abstract class PlateInstructionCRDAO {
    public abstract void bind(Plate plate, Instruction instruction, int position);

    public abstract void remove(Plate plate, int position);

    public abstract void swap(Plate plate, int position1, int position2);

    public abstract void shift(Plate plate, int positions, int from);

    public abstract void negate(Plate plate);
}