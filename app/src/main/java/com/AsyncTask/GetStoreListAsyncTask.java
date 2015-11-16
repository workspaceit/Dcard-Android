package com.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.com.workspace.activity.DiscountsNearMeActivity;
import com.com.workspace.activity.ManageEmployeeActivity;
import com.model.model.Member;
import com.model.model.Search;
import com.model.model.Store;
import com.service.BusinessManagerService;
import com.service.StoreService;

import java.util.ArrayList;

/**
 * Created by Fayme Shahriar on 9/3/2015.
 */
public class GetStoreListAsyncTask extends AsyncTask<String, String, Boolean> {

    ProgressDialog dialog;
    private ArrayList<Store> storeArrayList;
    private DiscountsNearMeActivity context;
    private boolean result;
    private Search search;

    public GetStoreListAsyncTask(DiscountsNearMeActivity context,Search search) {

        this.context = context;
        this.search = search;

    }

    @Override
    public void onPreExecute() {

        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("loading...");
        dialog.setCancelable(true);
        dialog.show();

        super.onPreExecute();

    }

    @Override
    public Boolean doInBackground(String... parms){
        //result=false;
        StoreService service = new StoreService();
        this.storeArrayList = service.getStoreList(search);

        if(this.storeArrayList.size()>0)
            result = true;
        else
            result = false;

        return result;
    }

    @Override
    public void  onPostExecute(Boolean result){

        dialog.dismiss();
        if(result==true)
            this.context.onDataLoad(this.storeArrayList);
        else {
            this.context.changeAdapterViews();
            Toast.makeText(context, "No store found!!", Toast.LENGTH_LONG).show();
        }

    }


}
