package com.service;

import android.util.Log;

import com.Utility.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.model.LoggedInMember;
import com.model.model.Member;
import com.model.model.ResponseStatus;
import com.model.model.Store;
import com.model.model.User;

/**
 * Created by Fayme Shahriar on 8/26/2015.
 */
public class LoginService extends DcardService {

    private LoggedInMember member;
    private ResponseStatus responseObj;
    private Store store;



    public boolean authintication(User user) {

        this.member = new LoggedInMember();
        this.store = new Store();
        this.responseObj = new ResponseStatus();
        this.setController("registration/facebook");
        // Log.v("username", username);
        this.setParams("email", user.email);
        this.setParams("first_name", user.first_name);
        this.setParams("last_name", user.last_name);
        this.setParams("fb_id", user.fbId);
        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status==true) {
                Log.v("check", "true");

                this.member = gSonObj.fromJson(jObj.get("member"), member.getClass());
                this.store = gSonObj.fromJson(jObj.get("store"), store.getClass());
                Utility.store = store;
                //System.out.println(this.member.member_code);
                Utility.member = this.member;
                Utility.fbPicLink = "https://graph.facebook.com/"+Utility.member.fb_id+"/picture?type=small";

                return true;
            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }


    public boolean accessTokenLogin(String access_token)
    {
        this.member = new LoggedInMember();
        this.store = new Store();
        this.responseObj = new ResponseStatus();
        this.setController("login/authentication");
        // Log.v("username", username);
        this.setParams("access_token", access_token);

        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status==true) {
                Log.v("check", "true");


                this.member = gSonObj.fromJson(jObj.get("member"), member.getClass());
                this.store = gSonObj.fromJson(jObj.get("store"), store.getClass());
                //System.out.println(this.member.member_code);
                Utility.store = this.store;
                Utility.member = this.member;
                Utility.fbPicLink = "https://graph.facebook.com/"+Utility.member.fb_id+"/picture?type=large";
                return true;
            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());

            return false;
        }




    }

    public boolean registerGCM(String id)
    {

        this.responseObj = new ResponseStatus();
        this.setController("device/register");
        // Log.v("username", username);
        this.setParams("device_id", id);

        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status==true) {
                Log.v("check", "true");
                return true;
            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());

            return false;
        }




    }





}
