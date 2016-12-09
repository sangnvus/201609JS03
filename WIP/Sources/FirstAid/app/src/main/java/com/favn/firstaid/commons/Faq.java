package com.favn.firstaid.commons;

/**
 * Created by Mikey on 11/1/2016.
 */

public class Faq {
    private int injuryId;
    private String question;
    private String answer;

    public Faq(int injuryId, String question, String answer) {
        this.injuryId = injuryId;
        this.question = question;
        this.answer = answer;
    }

    public int getInjuryId() {return injuryId;}

    public void setInjuryId(int injuryId) {this.injuryId = injuryId;}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


}
