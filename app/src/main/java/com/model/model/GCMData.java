package com.model.model;

/**
 * Created by Fayme Shahriar on 9/10/2015.
 */
public class GCMData {

    public String storeName;
    public double amountOff;
    public double percentOff;
    public double onSpent;
    public double discount;
    public double totalSpent;
    public double currentAmount;
    public int transactionCount;


    public GCMData()
    {

        this.storeName = "";
        this.amountOff = 0.0;
        this.percentOff = 0.0;
        this.onSpent = 0.0;
        this.discount = 0.0;
        this.totalSpent = 0.0;
        this.currentAmount = 0.0;
        this.transactionCount = 0;

    }

}
