package com.favn.firstaid.Models;

/**
 * Created by Hung Gia on 10/7/2016.
 */

public class Instruction {
    private int injuryId;
    private int step;
    private String instruction;
    private String image;
    private String audio;

    public Instruction(int injuryId, int step, String instruction, String image, String audio) {
        this.injuryId = injuryId;
        this.step = step;
        this.instruction = instruction;
        this.image = image;
        this.audio = audio;
    }

    public int getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(int injuryId) {
        this.injuryId = injuryId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
