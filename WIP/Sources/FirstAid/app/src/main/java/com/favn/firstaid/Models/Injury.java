package com.favn.firstaid.Models;

/**
 * Created by Hung Gia on 10/5/2016.
 */

public class Injury {
    private int id;
    private String injuryName;
    private String injurySymptom;

    public Injury(int id, String injuryName, String injurySymptom) {
        this.id = id;
        this.injuryName = injuryName;
        this.injurySymptom = injurySymptom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInjuryName() {
        return injuryName;
    }

    public void setInjuryName(String injuryName) {
        this.injuryName = injuryName;
    }

    public String getInjurySymptom() {
        return injurySymptom;
    }

    public void setInjurySymptom(String injurySymptom) {
        this.injurySymptom = injurySymptom;
    }
}
