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
    private int caller_taking_id;
    private int isDeleted;
    private String updated_at;
    private String created_at;

    public Ambulance() {

    }

    public Ambulance(int id, int user_id, String team) {
        this.id = id;
        this.user_id = user_id;
        this.team = team;
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

    public int getCaller_taking_id() {
        return caller_taking_id;
    }

    public void setCaller_taking_id(int caller_taking_id) {
        this.caller_taking_id = caller_taking_id;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
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
