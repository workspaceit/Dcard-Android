package com.com.workspace.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.CategorySpinnerAdapter;
import com.AsyncTask.UpdatePriceAsyncTask;
import com.Utility.ConnectionDetector;
import com.Utility.Utility;
import com.model.dcard.R;
import com.model.model.Category;
import com.service.BusinessManagerService;
import com.service.LoginService;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Fayme Shahriar on 8/26/2015.
 */
public class BusinessManagerActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private ImageButton back,edit;
    private Button manageEmployee,save;
    private TextView title,address,category;
    private LinearLayout layout;
    private Spinner categorySpinner;
    private int state=0;
    private ArrayList<Category>cArrayList;
    private ConnectionDetector cd;
    private CheckBox checkbox1,checkbox2;
    private int category_id = 0;
    private double amount_off;
    private double on_spent;
    private double percent_off;
    private EditText spend,get,discount;
    private int checkbox_state=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.business_manager_layout);
        initialize();
    }

    private void initialize()
    {
        this.back = (ImageButton)findViewById(R.id.back);
        this.back.setOnClickListener(this);
        this.edit = (ImageButton)findViewById(R.id.edit);
        this.edit.setOnClickListener(this);
        this.save = (Button) findViewById(R.id.save);
        this.save.setOnClickListener(this);
        this.manageEmployee = (Button)findViewById(R.id.manageEmployee);
        this.manageEmployee.setOnClickListener(this);
        this.title = (TextView)findViewById(R.id.storeTitle);
        this.address = (TextView)findViewById(R.id.storeaddress);
        this.category = (TextView)findViewById(R.id.storeCategory);
        this.categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        this.categorySpinner.setOnItemSelectedListener(this);

        this.layout = (LinearLayout)findViewById(R.id.layout2);
        this.cd = new ConnectionDetector(this);

        this.checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        this.checkbox1.setOnClickListener(this);
        this.checkbox1.setChecked(true);
        this.checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        this.checkbox2.setOnClickListener(this);

        this.spend = (EditText)findViewById(R.id.spend);
        this.get = (EditText)findViewById(R.id.get);
        this.discount = (EditText)findViewById(R.id.discount);


        try {
            this.title.setText(Utility.store.store_name);
            this.address.setText(Utility.store.store_state+","+ Utility.store.store_city+","+ Utility.store.store_country);
            this.category.setText("Category: "+ Utility.store.category[0].name);

            this.spend.setText(String.valueOf(Utility.store.amount_off));
            this.get.setText(String.valueOf(Utility.store.on_spent));
            this.discount.setText(String.valueOf(Utility.store.percent_off));

            this.amount_off = Utility.store.amount_off;
            this.on_spent = Utility.store.on_spent;
            this.percent_off = Utility.store.percent_off;

        }catch (Exception ex)
        {

        }





        this.cArrayList = new ArrayList<Category>();
        if(cd.isConnectingToInternet())
        {
            new CategoryTask(this).execute();

        }
    }

    @Override
    public void onClick(View v) {

        if(v==back)
        {
            this.finish();
        }
        if(v==edit)
        {
            if(state==0) {
                this.layout.setVisibility(View.VISIBLE);
                this.categorySpinner.setVisibility(View.VISIBLE);
                this.category.setVisibility(View.GONE);
                state=1;
            }
            else
            {
                this.layout.setVisibility(View.GONE);
                this.categorySpinner.setVisibility(View.GONE);
                this.category.setVisibility(View.VISIBLE);
                state=0;

            }

        }
        else if(v==manageEmployee)
        {
            Intent intent = new Intent(this,ManageEmployeeActivity.class);
            startActivity(intent);


        }
        else if(v==save)
        {
            if(checkbox_state==0)
            {
                this.on_spent = Double.valueOf(this.spend.getText().toString());
                this.amount_off = Double.valueOf(this.get.getText().toString());
                this.percent_off = 0.0;

            }
            else
            {

                this.on_spent = 0.0;
                this.amount_off = 0.0;
                this.percent_off = Double.valueOf(this.discount.getText().toString());


            }

            if(category_id != -1) {
                if (cd.isConnectingToInternet())
                    new UpdatePriceAsyncTask(this, percent_off, amount_off, on_spent, category_id).execute();
                else
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();

            }

        }
        else if(v==checkbox1)
        {
            checkbox2.setChecked(false);
            checkbox_state=0;


        }
        else if(v==checkbox2)
        {
            checkbox1.setChecked(false);
            checkbox_state=1;

        }

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position!=0) {
            this.category_id = this.cArrayList.get(position).id;
            System.out.println(category_id);
        }
        else if(position==0)
        {
            this.category_id = -1;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onUpdate() {

        Toast.makeText(this,"Saved successfully!!",Toast.LENGTH_SHORT).show();
        try {
            this.category.setText("Category: " + Utility.store.category[0].name);
            this.spend.setText(String.valueOf(Utility.store.amount_off));
            this.get.setText(String.valueOf(Utility.store.on_spent));
            this.discount.setText(String.valueOf(Utility.store.percent_off));
        }
        catch (Exception ex)
        {

        }
    }


    public class CategoryTask extends AsyncTask<String, String, Boolean> {

        ProgressDialog dialog;
        boolean result;
        Context mycontext;


        public CategoryTask(Context c) {
            this.mycontext = c;


        }

        @Override
        public void onPreExecute() {
            // Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(mycontext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("......");
            // dialog.setCancelable(false);
            //dialog.show();

            super.onPreExecute();

        }

        @Override
        public Boolean doInBackground(String... parms) {
            // result=false;

            BusinessManagerService bm = new BusinessManagerService();

            cArrayList= bm.getCategoryList();

            if(cArrayList.size()>0)
               result = true;
            else
            result = false;


            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {

            //dialog.dismiss();

            if (result == true) {


                onDataLoad();

            } else {

                Toast.makeText(BusinessManagerActivity.this, "Something went wrong,check again", Toast.LENGTH_LONG).show();

            }

        }

    }

    private void onDataLoad() {


        Category c= new Category();
        c.id=0;
        c.name = "Click to select";
        cArrayList.add(c);
        Collections.reverse(cArrayList);
        this.categorySpinner.setAdapter(new CategorySpinnerAdapter(this,cArrayList));


    }


}
