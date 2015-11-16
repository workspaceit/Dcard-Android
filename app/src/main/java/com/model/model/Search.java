package com.model.model;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 9/3/2015.
 */
public class Search {

    public int offset;
    public String category_id_list;
    public String location;
    public int limit;
    public double distance;
    public int local_store;
    public double lat;
    public double lon;


    public Search()
    {

        this.offset = 0;
        this.location = "";
        this.limit = 8;
        this.distance = 0.0;
        this.local_store = 0;
        this.lat = 0.0;
        this.lon = 0.0;
        this.category_id_list = "";


    }

}
