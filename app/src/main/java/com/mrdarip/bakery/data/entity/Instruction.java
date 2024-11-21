package com.mrdarip.bakery.data.entity;

public class Instruction {
    private final int idInstruction;
    private final Instruction sharperInstruction;
    private final int difficulty;
    private final int duration;
    private final String instructionText;

    public Instruction(int idInstruction, Instruction sharperInstruction, int difficulty, int duration, String instructionText) {
        this.idInstruction = idInstruction;
        this.sharperInstruction = sharperInstruction;
        this.difficulty = difficulty;
        this.duration = duration;
        this.instructionText = instructionText;
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

    public int getIdInstruction() {
        return idInstruction;
    }

    public boolean hasSharperInstruction() {
        return this.sharperInstruction != null;
    }

    public int getId() {
        return idInstruction;
    }
}
