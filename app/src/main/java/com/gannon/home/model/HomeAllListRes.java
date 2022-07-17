package com.gannon.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeAllListRes {

    @JsonProperty("statusCode")
    private Integer statusCode;
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

    @JsonProperty("message")
    private List<Message> message = null;

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
    public List<Message> getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(List<Message> message) {
        this.message = message;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String productName;
        private String imageUrl;
        private Integer totalCount;
        private Integer auctionId;
        private Integer donationId;
        private Integer auctionAmount;
        private String closingDate;
        private Boolean favouriteCheck;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
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

        public Integer getAuctionAmount() {
            return auctionAmount;
        }

        public void setAuctionAmount(Integer auctionAmount) {
            this.auctionAmount = auctionAmount;
        }

        public String getClosingDate() {
            return closingDate;
        }

        public void setClosingDate(String closingDate) {
            this.closingDate = closingDate;
        }

        public Boolean getFavouriteCheck() {
            return favouriteCheck;
        }

        public void setFavouriteCheck(Boolean favouriteCheck) {
            this.favouriteCheck = favouriteCheck;
        }
    }
    }