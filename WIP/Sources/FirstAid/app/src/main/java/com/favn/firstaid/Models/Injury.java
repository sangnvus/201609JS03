package com.favn.firstaid.models;

/**
 * Created by Hung Gia on 10/5/2016.
 */

public class Injury {
    private int id;
    private String injury_name;
    private String symptom;

    // START ADD ATTRIBUTES - Kienmt : 11/08/2016
    private String priority;
    private String image;
    private String updated_at;
    private String created_at;
    // END ADD ATTRIBUTES


    public Injury() {
    }

    public Injury(int id, String injury_name, String symptom) {
        this.id = id;
        this.injury_name = injury_name;
        this.symptom = symptom;
    }

    public Injury(int id, String injury_name, String symptom, String priority, String image, String updated_at, String created_at) {
        this.id = id;
        this.injury_name = injury_name;
        this.symptom = symptom;
        this.priority = priority;
        this.image = image;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInjury_name() {
        return injury_name;
    }

    public void setInjury_name(String injury_name) {
        this.injury_name = injury_name;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
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
