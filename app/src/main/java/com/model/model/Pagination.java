package com.model.model;

/**
 * Created by Fayme Shahriar on 9/2/2015.
 */
public class Pagination {

    public int per_page;
    public int last_page;
    public int current_page;
    public String next_page_url;
    public String prev_page_url;


    public Pagination()
    {

        this.per_page = 0;
        this.last_page = 0;
        this.current_page = 0;
        this.next_page_url = "";
        this.prev_page_url = "";

    }

}
