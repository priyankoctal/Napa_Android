package com.octalsoftaware.archi.models;

import java.io.Serializable;

/**
 * Created by anandj on 5/9/2017.
 */

public class ProceduresModal implements Serializable{
    String code;
    String code_statis;
    String mode1;
    String mode2;
    String mode3;
    String icd1;
    String icd2;
    String icd3;
    String icd4;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode_statis() {
        return code_statis;
    }

    public void setCode_statis(String code_statis) {
        this.code_statis = code_statis;
    }

    public String getMode1() {
        return mode1;
    }

    public void setMode1(String mode1) {
        this.mode1 = mode1;
    }

    public String getMode2() {
        return mode2;
    }

    public void setMode2(String mode2) {
        this.mode2 = mode2;
    }

    public String getMode3() {
        return mode3;
    }

    public void setMode3(String mode3) {
        this.mode3 = mode3;
    }

    public String getIcd1() {
        return icd1;
    }

    public void setIcd1(String icd1) {
        this.icd1 = icd1;
    }

    public String getIcd2() {
        return icd2;
    }

    public void setIcd2(String icd2) {
        this.icd2 = icd2;
    }

    public String getIcd3() {
        return icd3;
    }

    public void setIcd3(String icd3) {
        this.icd3 = icd3;
    }

    public String getIcd4() {
        return icd4;
    }

    public void setIcd4(String icd4) {
        this.icd4 = icd4;
    }

    @Override
    public String toString() {
        return code;
    }

}
