package com.favn.ambulance.models;

/**
 * Created by Hung Gia on 11/28/2016.
 */

public class Ambulance {
    private int id;
    private int user_id;
    private String team;
    private double latitude;
    private double longitude;
    private String status;
    private int isDeleted;
    private String updated_at;
    private String created_at;

    public Ambulance(int id, int user_id, String team, double latitude, double longitude, String
            status, int isDeleted, String updated_at, String created_at) {
        this.id = id;
        this.user_id = user_id;
        this.team = team;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.isDeleted = isDeleted ;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int isDeleted() {
        return isDeleted;
    }

    public void setDeleted(int deleted) {
        isDeleted = deleted;
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
