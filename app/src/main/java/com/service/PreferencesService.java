package com.service;

import android.util.Log;

import com.Utility.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.model.LoggedInMember;
import com.model.model.Member;
import com.model.model.PrefernceModel;
import com.model.model.ResponseStatus;
import com.model.model.Scan;

import java.security.PrivateKey;

/**
 * Created by Fayme Shahriar on 8/31/2015.
 */
public class PreferencesService extends DcardService {

    private Member member;
    private double savings;
    private ResponseStatus responseObj;

    public PrefernceModel getPreferences() {


        this.member = new Member();
        this.responseObj = new ResponseStatus();
        this.savings=0.0;
        this.setController("member/get/preference");

        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status == true) {
                Log.v("check", "true");


                this.member = gSonObj.fromJson(jObj.get("member"), member.getClass());
                System.out.println("search_flag:" + this.member.search_flag);
                this.savings = jObj.get("saving").getAsDouble();


                return new PrefernceModel(this.member,this.savings,true);

            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return new PrefernceModel(this.member,this.savings,false);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return new PrefernceModel(this.member,this.savings,false);


    }


    public boolean updatePreferences(boolean email_preference,String search_preference,String zipcode)
    {
        this.member = new Member();
        this.responseObj = new ResponseStatus();
        this.setController("member/update/preference");
        System.out.println(email_preference);
        this.setParams("email_preference", String.valueOf(email_preference));
        this.setParams("search_preference", search_preference);
        this.setParams("zip_code", zipcode);

        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status == true) {
                Log.v("check", "true");


                this.member = gSonObj.fromJson(jObj.get("member"), member.getClass());
                Utility.member.email_flag = this.member.email_flag;
                Utility.member.zip_code= this.member.zip_code;
                Utility.member.search_flag = this.member.search_flag;



                return true;

            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }


        return false;
    }


}
