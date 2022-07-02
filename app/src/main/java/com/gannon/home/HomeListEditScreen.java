package com.gannon.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.gannon.BuildConfig;
import com.gannon.R;
import com.gannon.home.model.AmountSaveReq;
import com.gannon.home.model.HomeListEditReqPayLoad;
import com.gannon.home.model.HomeListEditResponsePayLoad;
import com.gannon.myfavourite.MyFavouriteScreen;
import com.gannon.myfavourite.model.MyFavouriteUpdateReqPayLoad;
import com.gannon.mysales.model.MySalesUpdateReq;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.uploadAuctionDonation.FileUploadRespPojo;
import com.gannon.uploadAuctionDonation.activity.MultiPhotoSelectActivity;
import com.gannon.uploadAuctionDonation.interactor.model.SaveResponsePayLoad;
import com.gannon.utils.ApplicationContext;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeListEditScreen extends SuperCompatActivity {

    Button buttonsave, buttonclear;
    Call<SaveResponsePayLoad> damageSaveResponsePayLoadCall;
    SaveResponsePayLoad damage_saveResponsePayLoad;
    private int cargoId, barcode;
    private RestAPI restAPI;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressDialog progressDialog, progressDialog1;

    TextView productname_edt, productdes_edt, auctionAmount_txt,auctionCloseDate_txt, sellerName_txt, sellerEMail_txt, sellerPhoneNumber_txt;
    private ProgressDialog m_progress,m_progress1;
    HomeListEditResponsePayLoad mySalesEditResponsePayLoad;

    int pagePosition = 0, page_position = 0;
    HomeViewSlidingAdapter homeViewSlidingAdapter;
    ArrayList<String> slider_image_array = new ArrayList<>();
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    private TextView[] dots;
    private FrameLayout slide_fram;

    private EditText newAmount_edt;
    private Button save_btn;
    private String type,screen;

    private LinearLayout amount_ll,sellerName_ll;


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.home_list_edit_activity);

        initializeUiElements();


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            cargoId = 0;
            barcode = 0;
            type = null;
            screen = null;

        } else {
            cargoId = extras.getInt("cargoId");
            barcode = extras.getInt("barcode");
            type = extras.getString("type");
            screen = extras.getString("screen");

        }

        if (!checkInternet()) {
            CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
        } else {
            getMySalesService(type);
        }

        if (type.equalsIgnoreCase("auction")){
            amount_ll.setVisibility(View.VISIBLE);
            sellerName_ll.setVisibility(View.VISIBLE);
        }else {
            amount_ll.setVisibility(View.GONE);
            sellerName_ll.setVisibility(View.VISIBLE);
        }

        if (screen != null && screen.equalsIgnoreCase("mywins")){
            amount_ll.setVisibility(View.GONE);
            sellerName_ll.setVisibility(View.VISIBLE);
        }else {
//            amount_ll.setVisibility(View.VISIBLE);
//            sellerName_ll.setVisibility(View.VISIBLE);
        }

    }


    private void slidingMethod() {

        if (slider_image_array != null) {

            homeViewSlidingAdapter = new HomeViewSlidingAdapter(HomeListEditScreen.this, slider_image_array, mySalesEditResponsePayLoad);
            vp_slider.setAdapter(homeViewSlidingAdapter);


            vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position);

                    pagePosition = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        }


    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_array.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#fd726b"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }


