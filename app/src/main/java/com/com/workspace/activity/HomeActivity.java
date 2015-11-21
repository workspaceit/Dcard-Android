package com.com.workspace.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AsyncTask.LogOutAsyncTask;
import com.AsyncTask.MerchantAsyncTask;
import com.AsyncTask.RegisterGCMAsyncTask;
import com.Utility.ConnectionDetector;
import com.Utility.SessionManager;
import com.Utility.Utility;
import com.google.android.gcm.GCMRegistrar;
import com.model.dcard.R;

/**
 * Created by Fayme Shahriar on 8/25/2015.
 */
public class HomeActivity extends Activity implements View.OnClickListener {

    private Button preferences,bManager,myDcard,discounts,scanCard,logOut;
    private TextView username,position;
    private Button submit;
    private EditText mCode;
    private LinearLayout layout;
    private ConnectionDetector cd;
    private SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_layout);
        initialize();

    }

    private void initialize()
    {
        this.preferences = (Button)findViewById(R.id.preferences);
        this.preferences.setOnClickListener(this);
        this.username = (TextView) findViewById(R.id.username);
        this.position = (TextView) findViewById(R.id.position);
        this.bManager = (Button) findViewById(R.id.bManager);
        this.scanCard = (Button) findViewById(R.id.scanCard);

        this.logOut = (Button) findViewById(R.id.logout);
        this.logOut.setOnClickListener(this);

        this.mCode = (EditText)findViewById(R.id.merchantId);
        this.submit = (Button)findViewById(R.id.submit);
        this.submit.setOnClickListener(this);

        this.myDcard = (Button) findViewById(R.id.myDcard);
        this.myDcard.setOnClickListener(this);

        this.scanCard = (Button)findViewById(R.id.scanCard);




        this.discounts = (Button) findViewById(R.id.discounts);
        this.discounts.setOnClickListener(this);
        this.layout = (LinearLayout)findViewById(R.id.layout3);

        this.username.setText(Utility.member.first_name + " "+ Utility.member.last_name);
        System.out.println(Utility.member.user_type);
        if(Utility.member.user_type==1)
        {
            this.bManager.setOnClickListener(this);
            this.scanCard.setOnClickListener(this);
            position.setVisibility(View.VISIBLE);
            try {
                position.setText("Executive," + Utility.store.store_name);

            }catch (Exception ex)
            {


            }

           // layout.setVisibility(View.GONE);
            mCode.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);

        }
        else if(Utility.member.user_type==2)
        {
            //this.bManager.setOnClickListener(this);
            this.scanCard.setOnClickListener(this);
            position.setVisibility(View.VISIBLE);
            try {
                position.setText("Employee-" + Utility.store.store_name);
            }catch (Exception ex)
            {

            }
        }
        else
        {
            this.bManager.setBackground(getResources().getDrawable(R.drawable.custom_not_clickable));
            this.scanCard.setBackground(getResources().getDrawable(R.drawable.custom_not_clickable));
        }

        this.cd = new ConnectionDetector(this);
        this.sm = new SessionManager(this);


        final String regId = GCMRegistrar.getRegistrationId(this);
        System.out.println("already Registered: "+GCMRegistrar.getRegistrationId(this));

        if(cd.isConnectingToInternet())
        {

            if(regId.equals(""))
            {

                GCMRegistrar.register(this, CommonUtilities.SENDER_ID);


            }
            else
            {
                //new RegisterGCMAsyncTask(this,regId).execute();

            }



        }


    }

    @Override
    public void onClick(View v) {

        if(v==preferences)
        {
            Intent intent = new Intent(HomeActivity.this,PreferencesActivity.class);
            startActivity(intent);


        }
        else if(v==bManager)
        {
            Intent intent = new Intent(HomeActivity.this,BusinessManagerActivity.class);
            startActivity(intent);

        }
        else if(v==myDcard)
        {
            Intent intent = new Intent(HomeActivity.this,CreateBarCode.class);
            startActivity(intent);

        }
        else if(v==discounts)
        {
            Intent intent = new Intent(HomeActivity.this,DiscountsNearMeActivity.class);
            startActivity(intent);

        }

        else if(v==submit)
        {
            if(mCode.getText().length()>0)
            {
                if(this.cd.isConnectingToInternet())
                    new MerchantAsyncTask(this,mCode.getText().toString()).execute();
                else
                    Toast.makeText(this,"Failed to send..check ur connection",Toast.LENGTH_SHORT).show();

            }


        }
        else if(v==scanCard)
        {
            Intent intent = new Intent(HomeActivity.this,ScanBarcodeActivity.class);
            startActivity(intent);

        }
        else if(v==logOut)
        {
           new LogOutAsyncTask(HomeActivity.this).execute();
        }

    }

    public void onDataLoad() {

    Intent intent = new Intent(this,HomeActivity.class);
    startActivity(intent);
    finish();
    }

    public void logOutUser()
    {
        this.sm.logoutUser();
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }






}
