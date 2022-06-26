package com.gannon.mysales.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Srikanth.K on
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
  public class MySalesReqPayLoad {

  @JsonProperty("userId")
  private int userId;
  @JsonProperty("auctionOrDonation")
  private String auctionOrDonation;
  @JsonProperty("offset")
  private int offset;
  @JsonProperty("limit")
  private int limit;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getAuctionOrDonation() {
    return auctionOrDonation;
  }

  public void setAuctionOrDonation(String auctionOrDonation) {
    this.auctionOrDonation = auctionOrDonation;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }
}