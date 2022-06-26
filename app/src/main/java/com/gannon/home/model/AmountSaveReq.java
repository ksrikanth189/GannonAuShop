package com.gannon.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmountSaveReq {

    @JsonProperty("auctionId")
    private Integer auctionId;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("auctionAmount")
    private String auctionAmount;


    public Integer getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAuctionAmount() {
        return auctionAmount;
    }

    public void setAuctionAmount(String auctionAmount) {
        this.auctionAmount = auctionAmount;
    }
}
