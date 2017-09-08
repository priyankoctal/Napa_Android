
package com.octalsoftaware.archi.models.chargemodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiagnoseList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("diagnose_id")
    @Expose
    private String diagnoseId;
    @SerializedName("diagnosis_desc")
    @Expose
    private String diagnosisDesc;
    @SerializedName("diagnosis_code")
    @Expose
    private String diagnosisCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiagnoseId() {
        return diagnoseId;
    }

    public void setDiagnoseId(String diagnoseId) {
        this.diagnoseId = diagnoseId;
    }

    public String getDiagnosisDesc() {
        return diagnosisDesc;
    }

    public void setDiagnosisDesc(String diagnosisDesc) {
        this.diagnosisDesc = diagnosisDesc;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

}
