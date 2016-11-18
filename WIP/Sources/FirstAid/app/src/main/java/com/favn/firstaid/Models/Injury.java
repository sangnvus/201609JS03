package com.favn.firstaid.Models;

/**
 * Created by Hung Gia on 10/5/2016.
 */

public class Injury {
    private int id;
    private String injuryName;
    private String injurySymptom;

    // START ADD ATTRIBUTES - Kienmt : 11/08/2016
    private String priority;
    private String image;
    private String updated_at;
    private String created_at;
    // END ADD ATTRIBUTES


    public Injury() {
    }

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
