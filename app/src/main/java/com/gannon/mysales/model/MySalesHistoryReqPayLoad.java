package com.gannon.mysales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Srikanth.K on
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
  public class MySalesHistoryReqPayLoad {

  @JsonProperty("auctionId")
  private int auctionId;
  @JsonProperty("donationId")
  private int donationId;
  @JsonProperty("auctionOrDonation")
  private String auctionOrDonation;


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

  public String getAuctionOrDonation() {
    return auctionOrDonation;
  }

  public void setAuctionOrDonation(String auctionOrDonation) {
    this.auctionOrDonation = auctionOrDonation;
  }
}