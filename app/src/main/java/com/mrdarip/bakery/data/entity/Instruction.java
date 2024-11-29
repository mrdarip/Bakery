package com.mrdarip.bakery.data.entity;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
    private int idInstruction;
    private Instruction sharperInstruction;
    private int difficulty;
    private int duration;
    private String instructionText;
    private final List<Runnable> OnSharperInstructionChange;
    private final List<Runnable> OnChange;

    public Instruction(int idInstruction, Instruction sharperInstruction, int difficulty, int duration, String instructionText) {
        this.idInstruction = idInstruction;
        this.sharperInstruction = sharperInstruction;
        this.difficulty = difficulty;
        this.duration = duration;
        this.instructionText = instructionText;
        this.OnSharperInstructionChange = new ArrayList<>();
        this.OnChange = new ArrayList<>();
    }

    public static int created = 0;
    public static Instruction getEmptyInstruction() {
        created++;
        return new Instruction(-created, null, 0, 0, "New Instruction");
    }

    public String getInstructionText() {
        return instructionText;
    }

    public Instruction getSharperInstruction() {
        return sharperInstruction;
    }

    public int getDuration() {
        return duration;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public boolean hasSharperInstruction() {
        return this.sharperInstruction != null;
    }

    public int getId() {
        return idInstruction;
    }

    public void setInstructionText(String newValue) {
        this.instructionText = newValue;
        executeOnChange();
    }

    public void setDuration(int newValue) {
        this.duration = newValue;
        executeOnChange();
    }

    public void setDifficulty(int newValue) {
        this.difficulty = newValue;
        executeOnChange();
    }

    public void setIdInstruction(int anInt) {
        this.idInstruction = anInt;
        executeOnChange();
    }

    @Override
    public String toString() {
        return "Instruction: " + instructionText + " with duration: " + duration + " and difficulty: " + difficulty;
    }

    public void setSharperInstruction(Instruction instruction) {
        this.sharperInstruction = instruction;
        this.executeOnSharperInstructionChange();
        this.executeOnChange();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Instruction instruction) {
            return this.idInstruction == instruction.idInstruction;
        }
        return false;
    }

    public void addOnSharperInstructionChange(Runnable lambda) {
        this.OnSharperInstructionChange.add(lambda);
    }

    private void executeOnSharperInstructionChange() {
        this.OnSharperInstructionChange.forEach(Runnable::run);
    }

    public void addOnChange(Runnable lambda) {
        this.OnChange.add(lambda);
    }

    private void executeOnChange() {
        this.OnChange.forEach(Runnable::run);
    }


}
