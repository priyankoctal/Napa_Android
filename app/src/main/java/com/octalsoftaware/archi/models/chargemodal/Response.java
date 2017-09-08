
package com.octalsoftaware.archi.models.chargemodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private ChargeInformationPrefillmodalData data;
    @SerializedName("APIname")
    @Expose
    private String aPIname;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChargeInformationPrefillmodalData getData() {
        return data;
    }

    public void setData(ChargeInformationPrefillmodalData data) {
        this.data = data;
    }

    public String getAPIname() {
        return aPIname;
    }

    public void setAPIname(String aPIname) {
        this.aPIname = aPIname;
    }

}
