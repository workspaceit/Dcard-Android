package com.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.com.workspace.activity.DiscountsNearMeActivity;

import com.com.workspace.activity.ManageEmployeeActivity;
import com.model.dcard.R;
import com.model.model.Store;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 8/28/2015.
 */
public class StoreListAdapter extends BaseAdapter {

    private DiscountsNearMeActivity context;
    private LayoutInflater inflater;
    private int state;

    public StoreListAdapter(DiscountsNearMeActivity context) {
        this.context = context;
        this.inflater = context.getLayoutInflater();

    }

    @Override
    public int getCount() {
        return DiscountsNearMeActivity.storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {

        public TextView storeName;
        public TextView storeAddress;
        public ImageView storeImage;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.store_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.storeImage = (ImageView) convertView.findViewById(R.id.storeImage);
            viewHolder.storeAddress = (TextView) convertView.findViewById(R.id.storeaddress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.storeName.setText(DiscountsNearMeActivity.storeList.get(position).store_name);
        viewHolder.storeAddress.setText(DiscountsNearMeActivity.storeList.get(position).store_city+" - "+DiscountsNearMeActivity.storeList.get(position).store_zip +" , "+DiscountsNearMeActivity.storeList.get(position).store_state+" , "+DiscountsNearMeActivity.storeList.get(position).store_country);
        if(DiscountsNearMeActivity.storeList.get(position).participator == true)
           viewHolder.storeImage.setImageResource(R.drawable.logo);
//        else
//            viewHolder.storeImage.setImageResource(R.drawable.ic_logo);

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

