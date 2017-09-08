package com.octalsoftaware.archi.models;

import java.io.Serializable;

/**
 * Created by anandj on 4/19/2017.
 */

public class HomePageModal implements Serializable {

    private String id;
    private String name;
    private String dob;
    private String gender;
    private String status;
    private String is_completed;
    private String location_id;
    private String location_name;
    private String status3;
    private String site;
    private String is_charged;
    private String is_qi;
    private String case_id;
    private String totalImages;
    private String start_date;

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(String is_completed) {
        this.is_completed = is_completed;
    }

    public String getStatus3() {
        return status3;
    }

    public void setStatus3(String status3) {
        this.status3 = status3;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getIs_charged() {
        return is_charged;
    }

    public void setIs_charged(String is_charged) {
        this.is_charged = is_charged;
    }

    public String getIs_qi() {
        return is_qi;
    }

    public void setIs_qi(String is_qi) {
        this.is_qi = is_qi;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(String totalImages) {
        this.totalImages = totalImages;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @Override
    public String toString() {
        return location_name;
    }
}
