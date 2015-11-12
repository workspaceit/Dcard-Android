package com.service;

import android.util.Log;

import com.Utility.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.model.Member;
import com.model.model.Pagination;
import com.model.model.ResponseStatus;
import com.model.model.Scan;
import com.model.model.Search;
import com.model.model.Store;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 9/3/2015.
 */
public class StoreService extends DcardService {

    private Store store;
    private ArrayList<Store> storeArrayList;
    private ResponseStatus responseObj;
    private Pagination pg;
    private Search search;

    public ArrayList<Store> getStoreList(Search search) {
        this.storeArrayList = new ArrayList<Store>();
        this.search = search;
        this.responseObj = new ResponseStatus();
        this.setController("search/store");
        this.setParams("offset", String.valueOf(this.search.offset));
        this.setParams("category_id", String.valueOf(this.search.category_id_list));



        this.setParams("location", this.search.location);
        this.setParams("limit", String.valueOf(this.search.limit));
        this.setParams("distance", String.valueOf(this.search.distance));
        this.setParams("local_store", String.valueOf(this.search.local_store));
        this.setParams("lat", String.valueOf(this.search.lat));
        this.setParams("lon", String.valueOf(this.search.lon));

        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();


            Gson gSonObj = new Gson();
            Utility.total = jObj.get("total").getAsInt();
            Utility.current_number += 8;
            Utility.page_number += 1;
            System.out.println("total: " + Utility.total);
            System.out.println("current: " + Utility.current_number);
            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            //System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status == true) {
                Log.v("check", "true");


                Store[] store = gSonObj.fromJson(jObj.get("store"), Store[].class);
                //System.out.println(member);

                for (Store s : store) {
                    this.storeArrayList.add(s);
                }
                System.out.println("list size: " + this.storeArrayList.size());
                return this.storeArrayList;
            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return this.storeArrayList;
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }


        return this.storeArrayList;
    }

    public Scan getStoreDetail(int storeId) {

        Scan scan = new Scan();

        this.responseObj = new ResponseStatus();
        this.setController("store/details/"+storeId);

        String resp = this.getData("GET");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();


            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            //System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status == true) {
                Log.v("check", "true");

                scan =  gSonObj.fromJson(jObj.get("responseStatus"), scan.getClass());

                return scan;
            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return scan;
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }


        return scan;
    }

}
