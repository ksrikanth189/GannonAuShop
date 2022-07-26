package com.gannon.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.gannon.BuildConfig;
import com.gannon.Login.activity.LoginActivity;
import com.gannon.R;
import com.gannon.home.model.ApprovedDenySaveRes;
import com.gannon.home.model.ApprovedSaveReq;
import com.gannon.home.model.DenySaveReq;
import com.gannon.home.model.HomeAllListReq;
import com.gannon.home.model.HomeAllListRes;
import com.gannon.home.model.HomeApproveListRes;
import com.gannon.home.model.HomeDenyListRes;
import com.gannon.home.model.HomeListEditReqPayLoad;
import com.gannon.home.model.HomeListEditResponsePayLoad;
import com.gannon.home.model.HomeSlidingListRes;
import com.gannon.home.model.NotificationsCountReq;
import com.gannon.home.model.NotificationsCountRes;
import com.gannon.home.model.ProductNamesDropDownServiceReq;
import com.gannon.home.model.ProductNamesDropDownServiceRes;
import com.gannon.home.model.SearchProductReq;
import com.gannon.home.model.SearchProductRes;
import com.gannon.myfavourite.MyFavouriteScreen;
import com.gannon.myfavourite.model.MyFavouriteUpdateReqPayLoad;
import com.gannon.mysales.MySalesEditScreen;
import com.gannon.mywins.MyWinsScreen;
import com.gannon.notifications.NotificationActivity;
import com.gannon.notifications.model.NotificationsUpdateReq;
import com.gannon.notifications.model.NotificationsUpdateRes;
import com.gannon.profileUpdate.activity.ProfileUpdateActivity;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.mysales.MySalesScreen;
import com.gannon.uploadAuctionDonation.activity.MultiPhotoSelectActivity;
import com.gannon.uploadAuctionDonation.activity.NewAuctionDonation;
import com.gannon.uploadAuctionDonation.interactor.model.SaveResponsePayLoad;
import com.gannon.usermanagement.UserManagementScreen;
import com.gannon.utils.ApplicationContext;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;
import com.gannon.webview.WebViewImageUpload;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends SuperCompatActivity{
//public class HomeActivity extends SuperCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    Dialog fbDialogue;
    RecyclerView recyclerView, recyclerView1, home_all_recycle;
    int page_position = 0;
    HomeViewSlidingAdapter homeViewSlidingAdapter;
    ArrayList<String> slider_image_array = new ArrayList<>();
    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog, m_progress, m_progress1, m_progress2,m_progress3;
    private TextView name_txt, marque_txt;
    private Boolean firstTime = null, adfirst = false;
    private ImageView menu_item_img, notifica_img, logout_img,
            home_img, fav_img, search_img, profile_img, filter_img;

    private LinearLayout auction_donation_ll,notifica_ll,logout_ll;
    private TextView auction_list, donation_list,notifica_value;


    private NestedScrollView nestedSV;
    private ProgressBar loadingPB;
    int page = 0, limit = 2;

    ProductNamesDropDownServiceRes searchProductRes;

    String typeStr = "auction";
    private ActionBarDrawerToggle mDrawerToggle;
    private FrameLayout home_frame_ll;
    private Dialog customDialog;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        isFirstTime();

        initializeUiElements();

        Bundle extras = getIntent().getExtras();
        if (extras != null && adfirst != null) {
            adfirst = extras.getBoolean("adfirst");
        }


//        if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getAdminFlag() == false) {
//            hideLeftMenuItem();
//        } else {
//            approve_deny_ll.setVisibility(View.VISIBLE);
//
////            if (checkInternet()) {
////                getCategoryList();
////            } else {
////                CustomErrorToast(getResources().getString(R.string.server_not_responding));
////            }
//        }


//        if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getAdminFlag() == false) {
//
//            if (checkInternet()) {
//                getHomeAllListService();
//            } else {
//                CustomErrorToast(getResources().getString(R.string.server_not_responding));
//            }
//
//        }

//        nestedSV = findViewById(R.id.nestedSV);
//        loadingPB = findViewById(R.id.idPBLoading);


//        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                // on scroll change we are checking when users scroll as bottom.
//                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//                    // in this method we are incrementing page number,
//                    // making progress bar visible and calling get data method.
//                    page++;
//                    loadingPB.setVisibility(View.VISIBLE);
//                    getDataFromAPI(page, limit);
//                }
//            }
//        });


    }



    private void getToggleAndSlider() {

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, (Toolbar) findViewById(R.id.toolbar),
                R.string.app_name, R.string.app_name
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

//        home_frame_ll

    }
    private void initializeUiElements() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getAdminFlag() == true) {
            setToolBar(getApplicationContext(), "ALL AUCTION/DONATION", "yes");
        } else {
            setToolBar(getApplicationContext(), "HOME ", "yes");
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        setupdrawerLayout();
        getToggleAndSlider();


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        navHeader = navigationView.getHeaderView(0);
//        name_txt = navHeader.findViewById(R.id.name_txt);
//
        context = getApplicationContext();
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);


        menu_item_img = findViewById(R.id.menu_item_img);
        notifica_img = findViewById(R.id.notifica_img);
        logout_img = findViewById(R.id.logout_img);
        home_img = findViewById(R.id.home_img);
        fav_img = findViewById(R.id.fav_img);
        search_img = findViewById(R.id.search_img);
        profile_img = findViewById(R.id.profile_img);
        filter_img = findViewById(R.id.filter_img);

        auction_donation_ll = findViewById(R.id.auction_donation_ll);
        auction_list = findViewById(R.id.auction_list);
        donation_list = findViewById(R.id.donation_list);
        home_frame_ll = findViewById(R.id.home_frame_ll);
        notifica_value = findViewById(R.id.notifica_value);

        home_all_recycle = (RecyclerView) findViewById(R.id.home_all_recycle);

        notifica_ll = findViewById(R.id.notifica_ll);
        logout_ll = findViewById(R.id.logout_ll);


        GridLayoutManager manager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        home_all_recycle.setLayoutManager(manager2);


        home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
            }
        });

        fav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyFavouriteScreen.class));
            }
        });
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customSearchDialog(typeStr);

            }
        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileUpdateActivity.class));
            }
        });




            logout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutDialog();
            }
        });

      if (checkInternet()) {
            getHomeAllListService("auction",null);
        } else {
            CustomErrorToast(getResources().getString(R.string.server_not_responding));
        }

        auction_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                donation_recycler_view.setVisibility(View.GONE);

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getHomeAllListService("auction",null);
                }

            }
        });

        donation_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recyclerView.setVisibility(View.GONE);

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getHomeAllListService("donation",null);
                }

            }
        });


        NotificationsCountReq countReq = new NotificationsCountReq();
        countReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
        getNotificationsCountService(countReq);


        notifica_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,NotificationActivity.class));
            }
        });


    }



    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        NotificationsCountReq countReq = new NotificationsCountReq();
