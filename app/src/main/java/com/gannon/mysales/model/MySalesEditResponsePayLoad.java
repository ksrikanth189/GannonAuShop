package com.gannon.mysales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by Srikanth.K on
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MySalesEditResponsePayLoad {
    @JsonProperty("message")
    private Message message;
    @JsonProperty("status")
    private String status;
    @JsonProperty("statusCode")
    private Integer statusCode;

    @JsonProperty("message")
    public Message getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(Message message) {
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
    public static class Message {

        @JsonProperty("productName")
        private String productName;
        @JsonProperty("productDescription")
        private String productDescription;
        @JsonProperty("auctionCloseDate")
        private String auctionCloseDate;
        @JsonProperty("auctionStatus")
        private String auctionStatus;
        @JsonProperty("auctionAmount")
        private Integer auctionAmount;
        @JsonProperty("initialAmount")
        private Integer initialAmount;
        @JsonProperty("imagesList")
        private List<Images> imagesList = null;

        public List<Images> getImagesList() {
            return imagesList;
        }

        public void setImagesList(List<Images> imagesList) {
            this.imagesList = imagesList;
        }

        @JsonProperty("productName")
        public String getProductName() {
            return productName;
        }

        @JsonProperty("productName")
        public void setProductName(String productName) {
            this.productName = productName;
        }

        @JsonProperty("productDescription")
        public String getProductDescription() {
            return productDescription;
        }

        @JsonProperty("productDescription")
        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        @JsonProperty("auctionCloseDate")
        public String getAuctionCloseDate() {
            return auctionCloseDate;
        }

        @JsonProperty("auctionCloseDate")
        public void setAuctionCloseDate(String auctionCloseDate) {
            this.auctionCloseDate = auctionCloseDate;
        }

        @JsonProperty("auctionStatus")
        public String getAuctionStatus() {
            return auctionStatus;
        }

        @JsonProperty("auctionStatus")
        public void setAuctionStatus(String auctionStatus) {
            this.auctionStatus = auctionStatus;
        }

        @JsonProperty("auctionAmount")
        public Integer getAuctionAmount() {
            return auctionAmount;
        }

        @JsonProperty("auctionAmount")
        public void setAuctionAmount(Integer auctionAmount) {
            this.auctionAmount = auctionAmount;
        }

        @JsonProperty("initialAmount")
        public Integer getInitialAmount() {
            return initialAmount;
        }

        @JsonProperty("initialAmount")
        public void setInitialAmount(Integer initialAmount) {
            this.initialAmount = initialAmount;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonPropertyOrder({
                "id",
                "url"
        })
        public static class Images {

            @JsonProperty("id")
            private Integer id;
            @JsonProperty("url")
            private String url;

            @JsonProperty("id")
            public Integer getId() {
                return id;
            }

            @JsonProperty("id")
            public void setId(Integer id) {
                this.id = id;
            }

            @JsonProperty("url")
            public String getUrl() {
                return url;
            }

            @JsonProperty("url")
            public void setUrl(String url) {
                this.url = url;
            }

        }
    }
}
