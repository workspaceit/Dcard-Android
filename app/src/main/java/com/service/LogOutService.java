package com.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.model.ResponseStatus;

/**
 * Created by rajib on 11/16/15.
 */
public class LogOutService extends DcardService{

    private ResponseStatus responseStatus;

    public boolean logOutUser(){
        this.responseStatus = new ResponseStatus();
        this.setController("/logout");

        String resp = this.getData("GET");
        Log.v("Response:",resp);


        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseStatus = gSonObj.fromJson(jObj.get("responseStatus"), responseStatus.getClass());
            System.out.println(jObj);
            if (this.responseStatus.status==true)
            {
                return true;
            }
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

}