//        countReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
//        getNotificationsCountService(countReq);


    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public void onBackPressed() {
            logoutDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }

    public void getHomeAllListService(String str,String searchString) {

        try {


            m_progress = new ProgressDialog(HomeActivity.this);
            m_progress.setMessage("Please wait....");
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setIndeterminate(true);
            m_progress.setProgress(0);
            m_progress.setCancelable(false);
            m_progress.show();

            HomeAllListReq dmgreq = new HomeAllListReq();
            dmgreq.setAuctionOrDonation(str);
            dmgreq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
            dmgreq.setLimit(20);
            dmgreq.setOffset(0);
            dmgreq.setSearchString(searchString);


            Call<HomeAllListRes> damageHistoryResPayLoadCall = restAPI.getHomeAllListResCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<HomeAllListRes>() {
                @Override
                public void onResponse(Call<HomeAllListRes> call, Response<HomeAllListRes> response) {
                    if (response.isSuccessful()) {

                        HomeAllListRes mySalesEditResponsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(mySalesEditResponsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(mySalesEditResponsePayLoad));

                        if (mySalesEditResponsePayLoad != null && mySalesEditResponsePayLoad.getStatusCode() == 200 && mySalesEditResponsePayLoad.getMessage().size() > 0)  {
                            loadHistoryData(mySalesEditResponsePayLoad, str);
                            if (customDialog != null) {
                                customDialog.dismiss();
                            }

                        } else {
//                            CustomOKAlertDialog(mySalesEditResponsePayLoad.getError());
                        }
                    }


                    if (m_progress != null && m_progress.isShowing()) {
                        m_progress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<HomeAllListRes> call, Throwable t) {
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

    private void loadHistoryData(HomeAllListRes mySalesEditResponsePayLoad, String str) {
        if (mySalesEditResponsePayLoad.getMessage() != null) {
            if (mySalesEditResponsePayLoad.getMessage().size() > 0) {
                home_all_recycle.setVisibility(View.VISIBLE);
                HomeProductAdapter ca = new HomeProductAdapter(mySalesEditResponsePayLoad, getApplicationContext(), str);
                home_all_recycle.setAdapter(ca);
            } else {
                home_all_recycle.setVisibility(View.GONE);
            }
        }
    }

    public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ProductViewHolder> {

        Context mContext;
        HomeAllListRes damageHistoryResPayLoad;
        String type;

        public HomeProductAdapter(HomeAllListRes damageHistoryResPayLoad, Context context, String str) {
            this.damageHistoryResPayLoad = damageHistoryResPayLoad;
            mContext = context;
            type = str;
        }

        @Override
        public int getItemCount() {

            return damageHistoryResPayLoad.getMessage().size();

        }


        @Override
        public HomeProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.homeall_list_inflator, viewGroup, false);

            return new HomeProductAdapter.ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HomeProductAdapter.ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") int position) {
            String url = "";
            if (damageHistoryResPayLoad.getMessage().get(position).getImageUrl() != null) {
                url = ApplicationContext.BASE_URL + "/" + damageHistoryResPayLoad.getMessage().get(position).getImageUrl().replace(".png", ".jpg");
            }
            Glide.with(HomeActivity.this)
                    .load(url)
                    .error(R.mipmap.icon6)
                    .placeholder(R.mipmap.icon6)
                    .into(productViewHolder.item_img);

            productViewHolder.product_name_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getProductName());
            productViewHolder.datetime_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getClosingDate());


            if (type.equalsIgnoreCase("donation")) {
                productViewHolder.amount_txt.setVisibility(View.GONE);
            } else {
                productViewHolder.amount_txt.setVisibility(View.VISIBLE);
                productViewHolder.amount_txt.setText("$ " + damageHistoryResPayLoad.getMessage().get(position).getAuctionAmount());
            }

            productViewHolder.item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, HomeListEditScreen.class);
                    intent.putExtra("cargoId", damageHistoryResPayLoad.getMessage().get(position).getAuctionId());
                    intent.putExtra("barcode", damageHistoryResPayLoad.getMessage().get(position).getDonationId());
                    intent.putExtra("type", type);
                    intent.putExtra("productname", damageHistoryResPayLoad.getMessage().get(position).getProductName());
                    startActivity(intent);

                }
            });


            if (damageHistoryResPayLoad.getMessage().get(position).getFavouriteCheck() == true) {
                productViewHolder.fav_img.setBackground(getResources().getDrawable(R.mipmap.favorite_y));
            } else {
                productViewHolder.fav_img.setBackground(getResources().getDrawable(R.mipmap.favourite));
            }

            productViewHolder.fav_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyFavouriteUpdateReqPayLoad myFavouriteUpdateReqPayLoad = new MyFavouriteUpdateReqPayLoad();
                    myFavouriteUpdateReqPayLoad.setAuctionId(damageHistoryResPayLoad.getMessage().get(position).getAuctionId());
                    myFavouriteUpdateReqPayLoad.setDonationId(damageHistoryResPayLoad.getMessage().get(position).getDonationId());
                    myFavouriteUpdateReqPayLoad.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());

                    if (damageHistoryResPayLoad.getMessage().get(position).getFavouriteCheck() == true) {
                        myFavouriteUpdateReqPayLoad.setOnOffFlag(false);
                    } else {
                        myFavouriteUpdateReqPayLoad.setOnOffFlag(true);
                    }

                    getFavouriteUpdateService(myFavouriteUpdateReqPayLoad);

                }
            });


        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            protected ImageView item_img, fav_img;
            protected TextView product_name_txt, amount_txt, datetime_txt;
            protected LinearLayout liner_ll;

            public ProductViewHolder(View v) {
                super(v);
                item_img = v.findViewById(R.id.item_img);
                fav_img = v.findViewById(R.id.fav_img);
                product_name_txt = v.findViewById(R.id.product_name_txt);
                amount_txt = v.findViewById(R.id.amount_txt);
                datetime_txt = v.findViewById(R.id.datetime_txt);
                liner_ll = v.findViewById(R.id.liner_ll);


            }
        }

    }


    public void getFavouriteUpdateService(MyFavouriteUpdateReqPayLoad myFavouriteUpdateReqPayLoad) {

        try {


            m_progress2 = new ProgressDialog(HomeActivity.this);
            m_progress2.setMessage("Please wait....");
            m_progress2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress2.setIndeterminate(true);
            m_progress2.setProgress(0);
            m_progress2.setCancelable(true);
            m_progress2.show();


            Call<SaveResponsePayLoad> damageHistoryResPayLoadCall = restAPI.getMyFavouriteUpdatecall(myFavouriteUpdateReqPayLoad);
            damageHistoryResPayLoadCall.enqueue(new Callback<SaveResponsePayLoad>() {
                @Override
                public void onResponse(Call<SaveResponsePayLoad> call, Response<SaveResponsePayLoad> response) {
                    if (response.isSuccessful()) {

                        SaveResponsePayLoad responsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(responsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(responsePayLoad));

                        if (responsePayLoad.getStatusCode().equalsIgnoreCase("200")) {
                            CustomErrorToast(responsePayLoad.getMessage());

                            if (!checkInternet()) {
                                CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                            } else {
                                getHomeAllListService("auction",null);
                            }

                            auction_list.setTextColor(getResources().getColor(R.color.white));
                            auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                            donation_list.setTextColor(getResources().getColor(R.color.white));
                            donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));

                        } else {
                            CustomErrorToast(responsePayLoad.getMessage());
                        }
                    }


                    if (m_progress2 != null && m_progress2.isShowing()) {
                        m_progress2.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<SaveResponsePayLoad> call, Throwable t) {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));

                    if (m_progress2 != null && m_progress2.isShowing()) {
                        m_progress2.dismiss();
                    }

                }
            });

        } catch (Exception e) {

            if (m_progress2 != null && m_progress2.isShowing()) {
                m_progress2.dismiss();
            }

        }
    }

    private void customSearchDialog(String typeStr) {

        customDialog = new Dialog(HomeActivity.this);
        customDialog.setCancelable(true);

        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.search_home_dialog);
        customDialog.getWindow().setGravity(Gravity.TOP);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ImageView search_imag = customDialog.findViewById(R.id.search_imag);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) customDialog.findViewById(R.id.autocomp_search);
