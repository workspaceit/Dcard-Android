package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.com.workspace.activity.HomeActivity;
import com.com.workspace.activity.LoginActivity;
import com.model.model.User;
import com.service.LoginService;
import com.service.MerchantService;

/**
 * Created by Fayme Shahriar on 9/1/2015.
 */
public class MerchantAsyncTask extends AsyncTask<String, String, Boolean> {


    ProgressDialog dialog;
    boolean result;
    String merchant_code;
    HomeActivity context;


    public MerchantAsyncTask(HomeActivity context,String merchant_code)
    {
        this.context=context;
        this.merchant_code = merchant_code;

    }


    @Override
    public void onPreExecute(){


        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Submitting...");
        //dialog.setCancelable(false);
        dialog.show();

        super.onPreExecute();



    }

    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        MerchantService service = new MerchantService();
        result=service.updateUserType(this.merchant_code);




        return result;
    }


    @Override
    public void  onPostExecute(Boolean result){

        dialog.dismiss();
        if(result==true)
        this.context.onDataLoad();
        else
        Toast.makeText(context,"Wrong Mercahnt code",Toast.LENGTH_SHORT).show();



    }


}
