package com.gannon.Login.interactor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginActivityRes {

    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private Message message;

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public Message getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(Message message) {
        this.message = message;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder({
            "userId",
            "message",
            "userName",
            "adminFlag"
    })
    public static class Message {

        @JsonProperty("userId")
        private Integer userId;
        @JsonProperty("message")
        private String message;
        @JsonProperty("userName")
        private String userName;
        @JsonProperty("adminFlag")
        private Boolean adminFlag;

        @JsonProperty("userId")
        public Integer getUserId() {
            return userId;
        }

        @JsonProperty("userId")
        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        @JsonProperty("message")
        public String getMessage() {
            return message;
        }

        @JsonProperty("message")
        public void setMessage(String message) {
            this.message = message;
        }

        @JsonProperty("userName")
        public String getUserName() {
            return userName;
        }

        @JsonProperty("userName")
        public void setUserName(String userName) {
            this.userName = userName;
        }

        @JsonProperty("adminFlag")
        public Boolean getAdminFlag() {
            return adminFlag;
        }

        @JsonProperty("adminFlag")
        public void setAdminFlag(Boolean adminFlag) {
            this.adminFlag = adminFlag;
        }

    }
}