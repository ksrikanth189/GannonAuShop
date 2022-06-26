package com.gannon.myfavourite.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Srikanth.K on
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyFavouriteResponsePayLoad {

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

        @JsonProperty("productName")
        private String productName;
        @JsonProperty("imageUrl")
        private String imageUrl;
        @JsonProperty("totalCount")
        private Integer totalCount;
        @JsonProperty("auctionId")
        private Integer auctionId;
        @JsonProperty("donationId")
        private Integer donationId;
        @JsonProperty("auctionAmount")
        private Integer auctionAmount;
        @JsonProperty("closingDate")
        private String closingDate;


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
    }
    }
