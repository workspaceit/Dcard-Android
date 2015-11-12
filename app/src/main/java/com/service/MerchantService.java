package com.service;

import android.util.Log;

import com.Utility.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.model.Member;
import com.model.model.ResponseStatus;
import com.model.model.Store;

/**
 * Created by Fayme Shahriar on 9/1/2015.
 */
public class MerchantService extends DcardService {

    private Member member;
    private Store store;
    private ResponseStatus responseObj;


    public MerchantService()
    {




    }



    public boolean updateUserType(String merchant_code)
    {
        this.member = new Member();
        this.store = new Store();
        this.responseObj = new ResponseStatus();
        this.setController("merchant/add");

        this.setParams("merchant_code", merchant_code);
        //this.setParams("member_id", String.valueOf(member_id));


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
                Utility.member.user_type = this.member.user_type;
                this.store = gSonObj.fromJson(jObj.get("store"), store.getClass());

                Utility.store = this.store;


                return true;

            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
        }


        return false;
    }




}
