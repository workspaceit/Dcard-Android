package com.model.model;

/**
 * Created by Fayme Shahriar on 9/7/2015.
 */
public class ScanResult {

    public Member member;
    public ResponseStatus rObj;

    public ScanResult()
    {

        this.member = new Member();
        this.rObj = new ResponseStatus();

    }

}
