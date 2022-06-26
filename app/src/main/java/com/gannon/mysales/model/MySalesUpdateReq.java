package com.gannon.mysales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Srikanth.K on .
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class MySalesUpdateReq {


    @JsonProperty("productName")
    private String productName;
    @JsonProperty("productDescription")
    private String productDescription;
    @JsonProperty("auctionCloseDate")
    private String auctionCloseDate;
    @JsonProperty("auctionStatus")
    private String auctionStatus;
    @JsonProperty("auctionId")
    private Integer auctionId;
    @JsonProperty("donationId")
    private Integer donationId;
    @JsonProperty("auctionOrDonation")
    private String auctionOrDonation;


    @JsonProperty("imagesList")
    private List<String> imagesList = null;

    @JsonProperty("auctionOrDonation")
    public String getAuctionOrDonation() {
        return auctionOrDonation;
    }

    @JsonProperty("auctionOrDonation")
    public void setAuctionOrDonation(String auctionOrDonation) {
        this.auctionOrDonation = auctionOrDonation;
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


    @JsonProperty("imagesList")
    public List<String> getImagesList() {
        return imagesList;
    }

    @JsonProperty("imagesList")
    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public String getAuctionCloseDate() {
        return auctionCloseDate;
    }

    public void setAuctionCloseDate(String auctionCloseDate) {
        this.auctionCloseDate = auctionCloseDate;
    }

    public String getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(String auctionStatus) {
        this.auctionStatus = auctionStatus;
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
}


