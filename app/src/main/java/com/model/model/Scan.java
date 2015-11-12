package com.model.model;

/**
 * Created by Fayme Shahriar on 8/27/2015.
 */
public class Scan {

    public int id;
    public int customer_member_id;
    public int scanner_member_id;
    public int store_id;
    public int transaction_price;
    public double saving;
    public double paid;
    public double cumulative_paid;
    public double cumulative_saving;
    public double cumulative_count;
    public String createDate;


    public Scan()
    {
        this.id = 0;
        this.customer_member_id = 0;
        this.scanner_member_id = 0;
        this.store_id = 0;
        this.transaction_price = 0;
        this.saving= 0.0;
        this.paid = 0.0;
        this.cumulative_paid= 0.0;
        this.cumulative_saving = 0.0;
        this.cumulative_count = 0.0;
        this.createDate = "";
    }


}
