package com.favn.firstaid.Models;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Mikey on 11/1/2016.
 */

public class Faq {
    private String question;
    private String answer;
    private Context context;

    public Faq(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

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
