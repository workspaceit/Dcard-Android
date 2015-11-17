package com.service;

import android.util.Log;
import android.widget.ImageButton;

import com.Utility.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.journeyapps.barcodescanner.Util;
import com.model.model.Category;
import com.model.model.LoggedInMember;
import com.model.model.Member;
import com.model.model.Pagination;
import com.model.model.ResponseStatus;
import com.model.model.Store;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 9/1/2015.
 */
public class BusinessManagerService extends DcardService {

    ArrayList<Member> mArrayList;
    private Member member;
    ArrayList<Category>cArrayList;
    private Store store;
    private ResponseStatus responseObj;
    private Pagination pg;




    public ArrayList<Member> getEmployeeList(int page)
    {

        this.mArrayList = new ArrayList<Member>();
        this.pg = new Pagination();
        this.responseObj = new ResponseStatus();
        this.setController("employee/get?page="+page);



        String resp = this.getData("GET");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            JsonObject jpObj = jObj.get("member").getAsJsonObject();
            System.out.println("jpobj: " + jpObj);
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            //System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status==true) {
                Log.v("check", "true");

                this.pg.current_page = jpObj.get("current_page").getAsInt();
                System.out.println(this.pg.current_page);
                this.pg.last_page = jpObj.get("last_page").getAsInt();
                this.pg.per_page = jpObj.get("per_page").getAsInt();
//                if(jpObj.get("prev_page_url").getAsString()==null)
//                {
//                    this.pg.prev_page_url = "";
//                }
//                else
//                {
//                    this.pg.prev_page_url = jpObj.get("prev_page_url").getAsString();
//                }
//
//                if(jpObj.get("prev_page_url").getAsString()==null)
//                {
//
//                    this.pg.next_page_url = "";
//                }
//                else
//                {
//                    this.pg.next_page_url = jpObj.get("next_page_url").getAsString();
//
//                }

                Utility.pg = this.pg;

                Member[] member = gSonObj.fromJson(jpObj.get("data"),Member[].class);

                System.out.println(member.length);
                for (Member m : member) {
                   this.mArrayList.add(m);
                }
                System.out.println("size array: "+ mArrayList.size());
                return mArrayList;
            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return mArrayList;
            }
        } catch (Exception e) {
            // TODO: handle exception
          // System.out.println(e.getMessage());
        }




        return mArrayList;




    }



    public ArrayList<Category> getCategoryList()
    {

        this.cArrayList = new ArrayList<Category>();

        this.responseObj = new ResponseStatus();
        this.setController("category");



        String resp = this.getData("GET");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status==true) {
                Log.v("check", "true");



                Category[] category = gSonObj.fromJson(jObj.get("category"),Category[].class);
                for (Category c : category) {
                    this.cArrayList.add(c);
                }

                return this.cArrayList;
            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return this.cArrayList;
            }
        } catch (Exception e) {
            // TODO: handle exception
           // System.out.println(e.getMessage());
        }




        return this.cArrayList;




    }

    public boolean updatePrice(double percent_off,double amount_off,double on_spent,int category_id)
    {
        System.out.println("category id: " + category_id);

        this.responseObj = new ResponseStatus();
        this.store = new Store();
        this.setController("store/update");
        this.setParams("percent_off",String.valueOf(percent_off));
        this.setParams("amount_off",String.valueOf(amount_off));
        this.setParams("on_spent",String.valueOf(on_spent));
        this.setParams("category_id", String.valueOf(category_id));


        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status==true) {

                this.store = gSonObj.fromJson(jObj.get("store"),store.getClass());
                Utility.store.amount_off = this.store.amount_off;
                Utility.store.on_spent = this.store.on_spent;
                Utility.store.percent_off = this.store.percent_off;

                Utility.store.category = this.store.category;
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



    public Member addEmployee(int member_code)
    {
        this.member = new Member();

        this.responseObj = new ResponseStatus();

        this.setController("employee/add");
        this.setParams("member_code",String.valueOf(member_code));


        String resp = this.getData("POST");
        Log.v("resp", resp);

        try {
            JsonObject jObj = new JsonParser().parse(resp).getAsJsonObject();
            Gson gSonObj = new Gson();

            this.responseObj = gSonObj.fromJson(jObj.get("responseStatus"), responseObj.getClass());
            System.out.println(jObj);

            // System.out.println(user.role.other_permission);
            if (this.responseObj.status==true) {

                this.member = gSonObj.fromJson(jObj.get("member"), member.getClass());
                return member;
            } else {
                Log.v("checkFBLogin", "false");
                // System.out.print(resp);
                return member;
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
        }




        return member;
    }



    public boolean deleteEmployee(int member_id) {

        this.responseObj = new ResponseStatus();
        this.setController("employee/delete");
        this.setParams("member_id", String.valueOf(member_id));



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


    public boolean deleteSelf()
    {
        this.responseObj = new ResponseStatus();
        this.setController("merchant/delete");

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