//
//        //===========Auto complete text view for test=======
//
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (autoCompleteTextView.isPerformingCompletion()) {
                    // An item has been selected from the list. Ignore.
                    return;
                } else {
//                    getSearchService(typeStr, autoCompleteTextView.getText().toString().trim(), autoCompleteTextView);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTextView.dismissDropDown();

//                if (!checkInternet()) {
//                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
//                } else {
//                    getHomeAllListService(typeStr,autoCompleteTextView.getText().toString());
//                }


//                Intent intent = new Intent(HomeActivity.this, HomeListEditScreen.class);
//                intent.putExtra("cargoId", searchProductRes.getMessage().get(position).getAuctionId());
//                intent.putExtra("barcode", searchProductRes.getMessage().get(position).getDonationId());
//                intent.putExtra("type", typeStr);
//                startActivity(intent);
                ///srikanth

//                customDialog.dismiss();

                hideKeyboard();
            }
        });


        search_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getHomeAllListService(typeStr,autoCompleteTextView.getText().toString());
                }
            }
        });

        customDialog.show();

    }

    public void getSearchService(String typeStr, String searchVal, AutoCompleteTextView autoCompleteTextView) {

        try {


//            m_progress = new ProgressDialog(HomeActivity.this);
//            m_progress.setMessage("Please wait....");
//            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            m_progress.setIndeterminate(true);
//            m_progress.setProgress(0);
//            m_progress.setCancelable(false);
//            m_progress.show();


            ProductNamesDropDownServiceReq  productNamesDropDownServiceReq = new ProductNamesDropDownServiceReq();
            productNamesDropDownServiceReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
            productNamesDropDownServiceReq.setSearchValue(searchVal);
            productNamesDropDownServiceReq.setType(typeStr);



//            SearchProductReq dmgreq = new SearchProductReq();
//            dmgreq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
//            dmgreq.setSearchString(searchVal);
//            dmgreq.setAuctionOrDonation(typeStr);
//            dmgreq.setOffset(0);
//            dmgreq.setLimit(20);


            Call<ProductNamesDropDownServiceRes> damageHistoryResPayLoadCall = restAPI.getProductNamesDropDownServiceResCall(productNamesDropDownServiceReq);
            damageHistoryResPayLoadCall.enqueue(new Callback<ProductNamesDropDownServiceRes>() {
                @Override
                public void onResponse(Call<ProductNamesDropDownServiceRes> call, Response<ProductNamesDropDownServiceRes> response) {
                    if (response.isSuccessful()) {

                        searchProductRes = response.body();

                        Gson gson = new Gson();
                        gson.toJson(searchProductRes);
                        Log.v("damage his", "damge his res" + gson.toJson(searchProductRes));

                        if (searchProductRes.getStatusCode() == 200) {
                            if (searchProductRes.getMessage() != null && searchProductRes.getMessage().size() > 0) {

                                ArrayList<String> vinList = new ArrayList<String>();

                                for (int i = 0; i < searchProductRes.getMessage().size(); i++) {
                                    if (searchProductRes.getMessage().get(i) != null)
                                        vinList.add(searchProductRes.getMessage().get(i));
                                }

                                if (vinList != null) {
                                    if (vinList.size() != 0) {
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                                context,
                                                R.layout.search_text_inflator, R.id.vinId, vinList);
                                        autoCompleteTextView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } else {
                            CustomOKAlertDialog(searchProductRes.getStatus());
                        }

                    }


//                    if (m_progress != null && m_progress.isShowing()) {
//                        m_progress.dismiss();
//                    }
                }

                @Override
                public void onFailure(Call<ProductNamesDropDownServiceRes> call, Throwable t) {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
//                    if (m_progress != null && m_progress.isShowing()) {
//                        m_progress.dismiss();
//                    }

                }
            });

        } catch (Exception e) {

//            if (m_progress != null && m_progress.isShowing()) {
//                m_progress.dismiss();
//            }

            CustomErrorToast(getResources().getString(R.string.server_not_responding));


        }
    }



    public void getNotificationsCountService(NotificationsCountReq statusSaveReq) {

        try {


            m_progress3 = new ProgressDialog(HomeActivity.this);
            m_progress3.setMessage("Please wait....");
            m_progress3.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress3.setIndeterminate(true);
            m_progress3.setProgress(0);
            m_progress3.setCancelable(false);
            m_progress3.show();


            Call<NotificationsCountRes> damageHistoryResPayLoadCall = restAPI.getNotificationsCountResCall(statusSaveReq);
            damageHistoryResPayLoadCall.enqueue(new Callback<NotificationsCountRes>() {
                @Override
                public void onResponse(Call<NotificationsCountRes> call, Response<NotificationsCountRes> response) {
                    if (response.isSuccessful()) {

                        NotificationsCountRes responsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(responsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(responsePayLoad));

                        if (responsePayLoad.getStatusCode() == 200 && responsePayLoad.getMessage().getCount() != 0) {
                            notifica_value.setText(responsePayLoad.getMessage().getCount().toString());
//                            CustomErrorToast(responsePayLoad.getStatus());
                        } else {
//                            CustomErrorToast(responsePayLoad.getError());
                        }


                    }


                    if (m_progress3 != null && m_progress3.isShowing()) {
                        m_progress3.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsCountRes> call, Throwable t) {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));

                    if (m_progress3 != null && m_progress3.isShowing()) {
                        m_progress3.dismiss();
                    }

                }
            });

        } catch (Exception e) {

            CustomErrorToast(getResources().getString(R.string.server_not_responding));

            if (m_progress3 != null && m_progress3.isShowing()) {
                m_progress3.dismiss();
            }

        }
    }

}
