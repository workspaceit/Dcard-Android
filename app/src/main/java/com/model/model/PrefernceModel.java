package com.model.model;

/**
 * Created by Fayme Shahriar on 8/31/2015.
 */
public class PrefernceModel {

    public Member meber;
    public double savings;
    public boolean state;

    public PrefernceModel(Member member,double savings, boolean state)
    {

        this.meber = member;
        this.savings=savings;
        this.state = state;

    }
}
