package com.AsyncTask;

import android.os.AsyncTask;

import com.com.workspace.activity.DetailsActivity;
import com.model.model.Scan;
import com.service.StoreService;

/**
 * Created by Fayme Shahriar on 9/4/2015.
 */
public class GetStoreDetailsAsyncTask  extends AsyncTask<String, String, Boolean> {

    DetailsActivity context;
    private int storeId;
    private Scan scan;
    private boolean result;

    public GetStoreDetailsAsyncTask(DetailsActivity context,int storeId)
    {
        this.context = context;
        this.storeId = storeId;
        this.scan = new Scan();

    }


    @Override
    public void onPreExecute() {


        super.onPreExecute();


    }


    @Override
    public Boolean doInBackground(String... parms){
        //result=false;

        StoreService service = new StoreService();
        this.scan = service.getStoreDetail(storeId);

        try {

            if(this.scan!=null)
                result = true;
            else
                result = false;


        }catch (Exception ex)
        {

        }

        return result;
    }


    @Override
    public void  onPostExecute(Boolean result){

        //dialog.dismiss();

        this.context.onDataLoad(this.scan);




    }


}
