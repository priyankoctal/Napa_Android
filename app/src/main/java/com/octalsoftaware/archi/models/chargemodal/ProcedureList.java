
package com.octalsoftaware.archi.models.chargemodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcedureList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("procedure_code")
    @Expose
    private String procedureCode;
    @SerializedName("procedure_code_type")
    @Expose
    private String procedureCodeType;
    @SerializedName("modifier1")
    @Expose
    private String modifier1;
    @SerializedName("modifier2")
    @Expose
    private String modifier2;
    @SerializedName("modifier3")
    @Expose
    private String modifier3;
    @SerializedName("icd1")
    @Expose
    private String icd1;
    @SerializedName("icd2")
    @Expose
    private String icd2;
    @SerializedName("icd3")
    @Expose
    private String icd3;
    @SerializedName("icd4")
    @Expose
    private String icd4;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureCodeType() {
        return procedureCodeType;
    }

    public void setProcedureCodeType(String procedureCodeType) {
        this.procedureCodeType = procedureCodeType;
    }

    public String getModifier1() {
        return modifier1;
    }

    public void setModifier1(String modifier1) {
        this.modifier1 = modifier1;
    }

    public String getModifier2() {
        return modifier2;
    }

    public void setModifier2(String modifier2) {
        this.modifier2 = modifier2;
    }

    public String getModifier3() {
        return modifier3;
    }

    public void setModifier3(String modifier3) {
        this.modifier3 = modifier3;
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

}
