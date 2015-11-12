package com.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.model.Member;
import com.model.model.ResponseStatus;
import com.model.model.ScanResult;

/**
 * Created by Fayme Shahriar on 9/7/2015.
 */
public class ScanService extends DcardService {

    private Member member;
    private ResponseStatus responseObj;
    private ScanResult sr;


    public ScanResult getMember(String member_code) {
        this.member = new Member();
        this.sr = new ScanResult();
        this.responseObj = new ResponseStatus();
        this.setController("member/get/" + member_code);


        String resp = this.getData("GET");
        Log.v("resp", resp);

        try
        {

            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());

            if (this.responseObj.status == true) {
                Log.v("check", "true");

                member =  gSonObj.fromJson(jObj.get("member"), member.getClass());
                sr.member = member;
                sr.rObj = responseObj;
                return sr;
            } else {
                Log.v("check", "false");
                // System.out.print(resp);
                return sr;
            }


        }catch (Exception ex)
        {


        }


        return sr;
    }


    public boolean deleteLastTransaction(int member_id)
    {

        this.responseObj = new ResponseStatus();
        this.setController("scan/delete/last");
        this.setParams("member_id",String.valueOf(member_id));



        String resp = this.getData("POST");
        Log.v("resp", resp);
        try {

            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();
            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            if (this.responseObj.status == true) {
                Log.v("check", "true");

                return true;
            } else {
                Log.v("check", "false");
                // System.out.print(resp);
                return false;


            }

        }
        catch (Exception ex)
        {


        }
     return false;

    }


    public boolean addTransition(int member_id,double amount) {

        this.responseObj = new ResponseStatus();
        this.setController("scan/add");
        this.setParams("customer_id", String.valueOf(member_id));
        this.setParams("amount", String.valueOf(amount));


        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {

            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            if (this.responseObj.status == true) {
                Log.v("check", "true");

                return true;
            } else {
                Log.v("check", "false");
                // System.out.print(resp);
                return false;


            }


        }
        catch (Exception ex)
        {


        }


        return false;

        }



}
