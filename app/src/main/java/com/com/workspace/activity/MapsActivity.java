package com.com.workspace.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.Utility.ConnectionDetector;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.model.dcard.R;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationChangeListener,View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager lm = null;
    private Polyline line=null;

    boolean gps_enabled = false,network_enabled = false;

    private Marker mMarker,mNewMarker;

    private Circle mCircle;

    private ConnectionDetector cd;
    private double lat,lng;
    private int position;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_layout);
        this.back = (ImageButton)findViewById(R.id.back);
        this.back.setOnClickListener(this);
        Intent intent = getIntent();
        this.position = intent.getIntExtra("position",-1);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                if(getLocationService()== true)
                setUpMap();

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationChangeListener(this);


        if(this.position != -1) {
            lat = DiscountsNearMeActivity.storeList.get(position).lat;
            lng = DiscountsNearMeActivity.storeList.get(position).lon;
        }


//        LatLng latLng;
//        latLng = new LatLng(lat,lng);
//
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
//        drawMarkerWithCircle(latLng);




    }

    private boolean getLocationService()
    {
        if(lm==null)
            lm = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){}
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){}

        if(!gps_enabled && !network_enabled){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
            dialog.setMessage("Not enabled");
            dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MapsActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();

            return false;
        }

        else
        {
            return true;

        }

    }



    private void drawMarkerWithCircle(LatLng position){
        double radiusInMeters = 200.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mMap.addCircle(circleOptions);
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        MarkerOptions markerOptions = new MarkerOptions().position(position).title(DiscountsNearMeActivity.storeList.get(this.position).store_name);
        mMap.addMarker(markerOptions);



    }

    @Override
    public void onClick(View v) {

        if(v==back)
        {
            finish();

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
        System.out.println();

        LatLng loc=new LatLng(40.7141667, -74.0063889);
        if(mNewMarker!=null)
        {
            mNewMarker.remove();
        }



        MarkerOptions markerOptions = new MarkerOptions().position(loc).title("Current position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mNewMarker = mMap.addMarker(markerOptions);




       // myCoordinate=new LatLng(location.getLatitude(), location.getLongitude());

        Log.d("Lat", String.valueOf(location.getLatitude()));
        Log.d("Lat",String.valueOf(location.getLongitude()));



    }
}
