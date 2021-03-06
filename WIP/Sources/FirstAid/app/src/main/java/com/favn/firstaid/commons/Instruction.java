package com.favn.firstaid.commons;

/**
 * Created by Hung Gia on 10/7/2016.
 */

public class Instruction {
    private int injuryId;
    private int step;
    private String content;
    private boolean isMakeCall;
    private String explanation;
    private String image;
    private String audio;

    public Instruction(int injuryId, int step, String content, String explanation, String image) {
        this.injuryId = injuryId;
        this.step = step;
        this.content = content;
        this.explanation = explanation;
        this.image = image;
    }

    public Instruction(int injuryId, int step, String content, boolean isMakeCall, String image, String audio) {
        this.injuryId = injuryId;
        this.step = step;
        this.content = content;
        this.isMakeCall = isMakeCall;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMakeCall() {
        return isMakeCall;
    }

    public void setMakeCall(boolean makeCall) {
        isMakeCall = makeCall;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
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
