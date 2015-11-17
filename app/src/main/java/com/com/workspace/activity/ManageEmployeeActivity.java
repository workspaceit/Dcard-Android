package com.com.workspace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.Adapter.EmployeeListAdapter;
import com.AsyncTask.GetEmployeeListAsyncTask;
import com.AsyncTask.GetStoreListAsyncTask;
import com.Utility.BackgroundContainer;
import com.Utility.ConnectionDetector;
import com.Utility.SessionManager;
import com.Utility.SwipeDetector;
import com.Utility.Utility;
import com.journeyapps.barcodescanner.Util;
import com.model.dcard.R;
import com.model.model.Member;
import com.service.BusinessManagerService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fayme Shahriar on 8/27/2015.
 */
public class ManageEmployeeActivity extends Activity implements View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener,View.OnTouchListener {

    private ListView manageEmployee;
    private EditText employeeIdEditText;
    private Button add,removeSelf;
    public static ArrayList<Member> mEmployeeList;
    private EmployeeListAdapter adapter;
    private ConnectionDetector cd;
    private Member member;
    private ImageButton back;
    private boolean userScrolled;
    private int swipedPosition = 0;
    private SessionManager sm;
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    final SwipeDetector swipeDetector = new SwipeDetector();


    BackgroundContainer mBackgroundContainer;
    boolean mSwiping = false;
    boolean mItemPressed = false;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;


