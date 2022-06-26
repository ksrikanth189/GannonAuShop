package com.gannon.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "status",
        "message",
        "slides"
})
public class HomeSlidingListRes {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("result1")
    private List<Result1> result1 = null;
    @JsonProperty("result2")
    private List<Result2> result2 = null;

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("result1")
    public List<Result1> getResult1() {
        return result1;
    }

    @JsonProperty("result1")
    public void setResult1(List<Result1> result1) {
        this.result1 = result1;
    }

    @JsonProperty("result2")
    public List<Result2> getResult2() {
        return result2;
    }

    @JsonProperty("result2")
    public void setResult2(List<Result2> result2) {
        this.result2 = result2;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder({
            "slno",
            "name",
            "image"
    })
    public static class Result1 {

        @JsonProperty("slno")
        private String slno;
        @JsonProperty("name")
        private String name;
        @JsonProperty("image")
        private String image;

        @JsonProperty("slno")
        public String getSlno() {
            return slno;
        }

        @JsonProperty("slno")
        public void setSlno(String slno) {
            this.slno = slno;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("image")
        public String getImage() {
            return image;
        }

        @JsonProperty("image")
        public void setImage(String image) {
            this.image = image;
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    @JsonPropertyOrder({
            "category_slno",
            "category_name",
            "slno",
            "name",
            "image",
            "mrp",
            "discount",
            "description",
            "product_net",
            "product_gross",
            "no_of_items",
            "product_type",
            "popular_status",
            "notification_status"
    })
    public static class Result2 {

        @JsonProperty("category_slno")
        private String category_slno;
        @JsonProperty("category_name")
        private String category_name;
        @JsonProperty("slno")
        private String slno;
        @JsonProperty("name")
        private String name;
        @JsonProperty("image")
        private String image;
        @JsonProperty("mrp")
        private String mrp;
        @JsonProperty("discount")
        private String discount;
        @JsonProperty("description")
        private String description;
        @JsonProperty("product_net")
        private String product_net;
        @JsonProperty("product_gross")
        private String product_gross;
        @JsonProperty("no_of_items")
        private String no_of_items;
        @JsonProperty("product_type")
        private String product_type;
        @JsonProperty("popular_status")
        private String popular_status;
        @JsonProperty("notification_status")
        private String notification_status;

        @JsonProperty("category_slno")
        public String getCategory_slno() {
            return category_slno;
        }

        @JsonProperty("category_slno")
        public void setCategory_slno(String category_slno) {
            this.category_slno = category_slno;
        }

        @JsonProperty("category_name")
        public String getCategory_name() {
            return category_name;
        }

        @JsonProperty("category_name")
        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        @JsonProperty("slno")
        public String getSlno() {
            return slno;
        }

        @JsonProperty("slno")
        public void setSlno(String slno) {
            this.slno = slno;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("image")
        public String getImage() {
            return image;
        }

        @JsonProperty("image")
        public void setImage(String image) {
            this.image = image;
        }

        @JsonProperty("mrp")
        public String getMrp() {
            return mrp;
        }

        @JsonProperty("mrp")
        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        @JsonProperty("discount")
        public String getDiscount() {
            return discount;
        }

        @JsonProperty("discount")
        public void setDiscount(String discount) {
            this.discount = discount;
        }

        @JsonProperty("description")
        public String getDescription() {
            return description;
        }

        @JsonProperty("description")
        public void setDescription(String description) {
            this.description = description;
        }

        @JsonProperty("product_net")
        public String getProduct_net() {
            return product_net;
        }

        @JsonProperty("product_net")
        public void setProduct_net(String product_net) {
            this.product_net = product_net;
        }

        @JsonProperty("product_gross")
        public String getProduct_gross() {
            return product_gross;
        }

        @JsonProperty("product_gross")
        public void setProduct_gross(String product_gross) {
            this.product_gross = product_gross;
        }

        @JsonProperty("no_of_items")
        public String getNo_of_items() {
            return no_of_items;
        }

        @JsonProperty("no_of_items")
        public void setNo_of_items(String no_of_items) {
            this.no_of_items = no_of_items;
        }

        @JsonProperty("product_type")
        public String getProduct_type() {
            return product_type;
        }

        @JsonProperty("product_type")
        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        @JsonProperty("popular_status")
        public String getPopular_status() {
            return popular_status;
        }

        @JsonProperty("popular_status")
        public void setPopular_status(String popular_status) {
            this.popular_status = popular_status;
        }

        @JsonProperty("notification_status")
        public String getNotification_status() {
            return notification_status;
        }

        @JsonProperty("notification_status")
        public void setNotification_status(String notification_status) {
            this.notification_status = notification_status;
        }

    }

}
