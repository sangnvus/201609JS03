package com.favn.firstaid.Models;

/**
 * Created by Hung Gia on 10/7/2016.
 */

public class Instruction {
    private int injuryId;
    private String step;

    public Instruction(int injuryId, String step) {
        this.injuryId = injuryId;
        this.step = step;
    }

    public int getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(int injuryId) {
        this.injuryId = injuryId;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
