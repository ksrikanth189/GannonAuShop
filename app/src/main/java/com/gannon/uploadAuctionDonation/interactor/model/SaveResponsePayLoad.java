package com.gannon.uploadAuctionDonation.interactor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Srikanth.K on .
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveResponsePayLoad {
    @JsonProperty(value = "statusCode")
    private String statusCode;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
