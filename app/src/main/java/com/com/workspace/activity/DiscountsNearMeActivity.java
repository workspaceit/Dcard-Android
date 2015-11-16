package com.com.workspace.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Adapter.CategorySpinnerAdapter;
import com.Adapter.NearbyCategoryAdapter;
import com.Adapter.StoreListAdapter;
import com.AsyncTask.GetStoreListAsyncTask;
import com.Utility.ConnectionDetector;
import com.Utility.Utility;
import com.journeyapps.barcodescanner.Util;
import com.model.dcard.R;
import com.model.model.Category;
import com.model.model.Search;
import com.model.model.Store;
import com.service.BusinessManagerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fayme Shahriar on 9/3/2015.
 */
public class DiscountsNearMeActivity extends Activity implements View.OnClickListener, LocationListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public static ArrayList<Store> storeList;
    private ListView storeListView, categoryListView, distanceListView;
    public StoreListAdapter adapter;
    private ConnectionDetector cd;
    private ImageButton back;
    private ArrayList<Category> cArrayList;
    private ArrayList<String> dArrayList;
    private Button categoryButton, distanceButton, enrolled, confirm;
    private Search search;
    private Button searchButton;
    private int searchState, distanceState,localStoreState;
    private LocationManager locationManager;
    private String provider;
    private boolean userScrolled;
    private boolean checkClick;
    private NearbyCategoryAdapter nAdapter;
    private ArrayList<Integer> choosedCategories;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.storelist_layout);
        initialize();
    }

    private void initialize() {
        this.userScrolled = false;
        this.checkClick = false;
        this.storeList = new ArrayList<Store>();
        this.cArrayList = new ArrayList<Category>();
        this.dArrayList = new ArrayList<String>();

        this.confirm = (Button) findViewById(R.id.confirm);
        this.confirm.setOnClickListener(this);

        this.choosedCategories = new ArrayList<Integer>();

        this.distanceButton = (Button) findViewById(R.id.distance);
        this.distanceButton.setOnClickListener(this);
        this.distanceListView = (ListView) findViewById(R.id.distanceList);
        //this.distanceListView.setOnScrollListener(this);
        // this.distanceListView.setOnItemClickListener(this);

        generateDistance();
        this.categoryListView = (ListView) findViewById(R.id.categoryList);
        //this.categoryListView.setOnScrollListener(this);
        //this.categoryListView.setOnItemClickListener(this);


        this.storeListView = (ListView) findViewById(R.id.storesList);
        this.storeListView.setOnScrollListener(this);
        this.storeListView.setOnItemClickListener(this);

        this.adapter = new StoreListAdapter(this);
        this.storeListView.setAdapter(adapter);
        this.back = (ImageButton) findViewById(R.id.back);
        this.back.setOnClickListener(this);
        this.enrolled = (Button) findViewById(R.id.enrolledButton);
        this.enrolled.setOnClickListener(this);
        this.search = new Search();
        this.searchState = 0;
        this.distanceState = 0;
        this.localStoreState = 0;

        this.categoryButton = (Button) findViewById(R.id.categories);
        this.categoryButton.setOnClickListener(this);
        this.distanceButton = (Button) findViewById(R.id.distance);
        this.distanceButton.setOnClickListener(this);
        this.searchButton = (Button) findViewById(R.id.search);
        this.searchButton.setOnClickListener(this);

        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Location location = getLastKnownLocation();

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            // onLocationChanged(location);

            search.location = "new york";
            search.lat = 40.7141667;
            search.lon = -74.0063889;
        } else {
            System.out.println("location not found Lmao!!");
            search.location = "";
            search.lat = 0.0;
            search.lon = 0.0;
        }

        this.cd = new ConnectionDetector(this);

        if (cd.isConnectingToInternet())
            new CategoryTask(this).execute();

        if (cd.isConnectingToInternet())
            new GetStoreListAsyncTask(this, search).execute();


        this.distanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("lala lalalla");
                distanceButton.setText(dArrayList.get(position) + " miles");
                search.distance = calculateInMetre(Integer.valueOf(dArrayList.get(position)));
                DiscountsNearMeActivity.storeList.clear();
                if (cd.isConnectingToInternet())
                    new GetStoreListAsyncTask(DiscountsNearMeActivity.this, search).execute();

            }


        });

        this.categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("test 1");
                confirm.setVisibility(View.VISIBLE);
                if (Utility.categoryCheckList.get(position) == false) {

                    Utility.categoryCheckList.set(position, true);
                    choosedCategories.add(cArrayList.get(position).id);

                } else {
                    Utility.categoryCheckList.set(position, false);
                    if (choosedCategories.contains(cArrayList.get(position).id)) {
                        int index = choosedCategories.indexOf(cArrayList.get(position).id);
                        choosedCategories.remove(index);
                    }

                }
                nAdapter.notifyDataSetChanged();


                if (choosedCategories.size() > 1)
                    categoryButton.setText("Multiple Category selected");
                else {
                    categoryButton.setText("CATEGORIES");
                }

            }
        });


    }

    public void onDataLoad(ArrayList<Store> storeArrayList) {

        this.categoryListView.setVisibility(View.GONE);
        this.distanceListView.setVisibility(View.GONE);
        this.storeListView.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.GONE);

       System.out.print("storeArraListSize:"+storeArrayList.size());

            for (int i = 0; i < storeArrayList.size(); i++) {
                try {
                    DiscountsNearMeActivity.storeList.add(storeArrayList.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            System.out.println("here: " + DiscountsNearMeActivity.storeList.size());
            this.adapter.notifyDataSetChanged();

    }

    public void changeAdapterViews()
    {
        this.adapter.notifyDataSetChanged();
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(bestLocation.getLatitude(), bestLocation.getLongitude(), 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = builder.toString(); //This is the complete address.
            System.out.println(fnialAddress);
            //  search.location = fnialAddress;
            // search.lat = lat;
            // search.lon = lng;


        } catch (IOException e) {
        } catch (NullPointerException e) {
        }


        return bestLocation;
    }


    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = builder.toString(); //This is the complete address.
            System.out.println(fnialAddress);
            search.location = fnialAddress;
            search.lat = lat;
            search.lon = lng;
//            search.location = "new york";
//            search.lat = 40.7141667;
//            search.lon = -74.0063889;

        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    @Override
    public void onClick(View v) {
        if (v == back) {
            DiscountsNearMeActivity.storeList.clear();
            finish();

        } else if (v == searchButton) {
            if (searchState == 0) {
                searchState = 1;


                Drawable img = this.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_24dp);
                searchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            } else {
                searchState = 0;


                Drawable img = this.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp);
                searchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                DiscountsNearMeActivity.storeList.clear();
                if (cd.isConnectingToInternet())
                    new GetStoreListAsyncTask(this, search).execute();

            }

        } else if (v == categoryButton) {
            if (searchState == 0) {
                searchState = 1;
                distanceState = 0;
                storeListView.setVisibility(View.GONE);
                categoryListView.setVisibility(View.VISIBLE);
                distanceListView.setVisibility(View.GONE);
            } else if (searchState == 1) {
                searchState = 0;
                distanceState = 0;
                storeListView.setVisibility(View.VISIBLE);
                categoryListView.setVisibility(View.GONE);
                distanceListView.setVisibility(View.GONE);

            }

        } else if (v == distanceButton) {
            if (distanceState == 0) {
                confirm.setVisibility(View.GONE);
                distanceState = 1;
                searchState = 0;
                storeListView.setVisibility(View.GONE);
                categoryListView.setVisibility(View.GONE);
                distanceListView.setVisibility(View.VISIBLE);
            } else if (distanceState == 1) {
                distanceState = 0;
                searchState = 0;
                storeListView.setVisibility(View.VISIBLE);
                categoryListView.setVisibility(View.GONE);
                distanceListView.setVisibility(View.GONE);


            }

        } else if (v == confirm) {
            for (int i = 0; i < this.choosedCategories.size(); i++) {
                if (i == 0)
                    search.category_id_list = this.choosedCategories.get(i).toString();
                else
                    search.category_id_list = search.category_id_list + "," + this.choosedCategories.get(i).toString();

            }

            callSearch();
        }
        else if(v == enrolled)
        {
            if(localStoreState == 0) {

                search.local_store = 1;
                enrolled.setText("Show only Dcard Enrolled business");
                callSearch();

            }
            else if(localStoreState == 1)
            {
                search.local_store = 0;
                enrolled.setText("Show All Business");
                callSearch();
            }

        }

    }

    private void callSearch()
    {
        DiscountsNearMeActivity.storeList.clear();

        if (cd.isConnectingToInternet())
            new GetStoreListAsyncTask(DiscountsNearMeActivity.this, search).execute();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        System.out.println("onScrollStateChange: " + Utility.current_number);

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            userScrolled = true;
        }


    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int lastInScreen = firstVisibleItem + visibleItemCount;
        System.out.println("lastinscreen: " + lastInScreen);
        System.out.println("totalItemCount: " + totalItemCount);
        if (lastInScreen >= totalItemCount && userScrolled == true) {
            Log.d("TAG", "onScroll lastInScreen - so load more");
            System.out.println("getCalled");
            if (Utility.current_number < Utility.total) {
                if (cd.isConnectingToInternet() == true) {
                    search.offset = Utility.page_number * search.limit;
                    System.out.println(Utility.current_number);

                    new GetStoreListAsyncTask(this, search).execute();


                }
            }


        }

