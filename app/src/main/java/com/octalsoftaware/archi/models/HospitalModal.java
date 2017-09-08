package com.octalsoftaware.archi.models;

/**
 * Created by anandj on 5/8/2017.
 */

public class HospitalModal {
    private String id;
    private String name;
    private String code_id;
    private String status;
    private String state_id;

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

    public String getCode_id() {
        return code_id;
    }

    public void setCode_id(String code_id) {
        this.code_id = code_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String toString() {
        return name;
    }
}
