package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.com.workspace.activity.BusinessManagerActivity;
import com.com.workspace.activity.PreferencesActivity;
import com.service.BusinessManagerService;
import com.service.PreferencesService;

/**
 * Created by Fayme Shahriar on 9/2/2015.
 */
public class UpdatePriceAsyncTask extends AsyncTask<String, String, Boolean> {


    ProgressDialog dialog;
    boolean result;
    BusinessManagerActivity context;
    double percent_off;
    double amount_off;
    double on_spent;
    int category_id;

    public UpdatePriceAsyncTask(BusinessManagerActivity context, double percent_off,double amount_off,double on_spent,int category_id)
    {

        this.context = context;
        this.percent_off = percent_off;
        this.amount_off = amount_off;
        this.on_spent = on_spent;
        this.category_id = category_id;


    }

    @Override
    public void onPreExecute(){
        //Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();



        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Saving...");
        //dialog.setCancelable(false);
        dialog.show();

        super.onPreExecute();



    }


    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        BusinessManagerService service = new BusinessManagerService();
        result=service.updatePrice(percent_off,amount_off,on_spent,category_id);


        return result;
    }

    @Override
    public void  onPostExecute(Boolean result){

        dialog.dismiss();

        if(result==false)
        {
            Toast.makeText(context, "Update cannot be done!!", Toast.LENGTH_SHORT).show();

        }
        else
        {

            context.onUpdate();

        }

    }


}
