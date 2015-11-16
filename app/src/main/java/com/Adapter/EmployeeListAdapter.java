package com.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.com.workspace.activity.ManageEmployeeActivity;
import com.model.dcard.R;
import com.model.model.Member;

import java.nio.charset.MalformedInputException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fayme Shahriar on 9/1/2015.
 */
public class EmployeeListAdapter extends BaseAdapter {


    private ManageEmployeeActivity context;
    private LayoutInflater inflater;
    private int state = 0;
    //new

    View.OnTouchListener mTouchListener;

    public EmployeeListAdapter(ManageEmployeeActivity context,View.OnTouchListener listener) {
        this.context = context;
        mTouchListener = listener;
        this.inflater = context.getLayoutInflater();




    }


    @Override
    public int getCount() {

        return ManageEmployeeActivity.mEmployeeList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        //new
        return ManageEmployeeActivity.mEmployeeList.get(position).id;
    }

    //new
    @Override
    public boolean hasStableIds() {
        return true;
    }


    public class ViewHolder {

        public TextView name;
        public TextView memberCode;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.employee_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.memberCode = (TextView) convertView.findViewById(R.id.memberCode);
            //new


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        convertView.setOnTouchListener(mTouchListener);
        viewHolder.name.setText(ManageEmployeeActivity.mEmployeeList.get(position).first_name + " " + ManageEmployeeActivity.mEmployeeList.get(position).last_name);
        viewHolder.memberCode.setText(ManageEmployeeActivity.mEmployeeList.get(position).member_code);


//        if (position % 3 == 0) {
//            convertView.setBackgroundColor(Color.parseColor("#C9A798"));
//        } else if (position % 2 == 0) {
//            convertView.setBackgroundColor(Color.parseColor("#E9E0DB"));
//        } else {
//            convertView.setBackgroundColor(Color.parseColor("#FFCBD3"));
//
//        }
        state += 1;
        return convertView;
    }
}
