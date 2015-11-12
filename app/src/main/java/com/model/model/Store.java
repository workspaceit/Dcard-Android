package com.model.model;

/**
 * Created by Fayme Shahriar on 8/27/2015.
 */
public class Store {

    public int store_id;
    public String yelp_id;
    public String invite_code;
    public String store_name;
    public String store_state;
    public String store_city;
    public String store_zip;
    public String store_country;
    public Category category;
    public double lat;
    public double lon;
    public String created_date;
    public double percent_off;
    public double amount_off;
    public double on_spent;
    public boolean participator;
    public String phone;

    public Store() {
        this.percent_off = 0.0;
        this.amount_off = 0.0;
        this.on_spent = 0.0;
        this.participator = false;
        this.store_id = 0;
        this.yelp_id = "";
        this.invite_code = "";
        this.store_name = "";
        this.store_state = "";
        this.store_city = "";
        this.store_zip = "";
        this.store_country = "";
        this.created_date = "";
        this.category = new Category();
        this.lat = 0.0f;
        this.lon = 0.0f;
        this.phone = "";


    }


}
