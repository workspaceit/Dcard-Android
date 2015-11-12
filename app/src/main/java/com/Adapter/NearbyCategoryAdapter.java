package com.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Utility.Utility;
import com.com.workspace.activity.DiscountsNearMeActivity;
import com.com.workspace.activity.ManageEmployeeActivity;
import com.model.dcard.R;
import com.model.model.Category;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 9/3/2015.
 */
public class NearbyCategoryAdapter  extends BaseAdapter
{

    private DiscountsNearMeActivity context;
    private ArrayList<Category> cArrayList;
    private LayoutInflater inflater;



    public NearbyCategoryAdapter(DiscountsNearMeActivity context,ArrayList<Category> cArrayList)
    {
        this.context = context;
        this.cArrayList = cArrayList;
        this.inflater = context.getLayoutInflater();



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

    public class ViewHolder {

        public TextView categoryName;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_item, null);
            viewHolder = new ViewHolder();

            viewHolder.categoryName = (TextView) convertView.findViewById(R.id.categoryName);


            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }


        viewHolder.categoryName.setTag(position);
        viewHolder.categoryName.setText(this.cArrayList.get(position).name);


        convertView.setBackgroundColor(Color.CYAN);

        if(Utility.categoryCheckList.get(position) == true)
        {
            System.out.println("test 3");
            viewHolder.categoryName.setBackgroundColor(Color.parseColor("#D6D6D6"));
            notifyDataSetChanged();
        }
        else
        {
            viewHolder.categoryName.setBackgroundColor(Color.parseColor("#FFFFFF"));
            notifyDataSetChanged();
        }


        return convertView;


    }
}
