package com.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.com.workspace.activity.BusinessManagerActivity;
import com.com.workspace.activity.DiscountsNearMeActivity;
import com.model.dcard.R;
import com.model.model.Category;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 9/2/2015.
 */
public class CategorySpinnerAdapter extends BaseAdapter {

    private BusinessManagerActivity context;

    private ArrayList<Category> cArrayList;

    public CategorySpinnerAdapter(BusinessManagerActivity context,ArrayList<Category> cArrayList)
    {
        this.context = context;
        this.cArrayList = cArrayList;


    }



    @Override
    public int getCount() {
        return cArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.spinner_item, null, true);

        TextView title = (TextView) rowView.findViewById(R.id.textItem);
        title.setText(cArrayList.get(position).name);


        return rowView;
    }
}
