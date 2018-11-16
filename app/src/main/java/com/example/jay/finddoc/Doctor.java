package com.example.jay.finddoc;

import java.io.Serializable;

public class Doctor implements Serializable{

    private String head,firstName, lastName,dEmail,time;
    private String dob;
    private String mno;
    private String gender;
    private String address;
    private String lat;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    private String lng;

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getdEmail() {
        return dEmail;
    }
    public String getHead() {
        return head;
    }
    public String gettime() {
        return time;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setdEmail(String dEmail) {this.dEmail = dEmail;}
    public void setHead(String head) {this.head = head;}
    public void settime(String time) {this.time = time;}
}
