package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.com.workspace.activity.LoginActivity;
import com.model.model.User;
import com.service.LoginService;

/**
 * Created by Fayme Shahriar on 8/28/2015.
 */
public class LoginAsyncTask extends AsyncTask<String, String, Boolean> {


    ProgressDialog dialog;
    boolean result;
    User user;
    LoginActivity context;



    public LoginAsyncTask(LoginActivity context,User user)
    {
        this.context=context;
        this.user = user;

    }


    @Override
    public void onPreExecute(){
        //Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();



        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Login...");
        //dialog.setCancelable(false);
        dialog.show();

        super.onPreExecute();



    }
    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        LoginService log = new LoginService();
        result=log.authintication(this.user);




        return result;
    }
    @Override
    public void  onPostExecute(Boolean result){

        dialog.dismiss();
        if(result == true)
        this.context.onDataLoad();
        else
            Toast.makeText(context,"Login failed",Toast.LENGTH_LONG).show();




    }


}
