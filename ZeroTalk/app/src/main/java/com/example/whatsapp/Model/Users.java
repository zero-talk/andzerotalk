package com.example.whatsapp.Model;

import java.io.Serializable;

public class Users implements Serializable {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String gender;
    private String pro_q1;
    private String pro_q2;
    private String pro_q3;

    public Users() {
    }

    public Users(String id, String username, String imageURL, String status,
                 String gender, String pro_q1, String pro_q2, String pro_q3) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.gender = gender;
        this.pro_q1 = pro_q1;
        this.pro_q2 = pro_q2;
        this.pro_q3 = pro_q3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPro_q1() {
        return pro_q1;
    }

    public void setPro_q1(String pro_q1) {
        this.pro_q1 = pro_q1;
    }

    public String getPro_q2() {
        return pro_q2;
    }

    public void setPro_q2(String pro_q2) {
        this.pro_q2 = pro_q2;
    }

    public String getPro_q3() {
        return pro_q3;
    }

    public void setPro_q3(String pro_q3) {
        this.pro_q3 = pro_q3;
    }
}


