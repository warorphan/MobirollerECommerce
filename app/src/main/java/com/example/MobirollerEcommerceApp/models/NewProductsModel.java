package com.example.MobirollerEcommerceApp.models;

import java.io.Serializable;

public class NewProductsModel implements Serializable {

    String description;
    String name;
    String price;
    String img_url;
    String rating;
    String company_name;

    public NewProductsModel() {
    }

    public NewProductsModel(String description, String name, String price, String img_url, String rating,String company_name) {
        this.description = description;
        this.name = name;
        this.price = price;
        this.img_url = img_url;
        this.rating = rating;
        this.company_name = company_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
