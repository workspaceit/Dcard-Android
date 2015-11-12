package com.com.workspace.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AsyncTask.GetPreferencesAsyncTask;
import com.AsyncTask.GetStoreDetailsAsyncTask;
import com.Utility.ConnectionDetector;
import com.Utility.Utility;
import com.model.dcard.R;
import com.model.model.Scan;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.Format;
import java.util.logging.Formatter;

/**
 * Created by Fayme Shahriar on 8/26/2015.
 */
public class DetailsActivity extends Activity implements View.OnClickListener{

    private ImageButton back,navigate;
    private int position;
    private TextView storeTitle,storeCategory,spenditure,totalAmount,totalTransactions,totalSavings,phone;
    private ImageView mapImage;
    private ConnectionDetector cd;
    private Scan scan;

    double latitudeDestination = 0.0; // or some other location
    double longitudeDestination = 0.0; // or some other location
    String requestedMode = "car"; // or bike or car
    String mode = "";
    private LinearLayout layout2,layout3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.store_details_layout);
        initialize();

    }

    private void initialize() {

        if(requestedMode.equals("walking")) {
            mode = "&mode=w";
        } else if(requestedMode.equals("bike")) {
            mode = "&mode=b";
        } else if(requestedMode.equals("car")) {
            mode = "&mode=c";
        }


        this.back = (ImageButton)findViewById(R.id.back);
        this.back.setOnClickListener(this);
        this.navigate = (ImageButton)findViewById(R.id.navigate);
        this.navigate.setOnClickListener(this);
        this.scan = new Scan();
        this.cd = new ConnectionDetector(this);
        this.mapImage = (ImageView)findViewById(R.id.profileImg);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this) .build();
        ImageLoader.getInstance().init(config);
        this.totalTransactions = (TextView)findViewById(R.id.totalTransactions);
        this.totalAmount = (TextView)findViewById(R.id.totalAmounts);
        this.totalSavings = (TextView) findViewById(R.id.totalSavings);
        this.phone = (TextView) findViewById(R.id.storePhone);

        this.layout2 = (LinearLayout)findViewById(R.id.layout2);
        this.layout3 = (LinearLayout)findViewById(R.id.layout3);

        Intent intent = getIntent();
        this.position = intent.getIntExtra("position",-1);

        latitudeDestination = DiscountsNearMeActivity.storeList.get(this.position).lat;
        longitudeDestination = DiscountsNearMeActivity.storeList.get(this.position).lon;

        if(this.position!=-1)
        {
            setValue();

        }

        if(cd.isConnectingToInternet() && DiscountsNearMeActivity.storeList.get(position).participator==true)
        {
            System.out.println("position in details: "+this.position);
            System.out.println("item id in details: "+DiscountsNearMeActivity.storeList.get(position).store_id);
            ImageLoader.getInstance().displayImage(Utility.getMapImageUrl(DiscountsNearMeActivity.storeList.get(position).lat,DiscountsNearMeActivity.storeList.get(position).lon),mapImage);

            new GetStoreDetailsAsyncTask(this,DiscountsNearMeActivity.storeList.get(position).store_id).execute();

        }
        else
        {
            this.layout3.setVisibility(View.GONE);
            this.layout2.setVisibility(View.GONE);

        }





    }

    @Override
    public void onClick(View v) {

        if(v==back)
        {
            finish();

        }
        else if(v==navigate)
        {
//            Intent intent = new Intent(this,MapsActivity.class);
//            intent.putExtra("position",this.position);
//            startActivity(intent);

            Uri gmmIntentUri = Uri.parse(String.format("google.navigation:ll=%s,%s%s", latitudeDestination, longitudeDestination, mode));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }

        }


    }

    public void setValue()
    {
        this.storeTitle = (TextView)findViewById(R.id.storeTitle);
        this.storeTitle.setText(DiscountsNearMeActivity.storeList.get(this.position).store_name);

        this.storeCategory = (TextView)findViewById(R.id.storeCategory);
        if(DiscountsNearMeActivity.storeList.get(this.position).category.name=="")
        {
            this.storeCategory.setVisibility(View.GONE);
        }
        else
        this.storeCategory.setText("Category: "+DiscountsNearMeActivity.storeList.get(this.position).category.name);

        this.spenditure = (TextView)findViewById(R.id.spenditure);
        this.spenditure.setText("For every $"+DiscountsNearMeActivity.storeList.get(this.position).on_spent + " you spend you get $"+DiscountsNearMeActivity.storeList.get(this.position).amount_off + " free");

        this.phone.setText(DiscountsNearMeActivity.storeList.get(this.position).phone);



    }

    public void onDataLoad(Scan scan) {
        this.scan = scan;

        try {

            this.layout3.setVisibility(View.VISIBLE);
            this.layout2.setVisibility(View.VISIBLE);

            this.totalTransactions.setText("Your total transactions at " + DiscountsNearMeActivity.storeList.get(position).store_name + " store so far: $" + this.scan.cumulative_count);
            this.totalAmount.setText("Total amount spent so far: $" + scan.cumulative_paid);
            this.totalSavings.setText("Total savings: $" + this.scan.cumulative_saving);

        }catch (Exception ex)
        {


        }

    }







}