//        int topRowVerticalPosition = (this.storeListView == null || this.storeListView.getChildCount() == 0) ? 0 : storeListView.getChildAt(0).getTop();
//        sLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        System.out.println(position);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);


    }

    public class CategoryTask extends AsyncTask<String, String, Boolean> {

        ProgressDialog dialog;
        boolean result;
        Context mycontext;

        public CategoryTask(Context c) {
            this.mycontext = c;

        }

        @Override
        public void onPreExecute() {
            // Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(mycontext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("......");
            // dialog.setCancelable(false);
            //dialog.show();

            super.onPreExecute();

        }

        @Override
        public Boolean doInBackground(String... parms) {
            // result=false;

            BusinessManagerService bm = new BusinessManagerService();

            cArrayList = bm.getCategoryList();

            if (cArrayList.size() > 0)
                result = true;
            else
                result = false;


            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {

            dialog.dismiss();

            if (result == true) {

                onDataLoad();

            } else {

                Toast.makeText(DiscountsNearMeActivity.this, "Something went wrong,check again", Toast.LENGTH_LONG).show();

            }

        }

    }

    private void onDataLoad() {

        Collections.reverse(cArrayList);
        Utility.categoryCheckList.clear();
        Utility.setCheckList(cArrayList.size());
        this.nAdapter = new NearbyCategoryAdapter(this, cArrayList);
        this.categoryListView.setAdapter(nAdapter);


    }

    public void generateDistance() {
//      this.dArrayList.add("Select distance in miles");
        this.dArrayList.add("25");
        this.dArrayList.add("10");
        this.dArrayList.add("5");
        this.dArrayList.add("2");
        this.distanceListView.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_simple_item, dArrayList));


    }

    public double calculateInMetre(int a) {
        double value = 0.0;

        value = (a / 0.00062137);

        System.out.println(value);
        return value;
    }


}
