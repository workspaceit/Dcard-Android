package com.model.model;

/**
 * Created by Fayme Shahriar on 8/27/2015.
 */
public class Member {

    public int id;
    public String member_code;
    public String email;
    public String fb_id;
    public String first_name;
    public String last_name;
    public int email_flag;
    public String search_flag;
    public String zip_code;
    public String create_date;
    public int user_type;
    public long store_id;
    public Member()
    {
        this.id = 0;
        this.member_code="";
        this.email="";
        this.first_name="";
        this.last_name="";
        this.email_flag=0;
        this.search_flag = "";
        this.zip_code="";
        this.create_date="";
        this.fb_id = "";
        this.user_type=0;
        this.store_id = 0;
    }




}