//    private void getToggleAndSlider() {
//
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, (Toolbar) findViewById(R.id.toolbar),
//                R.string.app_name, R.string.app_name
//        );
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();
//
//    }

    public void showProgress() {
        if (!isFinishing()) {
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initializeUiElements() {

        //=== Set Tool Bar and Drawer layout ===
        setToolBar(getApplicationContext(), "Products", "yes");
        String pushToken = FirebaseInstanceId.getInstance().getToken();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        context = getApplicationContext();
//        getToggleAndSlider();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        //========================================
        context = getApplicationContext();
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);

        vp_slider = findViewById(R.id.vp_slider);
        ll_dots = findViewById(R.id.ll_dots);


        buttonsave = (Button) findViewById(R.id.buttonsave);
        buttonclear = ((Button) findViewById(R.id.buttonclear));

        productname_edt = findViewById(R.id.productname_edt);
        productdes_edt = findViewById(R.id.productdes_edt);
        auctionAmount_txt = findViewById(R.id.auctionAmount_txt);
        auctionCloseDate_txt = findViewById(R.id.auctionCloseDate_txt);
        sellerName_txt = findViewById(R.id.sellerName_txt);
        sellerEMail_txt = findViewById(R.id.sellerEMail_txt);
        sellerPhoneNumber_txt = findViewById(R.id.sellerPhoneNumber_txt);
        slide_fram = findViewById(R.id.slide_fram);
        newAmount_edt = findViewById(R.id.newAmount_edt);
        save_btn = findViewById(R.id.save_btn);
        amount_ll = findViewById(R.id.amount_ll);
        sellerName_ll = findViewById(R.id.sellerName_ll);


        save_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if (!auctionAmount_txt.getText().toString().equalsIgnoreCase(newAmount_edt.getText().toString()) && newAmount_edt.getText().toString().length() != 0){

                    AmountSaveReq amountSaveReq = new AmountSaveReq();
                    amountSaveReq.setAuctionAmount(Integer.valueOf(newAmount_edt.getText().toString()));
                    amountSaveReq.setAuctionId(cargoId);
                    amountSaveReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());

                    getAmountUpdateService(amountSaveReq);

                }else {

                    CustomErrorToast("Please check auction amount");
                }


            }
        });

        buttonclear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(HomeListEditScreen.this, HomeActivity.class);
