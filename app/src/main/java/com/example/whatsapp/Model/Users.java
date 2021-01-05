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
    private String hash1;
    private String hash2;
    private String hash3;
    private String hashtotal;

    public Users() {
    }

    public Users(String id, String username, String imageURL, String status,
                 String gender, String pro_q1, String pro_q2, String pro_q3,
                 String hash1, String hash2, String hash3, String hashtotal) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.gender = gender;
        this.pro_q1 = pro_q1;
        this.pro_q2 = pro_q2;
        this.pro_q3 = pro_q3;
        this.hash1 = hash1;
        this.hash2 = hash2;
        this.hash3 = hash3;
        this.hashtotal = hashtotal;

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

    public String getHash1() {
        return hash1;
    }

    public void setHash1(String hash1) {
        this.hash1 = hash1;
    }

    public String getHash2() {
        return hash2;
    }

    public void setHash2(String hash2) {
        this.hash2 = hash2;
    }

    public String getHash3() {
        return hash3;
    }

    public void setHash3(String hash3) {
        this.hash3 = hash3;
    }

    public String getHashtotal() {
        return hashtotal;
    }

    public void setHashtotal(String hashtotal) {
        this.hashtotal = hashtotal;
    }
}


