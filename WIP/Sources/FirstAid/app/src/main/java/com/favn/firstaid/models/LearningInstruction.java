package com.favn.firstaid.Models;

/**
 * Created by Mikey on 11/16/2016.
 */

public class LearningInstruction {
    private int injuryId;
    private int step;
    private String content;
    private String explanation;
    private String image;
    private String audio;

    public LearningInstruction(int injuryId, int step, String content, String explanation, String image, String audio) {
        this.injuryId = injuryId;
        this.step = step;
        this.content = content;
        this.explanation = explanation;
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
