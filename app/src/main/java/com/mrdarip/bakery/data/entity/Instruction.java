package com.mrdarip.bakery.data.entity;

public class Instruction {
    private int idInstruction;
    private Instruction sharperInstruction;
    private int difficulty;
    private int duration;
    private String instructionText;

    public Instruction(int idInstruction, Instruction sharperInstruction, int difficulty, int duration, String instructionText) {
        this.idInstruction = idInstruction;
        this.sharperInstruction = sharperInstruction;
        this.difficulty = difficulty;
        this.duration = duration;
        this.instructionText = instructionText;
    }

    public static Instruction getEmptyInstruction() {
        return new Instruction(-1, null, 0, 0, "New Instruction");
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
    }

    public void setDuration(int newValue) {
        this.duration = newValue;
    }

    public void setDifficulty(int newValue) {
        this.difficulty = newValue;
    }

    public void setIdInstruction(int anInt) {
        this.idInstruction = anInt;
    }

    @Override
    public String toString() {
        return "Instruction: " + instructionText + " with duration: " + duration + " and difficulty: " + difficulty;
    }

    public void setSharperInstruction(Instruction instruction) {
        this.sharperInstruction = instruction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Instruction instruction) {
            return this.idInstruction == instruction.idInstruction;
        }
        return false;
    }
}
