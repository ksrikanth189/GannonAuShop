package com.gannon.uploadAuctionDonation.interactor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srikanth.K on .
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class NewAuctionDonationSaveReq {

    @JsonProperty("auctionOrDonation")
    private String auctionOrDonation;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("productDescription")
    private String productDescription;
    @JsonProperty("closeDate")
    private String closeDate;
    @JsonProperty("auctionAmount")
    private Integer auctionAmount;
    @JsonProperty("userId")
    private Integer userId;
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

    @JsonProperty("closeDate")
    public String getCloseDate() {
        return closeDate;
    }

    @JsonProperty("closeDate")
    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    @JsonProperty("auctionAmount")
    public Integer getAuctionAmount() {
        return auctionAmount;
    }

    @JsonProperty("auctionAmount")
    public void setAuctionAmount(Integer auctionAmount) {
        this.auctionAmount = auctionAmount;
    }

    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("imagesList")
    public List<String> getImagesList() {
        return imagesList;
    }

    @JsonProperty("imagesList")
    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }
}


