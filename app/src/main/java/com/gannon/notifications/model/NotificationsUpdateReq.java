package com.gannon.notifications.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Srikanth.K on
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
  public class NotificationsUpdateReq {

  private int userId;
  private int auctionId;
  private int donationId;
  private int read;


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
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

  public int getRead() {
    return read;
  }

  public void setRead(int read) {
    this.read = read;
  }
}