//                Intent intent = new Intent(MySalesEditScreen.this, MySalesEditScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        buttonsave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (productname_edt.getText().toString().length() == 0) {
                    CustomErrorToast(getResources().getString(R.string.plz_enter_productname));
                } else if (productdes_edt.getText().toString().length() == 0) {
                    CustomErrorToast(getResources().getString(R.string.plz_enter_productdes));
                } else {
                    if (checkInternet()) {
                        NewAuctionDonationDetailstSave();
                    } else {
                        CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                    }
                }
            }
        });


        addBottomDots(0);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_array.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 3000);


    }

    /*save service */
    private void NewAuctionDonationDetailstSave() {

        try {

            showProgress();

            MySalesUpdateReq requestPayLoad = new MySalesUpdateReq();
//            requestPayLoad.setAuctionOrDonation(type_btn.getText().toString());
//            requestPayLoad.setProductName(productname_edt.getText().toString());
//            requestPayLoad.setProductDescription(productdes_edt.getText().toString());
//            requestPayLoad.setAuctionCloseDate(damage_date.getText().toString().trim() + " " + damage_time.getText().toString().trim());
//            requestPayLoad.setAuctionId(cargoId);
//            requestPayLoad.setDonationId(barcode);
//            requestPayLoad.setImagesList(multiPartImagesList);

            Gson gson = new Gson();
            gson.toJson(requestPayLoad);
            Log.v("damage", "damge save" + gson.toJson(requestPayLoad));

            damageSaveResponsePayLoadCall = restAPI.getMySalesUpdateResponsePayLoadCall(requestPayLoad);
            damageSaveResponsePayLoadCall.enqueue(new Callback<SaveResponsePayLoad>() {
                @Override
                public void onResponse(Call<SaveResponsePayLoad> call, Response<SaveResponsePayLoad> response) {

                    hideProgress();
                    if (response.isSuccessful()) {

                        damage_saveResponsePayLoad = response.body();

                        if (damage_saveResponsePayLoad.getStatus().equalsIgnoreCase("success")) {

                            CustomErrorToast(damage_saveResponsePayLoad.getMessage());

                            Intent intent = new Intent(HomeListEditScreen.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


                        } else {

                            CustomOKAlertDialog(damage_saveResponsePayLoad.getMessage());
                        }

                    }

                    hideProgress();


                }

                @Override
                public void onFailure(Call<SaveResponsePayLoad> call, Throwable t) {
                    CustomOKAlertDialog(damage_saveResponsePayLoad.getMessage());
                    hideProgress();


                }


            });


        } catch (Exception e) {

            //     hideProgress();
            e.getMessage();
        }

    }


    public void getMySalesService(String type) {

        try {


            m_progress = new ProgressDialog(HomeListEditScreen.this);
            m_progress.setMessage("Please wait....");
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setIndeterminate(true);
            m_progress.setProgress(0);
            m_progress.setCancelable(false);
            m_progress.show();

            HomeListEditReqPayLoad dmgreq = new HomeListEditReqPayLoad();
            dmgreq.setAuctionId(cargoId);
            dmgreq.setDonationId(barcode);
            dmgreq.setAuctionOrDonation(type);


            Call<HomeListEditResponsePayLoad> damageHistoryResPayLoadCall = restAPI.getHomeListEditResponsePayLoadCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<HomeListEditResponsePayLoad>() {
                @Override
                public void onResponse(Call<HomeListEditResponsePayLoad> call, Response<HomeListEditResponsePayLoad> response) {
                    if (response.isSuccessful()) {

                        mySalesEditResponsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(mySalesEditResponsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(mySalesEditResponsePayLoad));

                        if (mySalesEditResponsePayLoad.getStatusCode() == 200) {

                            productname_edt.setText(mySalesEditResponsePayLoad.getMessage().getProductName());
                            productdes_edt.setText(mySalesEditResponsePayLoad.getMessage().getProductDescription());
                            auctionAmount_txt.setText("$ "+mySalesEditResponsePayLoad.getMessage().getAuctionAmount().toString());
                            auctionAmount_txt.setText("$ "+mySalesEditResponsePayLoad.getMessage().getAuctionAmount().toString());
                            auctionCloseDate_txt.setText(mySalesEditResponsePayLoad.getMessage().getAuctionCloseDate());
                            sellerName_txt.setText(mySalesEditResponsePayLoad.getMessage().getSellerName());
                            sellerEMail_txt.setText(mySalesEditResponsePayLoad.getMessage().getSellerEMail());
                            sellerPhoneNumber_txt.setText(mySalesEditResponsePayLoad.getMessage().getSellerPhoneNumber().toString());

                            if (mySalesEditResponsePayLoad.getMessage().getImagesList().size() > 0) {

                                slide_fram.setVisibility(View.VISIBLE);

                                for (int i = 0; i < mySalesEditResponsePayLoad.getMessage().getImagesList().size(); i++) {
                                    slider_image_array.add(mySalesEditResponsePayLoad.getMessage().getImagesList().get(i));
                                }

                                slidingMethod();

                            }else {
                                slide_fram.setVisibility(View.GONE);

                            }


                        } else {
                            CustomOKAlertDialog(mySalesEditResponsePayLoad.getStatus());
                        }
                    }


                    if (m_progress != null && m_progress.isShowing()) {
                        m_progress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<HomeListEditResponsePayLoad> call, Throwable t) {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
                    if (m_progress != null && m_progress.isShowing()) {
                        m_progress.dismiss();
                    }

                }
            });

        } catch (Exception e) {

            if (m_progress != null && m_progress.isShowing()) {
                m_progress.dismiss();
            }

            CustomErrorToast(getResources().getString(R.string.server_not_responding));


        }
    }



    public void getAmountUpdateService(AmountSaveReq amountSaveReq) {

        try {


            m_progress1 = new ProgressDialog(HomeListEditScreen.this);
            m_progress1.setMessage("Please wait....");
            m_progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress1.setIndeterminate(true);
            m_progress1.setProgress(0);
            m_progress1.setCancelable(false);
            m_progress1.show();


            Call<SaveResponsePayLoad> damageHistoryResPayLoadCall = restAPI.getAmountSaveResCall(amountSaveReq);
            damageHistoryResPayLoadCall.enqueue(new Callback<SaveResponsePayLoad>() {
                @Override
                public void onResponse(Call<SaveResponsePayLoad> call, Response<SaveResponsePayLoad> response) {
                    if (response.isSuccessful()) {

                        SaveResponsePayLoad responsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(responsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(responsePayLoad));

                        if (responsePayLoad.getStatusCode().equalsIgnoreCase("200") ) {
                            CustomErrorToast(responsePayLoad.getMessage());

                            startActivity(new Intent(HomeListEditScreen.this,HomeActivity.class));

//                            if (!checkInternet()) {
//                                CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
//                            } else {
//                                getMySalesService();
//                            }

                            newAmount_edt.setText("");

                        } else {
                            CustomErrorToast(responsePayLoad.getError());
                        }
                    }


                    if (m_progress1 != null && m_progress1.isShowing()) {
                        m_progress1.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<SaveResponsePayLoad> call, Throwable t) {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));

                    if (m_progress1 != null && m_progress1.isShowing()) {
                        m_progress1.dismiss();
                    }

                }
            });

        } catch (Exception e) {

            if (m_progress1 != null && m_progress1.isShowing()) {
                m_progress1.dismiss();
            }

        }
    }



}