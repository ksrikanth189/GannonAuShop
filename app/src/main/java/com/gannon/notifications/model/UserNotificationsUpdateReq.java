package com.gannon.notifications.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Srikanth.K on
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
  public class UserNotificationsUpdateReq {

  private int registerId;
  private int read;



  public int getRead() {
    return read;
  }

  public void setRead(int read) {
    this.read = read;
  }


  public int getRegisterId() {
    return registerId;
  }

  public void setRegisterId(int registerId) {
    this.registerId = registerId;
  }
}