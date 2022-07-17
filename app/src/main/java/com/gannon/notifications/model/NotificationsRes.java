package com.gannon.notifications.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Srikanth.K on
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationsRes {

    @JsonProperty("message")
    private List<Message> message = null;
    @JsonProperty("status")
    private String status;
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

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("imageUrl")
        @Expose
        private String imageUrl;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("auctionId")
        @Expose
        private Integer auctionId;
        @SerializedName("donationId")
        @Expose
        private Integer donationId;
        @SerializedName("auctionOrDonation")
        @Expose
        private String auctionOrDonation;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getAuctionId() {
            return auctionId;
        }

        public void setAuctionId(Integer auctionId) {
            this.auctionId = auctionId;
        }

        public Integer getDonationId() {
            return donationId;
        }

        public void setDonationId(Integer donationId) {
            this.donationId = donationId;
        }

        public String getAuctionOrDonation() {
            return auctionOrDonation;
        }

        public void setAuctionOrDonation(String auctionOrDonation) {
            this.auctionOrDonation = auctionOrDonation;
        }
    }
    }
