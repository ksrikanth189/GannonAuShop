package com.gannon.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeAllListRes {

    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("status")
    private String status;
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
        @JsonProperty("productName")
        private String productName;
        @JsonProperty("imageUrl")
        private String imageUrl;
        @JsonProperty("totalCount")
        private int totalCount;
        @JsonProperty("auctionId")
        private int auctionId;
        @JsonProperty("donationId")
        private int donationId;
        @JsonProperty("auctionAmount")
        private float auctionAmount;
        @JsonProperty("closingDate")
        private String closingDate;
        @JsonProperty("favouriteCheck")
        private boolean favouriteCheck;

        public boolean isFavouriteCheck() {
            return favouriteCheck;
        }

        public void setFavouriteCheck(boolean favouriteCheck) {
            this.favouriteCheck = favouriteCheck;
        }

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

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getAuctionId() {
            return auctionId;
        }

        public void setAuctionId(int auctionId) {
            this.auctionId = auctionId;
        }

        public int getDonationId() {
            return donationId;
        }

        public void setDonationId(int donationId) {
            this.donationId = donationId;
        }

        public float getAuctionAmount() {
            return auctionAmount;
        }

        public void setAuctionAmount(float auctionAmount) {
            this.auctionAmount = auctionAmount;
        }

        public String getClosingDate() {
            return closingDate;
        }

        public void setClosingDate(String closingDate) {
            this.closingDate = closingDate;
        }
    }
}