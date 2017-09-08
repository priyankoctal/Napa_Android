
package com.octalsoftaware.archi.models.chargemodal;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChargeInformationPrefillmodalData {

    @SerializedName("record_id")
    @Expose
    private String recordId;
    @SerializedName("surgeon")
    @Expose
    private String surgeon;
    @SerializedName("anesthesiologist")
    @Expose
    private String anesthesiologist;
    @SerializedName("crna")
    @Expose
    private String crna;
    @SerializedName("srna")
    @Expose
    private String srna;
    @SerializedName("resident")
    @Expose
    private String resident;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("modeofAnesthesia")
    @Expose
    private String modeofAnesthesia;
    @SerializedName("asa")
    @Expose
    private String asa;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("adjust_time")
    @Expose
    private String adjustTime;
    @SerializedName("is_adjust_time")
    @Expose
    private String isAdjustTime;
    @SerializedName("reason_adjust")
    @Expose
    private String reasonAdjust;
    @SerializedName("pacu_time")
    @Expose
    private String pacuTime;
    @Nullable
    @SerializedName("DiagnoseList")
    @Expose
    private List<DiagnoseList> diagnoseList = null;
    @Nullable
    @SerializedName("ProcedureList")
    @Expose
    private List<ProcedureList> procedureList = null;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = surgeon;
    }

    public String getAnesthesiologist() {
        return anesthesiologist;
    }

    public void setAnesthesiologist(String anesthesiologist) {
        this.anesthesiologist = anesthesiologist;
    }

    public String getCrna() {
        return crna;
    }

    public void setCrna(String crna) {
        this.crna = crna;
    }

    public String getSrna() {
        return srna;
    }

    public void setSrna(String srna) {
        this.srna = srna;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getModeofAnesthesia() {
        return modeofAnesthesia;
    }

    public void setModeofAnesthesia(String modeofAnesthesia) {
        this.modeofAnesthesia = modeofAnesthesia;
    }

    public String getAsa() {
        return asa;
    }

    public void setAsa(String asa) {
        this.asa = asa;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(String adjustTime) {
        this.adjustTime = adjustTime;
    }

    public String getIsAdjustTime() {
        return isAdjustTime;
    }

    public void setIsAdjustTime(String isAdjustTime) {
        this.isAdjustTime = isAdjustTime;
    }

    public String getReasonAdjust() {
        return reasonAdjust;
    }

    public void setReasonAdjust(String reasonAdjust) {
        this.reasonAdjust = reasonAdjust;
    }

    public String getPacuTime() {
        return pacuTime;
    }

    public void setPacuTime(String pacuTime) {
        this.pacuTime = pacuTime;
    }

    @Nullable
    public List<DiagnoseList> getDiagnoseList() {
        return diagnoseList;
    }

    public void setDiagnoseList(List<DiagnoseList> diagnoseList) {
        this.diagnoseList = diagnoseList;
    }

    @Nullable
    public List<ProcedureList> getProcedureList() {
        return procedureList;
    }

    public void setProcedureList(List<ProcedureList> procedureList) {
        this.procedureList = procedureList;
    }

}
