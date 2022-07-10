package com.gannon.mysales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Srikanth.K on
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MySalesHistoryResponsePayLoad {

    @JsonProperty("message")
    private List<Message> message = null;
    @JsonProperty("status")
    private String status;
    @JsonProperty("error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @JsonProperty("statusCode")
    private Integer statusCode;

    @JsonProperty("message")
    public List<Message> getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(List<Message> message) {
        this.message = message;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Message {

        @JsonProperty("auctionUser")
        private String auctionUser;
        @JsonProperty("auctionDate")
        private String auctionDate;
        @JsonProperty("auctionAmount")
        private Integer auctionAmount;


        public String getAuctionUser() {
            return auctionUser;
        }

        public void setAuctionUser(String auctionUser) {
            this.auctionUser = auctionUser;
        }

        public String getAuctionDate() {
            return auctionDate;
        }

        public void setAuctionDate(String auctionDate) {
            this.auctionDate = auctionDate;
        }

        public Integer getAuctionAmount() {
            return auctionAmount;
        }

        public void setAuctionAmount(Integer auctionAmount) {
            this.auctionAmount = auctionAmount;
        }
    }
    }
