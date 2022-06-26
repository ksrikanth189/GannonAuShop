package com.gannon.myfavourite.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Srikanth.K on
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
  public class MyFavouriteUpdateReqPayLoad {

  @JsonProperty("userId")
  private int userId;
  @JsonProperty("donationId")
  private int donationId;
  @JsonProperty("auctionId")
  private int auctionId;
  @JsonProperty("onOffFlag")
  private boolean onOffFlag;


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }


  public int getDonationId() {
    return donationId;
  }

  public void setDonationId(int donationId) {
    this.donationId = donationId;
  }

  public int getAuctionId() {
    return auctionId;
  }

  public void setAuctionId(int auctionId) {
    this.auctionId = auctionId;
  }

  public boolean isOnOffFlag() {
    return onOffFlag;
  }

  public void setOnOffFlag(boolean onOffFlag) {
    this.onOffFlag = onOffFlag;
  }
}