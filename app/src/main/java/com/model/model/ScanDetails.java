package com.model.model;

/**
 * Created by Fayme Shahriar on 8/27/2015.
 */
public class ScanDetails extends Scan {


    public Member customer;
    public Store store;
    public Member scanner;
    //public String createDate;


    public ScanDetails()
    {

        this.scanner = new Member();
        this.customer = new Member();
        this.store = new Store();
        //this.createDate = "";


    }

}
