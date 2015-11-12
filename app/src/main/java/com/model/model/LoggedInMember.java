package com.model.model;

/**
 * Created by Fayme Shahriar on 8/31/2015.
 */
public class LoggedInMember extends Member {

    public String access_token;

    public LoggedInMember()
    {
        this.access_token = "";

    }
}
