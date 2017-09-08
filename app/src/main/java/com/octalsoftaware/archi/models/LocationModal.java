package com.octalsoftaware.archi.models;

import java.io.Serializable;

/**
 * Created by anandj on 4/28/2017.
 */

public class LocationModal implements Serializable {
    private String id;
    private String office;
    private String lname;
    private String fname;
    private String ref_code;
    private String title;
    private String diagnosis_code_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getRef_code() {
        return ref_code;
    }

    public void setRef_code(String ref_code) {
        this.ref_code = ref_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiagnosis_code_type() {
        return diagnosis_code_type;
    }

    public void setDiagnosis_code_type(String diagnosis_code_type) {
        this.diagnosis_code_type = diagnosis_code_type;
    }
}
