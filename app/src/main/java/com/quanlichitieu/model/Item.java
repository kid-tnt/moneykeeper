package com.quanlichitieu.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Item implements Serializable {
    private String title, category, price, date,key;
    public Item() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Item(String title, String category, String price, String date) {

        this.title = title;
        this.category = category;
        this.price = price;
        this.date = date;
    }

    public Item(String title, String category, String price, String date, String key) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.date = date;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("category", category);
        result.put("price", price);
        result.put("date", date);

        return result;
    }
}
