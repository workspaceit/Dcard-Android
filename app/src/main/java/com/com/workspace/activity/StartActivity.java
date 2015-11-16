package com.com.workspace.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.Utility.ConnectionDetector;
import com.Utility.SessionManager;
import com.Utility.Utility;
import com.model.dcard.R;
import com.service.LoginService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Fayme Shahriar on 8/28/2015.
 */
public class StartActivity extends Activity {


    SessionManager session;
    ConnectionDetector cd;
    int DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        try {
//            PackageInfo info = getPackageManager().getPackageInfo( "com.com.workspace.activity",PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }




        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_layout);






        cd=new ConnectionDetector(StartActivity.this);
        session=new SessionManager(StartActivity.this);






        if (session.checkLogin() == true) {

            if(cd.isConnectingToInternet()==true)
                new MyTask(StartActivity.this,session.getAccessToken()).execute();

            else
                Toast.makeText(StartActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
        else {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, DELAY);

        }

    }

    public void onDataLoad()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(StartActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();

            }


        }, DELAY);
    }

    public class MyTask extends AsyncTask<String, String, Boolean> {

        ProgressDialog dialog;
        boolean result;
        Context mycontext;
        String token = "";

        public MyTask(Context c,String token) {
            this.mycontext = c;
            this.token = token;

        }

        @Override
        public void onPreExecute() {
            // Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(mycontext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Login...");
            // dialog.setCancelable(false);
            //dialog.show();

            super.onPreExecute();

        }

        @Override
        public Boolean doInBackground(String... parms) {
            // result=false;

            LoginService login = new LoginService();

            result = login.accessTokenLogin(token);

            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {

            //dialog.dismiss();

            if (result == true) {


                onDataLoad();

            } else {

                Toast.makeText(StartActivity.this, "Something went wrong,check again", Toast.LENGTH_LONG).show();

            }

        }

    }





}
