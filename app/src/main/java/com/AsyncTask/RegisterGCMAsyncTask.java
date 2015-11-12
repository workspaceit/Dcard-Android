package com.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.service.LoginService;

/**
 * Created by Fayme Shahriar on 9/9/2015.
 */
public class RegisterGCMAsyncTask extends AsyncTask<String, String, Boolean> {

    ProgressDialog dialog;
    boolean result;
    Context context;
    String id;

    public RegisterGCMAsyncTask(Context context,String id)
    {
        this.context = context;
        this.id = id;


    }


    @Override
    public void onPreExecute(){
        //Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();



//        dialog = new ProgressDialog(context);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("Login...");
//        //dialog.setCancelable(false);
//        dialog.show();

        super.onPreExecute();



    }
    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        LoginService log = new LoginService();
        result=log.registerGCM(id);




        return result;
    }
    @Override
    public void  onPostExecute(Boolean result){

        //dialog.dismiss();

        //this.context.onDataLoad();




    }
}
