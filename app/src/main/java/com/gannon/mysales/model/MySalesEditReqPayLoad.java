package com.gannon.mysales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Srikanth.K on
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
  public class MySalesEditReqPayLoad {

  @JsonProperty("auctionId")
  private int auctionId;
  @JsonProperty("auctionOrDonation")
  private String auctionOrDonation;
  @JsonProperty("donationId")
  private int donationId;


  public int getAuctionId() {
    return auctionId;
  }

  public void setAuctionId(int auctionId) {
    this.auctionId = auctionId;
  }

  public String getAuctionOrDonation() {
    return auctionOrDonation;
  }

  public void setAuctionOrDonation(String auctionOrDonation) {
    this.auctionOrDonation = auctionOrDonation;
  }

  public int getDonationId() {
    return donationId;
  }

  public void setDonationId(int donationId) {
    this.donationId = donationId;
  }
}