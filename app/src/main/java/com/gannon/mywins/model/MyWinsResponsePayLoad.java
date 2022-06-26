package com.gannon.mywins.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Srikanth.K on
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyWinsResponsePayLoad {

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

        @JsonProperty("productName")
        public String getProductName() {
            return productName;
        }

        @JsonProperty("productName")
        public void setProductName(String productName) {
            this.productName = productName;
        }

        @JsonProperty("imageUrl")
        public String getImageUrl() {
            return imageUrl;
        }

        @JsonProperty("imageUrl")
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @JsonProperty("totalCount")
        public Integer getTotalCount() {
            return totalCount;
        }

        @JsonProperty("totalCount")
        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        @JsonProperty("auctionId")
        public Integer getAuctionId() {
            return auctionId;
        }

        @JsonProperty("auctionId")
        public void setAuctionId(Integer auctionId) {
            this.auctionId = auctionId;
        }

        @JsonProperty("donationId")
        public Integer getDonationId() {
            return donationId;
        }

        @JsonProperty("donationId")
        public void setDonationId(Integer donationId) {
            this.donationId = donationId;
        }

    }
    }
