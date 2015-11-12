package com.com.workspace.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.AsyncTask.GetPreferencesAsyncTask;
import com.AsyncTask.UpdatePreferencesAsyncTask;
import com.Utility.ConnectionDetector;
import com.Utility.Utility;
import com.model.dcard.R;
import com.model.model.PrefernceModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Fayme Shahriar on 8/26/2015.
 */
public class PreferencesActivity extends Activity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener {

    private ImageButton back,save;
    private ImageView userImg;
    private Switch emailEnable,zipcode,currentLocation;
    private boolean email_preference;
    private String search_preference;
    private int zip;
    private PrefernceModel pf;
    private EditText zipNumber;
    private ConnectionDetector cd;
    private TextView savings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preferences_layout);
        initialize();

    }

    private void initialize()
    {
        this.save = (ImageButton)findViewById(R.id.save);
        this.save.setOnClickListener(this);

        this.back = (ImageButton)findViewById(R.id.back);
        this.back.setOnClickListener(this);

        this.userImg = (ImageView)findViewById(R.id.userImg);
        this.email_preference = false;
        this.search_preference = "GPS";

        this.currentLocation = (Switch)findViewById(R.id.currentLocation);
        this.currentLocation.setChecked(true);
        this.currentLocation.setOnCheckedChangeListener(this);
        this.emailEnable = (Switch)findViewById(R.id.enableEmail);
        this.emailEnable.setOnCheckedChangeListener(this);
        this.zipcode = (Switch)findViewById(R.id.zipcode);
        this.zipcode.setOnCheckedChangeListener(this);
        this.zipNumber = (EditText)findViewById(R.id.zipNumber);
        this.zipNumber.setText("");
        this.savings = (TextView) findViewById(R.id.savings);
        this.cd = new ConnectionDetector(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .build();
        ImageLoader.getInstance().init(config);
        System.out.println(Utility.fbPicLink);

        ImageLoader.getInstance().displayImage(Utility.fbPicLink,userImg);
        if(cd.isConnectingToInternet())
        {

            new GetPreferencesAsyncTask(this).execute();


        }

    }

    @Override
    public void onClick(View v) {

        if(v==back)
        {
            this.finish();

        }
        else if(v==save)
        {
            if(search_preference=="zipsearch") {
                if (zipNumber.getText().length() > 0) {
                    if (cd.isConnectingToInternet()) {
                        new UpdatePreferencesAsyncTask(this, email_preference, search_preference, zipNumber.getText().toString()).execute();

                    }

                } else {
                    zipNumber.setHintTextColor(Color.RED);

                }

            }
            else
            {
                if (cd.isConnectingToInternet()) {
                    new UpdatePreferencesAsyncTask(this, email_preference, search_preference, zipNumber.getText().toString()).execute();

                }

            }


        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView==emailEnable)
        {
            if(emailEnable.isChecked())
            {
                this.email_preference = true;

            }
            else
            {
                this.email_preference = false;
            }

        }
        else if(buttonView == zipcode)
        {
            if(zipcode.isChecked()==true)
            {
                zipNumber.setVisibility(View.VISIBLE);
                zipNumber.setText("");
                search_preference = "zipsearch";
                currentLocation.setChecked(false);

            }
            else
            {

                zipNumber.setVisibility(View.GONE);
                zipNumber.setText("");
                search_preference = "GPS";
                // Check if no view has focus:
//                View view = this.getCurrentFocus();
//                if (view != null) {
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
            }


        }
        else if(buttonView == currentLocation)
        {
            if(currentLocation.isChecked())
            {
                zipNumber.setVisibility(View.GONE);
                zipcode.setChecked(false);
                search_preference = "GPS";

            }
            else
            {
                search_preference = "GPS";


            }


        }




    }

    public void onDataLoad(PrefernceModel pf) {

        this.pf = pf;
        String temp = this.pf.meber.search_flag;
        System.out.println(temp);

        if(this.pf.state==true) {

            if (this.pf.meber.email_flag==1) {
                this.emailEnable.setChecked(true);
                this.email_preference = true;
            }
            if (temp.trim().equals("GPS")) {
                this.currentLocation.setChecked(true);
                this.zipcode.setChecked(false);
                this.search_preference = "GPS";

            } else if (temp.trim().equals("")) {
                this.currentLocation.setChecked(false);
                this.zipcode.setChecked(false);
                this.search_preference = "";


            } else if(temp.trim().equals("zipsearch")) {
                this.zipcode.setChecked(true);
                this.currentLocation.setChecked(false);
                this.search_preference = "zipsearch";
                this.zipNumber.setText(this.pf.meber.zip_code);
                this.zipNumber.setVisibility(View.VISIBLE);

            }


            this.savings.setText("$" + String.valueOf(this.pf.savings));

        }
        else
        {
            System.out.println("it is false");

        }


    }
}
