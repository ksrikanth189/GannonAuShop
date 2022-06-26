package com.gannon.uploadAuctionDonation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Srikanth.K on .
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUploadRespPojo {
    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private List<String> data = null;

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public List<String> getData() {
        return data;
    }

    @JsonProperty("message")
    public void setData(List<String> data) {
        this.data = data;
    }

}