    enum Direction {LEFT, RIGHT;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.manage_employee_layout);
        initialize();


    }




    private void initialize() {


        this.userScrolled = false;
        this.mEmployeeList = new ArrayList<Member>();
        this.manageEmployee = (ListView) findViewById(R.id.employeeList);
        this.manageEmployee.setOnScrollListener(this);

        this.sm = new SessionManager(ManageEmployeeActivity.this);
        this.add = (Button) findViewById(R.id.add);
        this.add.setOnClickListener(this);
        this.removeSelf = (Button)findViewById(R.id.removeSelf);
        this.removeSelf.setOnClickListener(this);

        this.adapter = new EmployeeListAdapter(this,mTouchListener);

        this.manageEmployee.setAdapter(adapter);
        this.back = (ImageButton) findViewById(R.id.back);
        this.back.setOnClickListener(this);
        this.employeeIdEditText = (EditText) findViewById(R.id.memberCode);

        //new
        mBackgroundContainer = (BackgroundContainer) findViewById(R.id.listViewBackground);


        this.cd = new ConnectionDetector(this);

        if (cd.isConnectingToInternet())
            new GetEmployeeListAsyncTask(this, 1).execute();


    }

    @Override
    public void onClick(View v) {

        if (v == add) {
            if (cd.isConnectingToInternet() && employeeIdEditText.getText().length()>0)
                new AddEmployee(this, Integer.valueOf(employeeIdEditText.getText().toString())).execute();
            else
                Toast.makeText(ManageEmployeeActivity.this, "No internet connection", Toast.LENGTH_LONG).show();

        } else if (v == back) {
            finish();


        }
        else if(v == removeSelf)
        {
            if (cd.isConnectingToInternet())
                new DeleteMerchant(this).execute();
            else
                Toast.makeText(ManageEmployeeActivity.this, "No internet connection", Toast.LENGTH_LONG).show();

        }
    }


    public void onDataLoad(ArrayList<Member> mEmployeeList) {

        for (int i = 0; i < mEmployeeList.size(); i++) {
            ManageEmployeeActivity.mEmployeeList.add(mEmployeeList.get(i));

        }
        System.out.println("here: " + ManageEmployeeActivity.mEmployeeList.size());
        this.adapter.notifyDataSetChanged();


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
            if (Utility.pg.current_page < Utility.pg.last_page) {

                if (cd.isConnectingToInternet() == true) {
                    Utility.pg.current_page += 1;
                    new GetEmployeeListAsyncTask(this, Utility.pg.current_page).execute();

                }
            }


        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        this.swipedPosition = position;
       // functionDeleteRowWhenSlidingLeft(view);

//        if (swipeDetector.swipeDetected()){
//            functionDeleteRowWhenSlidingLeft(view);
//        } else {
//            // do the onItemClick action
//        }
    }

    /**
     * Handle touch events to fade/move dragged items as they are swiped out
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(ManageEmployeeActivity.this).getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return false;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1);
                    v.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            manageEmployee.requestDisallowInterceptTouchEvent(true);
                            mBackgroundContainer.showBackground(v.getTop(), v.getHeight());
                        }
                    }
                    if (mSwiping) {
                        v.setTranslationX((x - mDownX));
                        v.setAlpha(1 - deltaXAbs / v.getWidth());
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                {
                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + v.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > v.getWidth() / 4) {
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / v.getWidth();
                            endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / v.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        // Animate position and alpha of swiped item
                        // NOTE: This is a simplified version of swipe behavior, for the
                        // purposes of this demo about animation. A real version should use
                        // velocity (via the VelocityTracker class) to send the item off or
                        // back at an appropriate speed.
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        manageEmployee.setEnabled(false);
                        v.animate().setDuration(duration).
                                alpha(endAlpha).translationX(endX).
                                withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Restore animated values
                                        v.setAlpha(1);
                                        v.setTranslationX(0);
                                        if (remove) {
                                            animateRemoval(manageEmployee, v);
                                        } else {
                                            mBackgroundContainer.hideBackground();
                                            mSwiping = false;
                                            manageEmployee.setEnabled(true);
                                        }
                                    }
                                });
                    }
                }
                mItemPressed = false;
                break;
                default:
                    return false;
            }
            return true;
        }
    };

    /**
     * This method animates all other views in the ListView container (not including ignoreView)
     * into their final positions. It is called after ignoreView has been removed from the
     * adapter, but before layout has been run. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private void animateRemoval(final ListView listview, View viewToRemove) {
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = adapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        swipedPosition = manageEmployee.getPositionForView(viewToRemove);

        functionDeleteEmployee(ManageEmployeeActivity.mEmployeeList.get(swipedPosition).id);

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        mBackgroundContainer.hideBackground();
                                        mSwiping = false;
                                        manageEmployee.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    mBackgroundContainer.hideBackground();
                                    mSwiping = false;
                                    manageEmployee.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                historicX = event.getX();
                historicY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                if (event.getX() - historicX < -DELTA)
                {

                    return true;
                }
                else if (event.getX() - historicX > DELTA)
                {
                    //FunctionDeleteRowWhenSlidingRight();
                    return false;
                } break;
            default: return false;
        }


        return false;
    }

    private void functionDeleteEmployee(final int member_id) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure??");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new DeleteEmployee(ManageEmployeeActivity.this,member_id).execute();
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();






    }





    public class AddEmployee extends AsyncTask<String, String, Boolean> {

        ProgressDialog dialog;
        boolean result;
        Context mycontext;
        int member_code;


        public AddEmployee(Context c, int member_code) {
            this.mycontext = c;
            this.member_code = member_code;

        }

        @Override
        public void onPreExecute() {
            // Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(mycontext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("updating...");
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();

        }

        @Override
        public Boolean doInBackground(String... parms) {
            // result=false;

            BusinessManagerService bm = new BusinessManagerService();

            member = bm.addEmployee(member_code);

            if (member.id == 0)
                result = false;
            else
                result = true;


            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {

            dialog.dismiss();

            if (result == true) {


                onEmployeeAdd();

            } else {

                Toast.makeText(ManageEmployeeActivity.this, "Something went wrong,check again", Toast.LENGTH_LONG).show();

            }

        }

    }


    public class DeleteEmployee extends AsyncTask<String, String, Boolean> {

        ProgressDialog dialog;
        boolean result;
        Context mycontext;
        int member_id;


        public DeleteEmployee(Context c, int member_id) {
            this.mycontext = c;
            this.member_id = member_id;

        }

        @Override
        public void onPreExecute() {
            // Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(mycontext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("updating...");
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();

        }

        @Override
        public Boolean doInBackground(String... parms) {
            // result=false;

            BusinessManagerService bm = new BusinessManagerService();

            result = bm.deleteEmployee(member_id);



            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {

            dialog.dismiss();

            if (result == true) {

                onEmployeeDelete();



            } else {
                employeeIdEditText.setText("");
                Toast.makeText(ManageEmployeeActivity.this, "Something went wrong,try again later", Toast.LENGTH_LONG).show();

            }

        }

    }


    public class DeleteMerchant extends AsyncTask<String, String, Boolean> {

        ProgressDialog dialog;
        boolean result;
        Context mycontext;
        int member_id;


        public DeleteMerchant(Context c) {
            this.mycontext = c;


        }

        @Override
        public void onPreExecute() {
            // Toast.makeText(getActivity(),"Progress",Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(mycontext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Deleting...");
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();

        }

        @Override
        public Boolean doInBackground(String... parms) {
            // result=false;

            BusinessManagerService bm = new BusinessManagerService();

            result = bm.deleteSelf();



            return result;
        }

        @Override
        public void onPostExecute(Boolean result) {

            dialog.dismiss();

            if (result == true) {

                onMerchantDelete();



            } else {

                Toast.makeText(ManageEmployeeActivity.this, "Something went wrong,try again later", Toast.LENGTH_LONG).show();

            }

        }

    }


    private void onMerchantDelete()
    {
        Toast.makeText(ManageEmployeeActivity.this, "Deleted", Toast.LENGTH_LONG).show();
        this.sm.logoutUser();
        Intent intent = new Intent(ManageEmployeeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();


    }


    private void onEmployeeDelete() {

        Toast.makeText(ManageEmployeeActivity.this, "Deleted", Toast.LENGTH_LONG).show();
        mEmployeeList.remove(swipedPosition);
        adapter.notifyDataSetChanged();
        ManageEmployeeActivity.mEmployeeList.clear();
        if (cd.isConnectingToInternet())
            new GetEmployeeListAsyncTask(this, 1).execute();
    }


    private void onEmployeeAdd() {

        employeeIdEditText.setText("");
        Toast.makeText(ManageEmployeeActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
        ManageEmployeeActivity.mEmployeeList.add(member);
        adapter.notifyDataSetChanged();





    }







}
