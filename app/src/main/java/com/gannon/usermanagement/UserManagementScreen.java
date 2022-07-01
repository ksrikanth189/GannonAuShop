package com.gannon.usermanagement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gannon.R;
import com.gannon.home.HomeActivity;
import com.gannon.home.model.ApprovedDenySaveRes;
import com.gannon.home.model.ApprovedSaveReq;
import com.gannon.home.model.DenySaveReq;
import com.gannon.home.model.HomeApproveListRes;
import com.gannon.home.model.HomeDenyListRes;
import com.gannon.mysales.MySalesEditScreen;
import com.gannon.mysales.MySalesScreen;
import com.gannon.mysales.model.MySalesReqPayLoad;
import com.gannon.mysales.model.MySalesResponsePayLoad;
import com.gannon.mywins.MyWinsScreen;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.uploadAuctionDonation.activity.NewAuctionDonation;
import com.gannon.utils.ApplicationContext;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by Srikanth.K on
 */

public class UserManagementScreen extends SuperCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {


    private ProgressDialog m_progress,progressDialog,progressDialog1,progressDialog2,m_progress1;
    private Retrofit retrofit;
    private RestAPI restAPI;

    private TextView no_cat_display_id;
    private LinearLayout header_linear;
    private RecyclerView recyclerView,donation_recycler_view;
    private String cargoId = null, barcode = null;
    private String message;
    private ActionBarDrawerToggle mDrawerToggle;

    private TextView auction_list, donation_list;

    private ProgressBar idPBLoading;
    private NestedScrollView idNestedSV;
    int page = 0, limit = 20;

    private HomeApproveListRes homeCategorysListRes;
    private View navHeader;
    private TextView name_txt, marque_txt;



//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }
//
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
        setContentView(R.layout.auction_donations_list_activity);
        initializeUiElements();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            cargoId = null;
            barcode = null;

        } else {
            cargoId = extras.getString("cargoId");
            barcode = extras.getString("barcode");

        }


        restAPI = getRestAPIObj();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        donation_recycler_view = (RecyclerView) findViewById(R.id.donation_recycler_view);
        no_cat_display_id = (TextView) findViewById(R.id.no_cat_display_id);

        auction_list = (TextView) findViewById(R.id.auction_list);
        donation_list = (TextView) findViewById(R.id.donation_list);
        idNestedSV = (NestedScrollView) findViewById(R.id.idNestedSV);
        idPBLoading = findViewById(R.id.idPBLoading);

        auction_list.setText("Newly Registered");
        donation_list.setText("Registered Users");

        recyclerView.setLayoutManager(new LinearLayoutManager(UserManagementScreen.this, LinearLayoutManager.VERTICAL, false));
        donation_recycler_view.setLayoutManager(new LinearLayoutManager(UserManagementScreen.this, LinearLayoutManager.VERTICAL, false));


//        GridLayoutManager manager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(manager2);
//
//        GridLayoutManager manager3 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
//        donation_recycler_view.setLayoutManager(manager3);
//


        if (!checkInternet()) {
            CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
        } else {
            getCategoryList();
        }


//        // adding on scroll change listener method for our nested scroll view.
//        idNestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                // on scroll change we are checking when users scroll as bottom.
//                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//                    // in this method we are incrementing page number,
//                    // making progress bar visible and calling get data method.
//                    page++;
//                    idPBLoading.setVisibility(View.VISIBLE);
//
//                    getMySalesService("auction", page, limit);
//
//                }
//            }
//        });


        auction_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donation_recycler_view.setVisibility(View.GONE);

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getCategoryList();
                }

            }
        });

        donation_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getHomeDenyListResCall();
                }

            }
        });


    }

//    public void getMySalesService(String typestr, int page, int limit) {
//
//        try {
//
//
//            m_progress = new ProgressDialog(UserManagementScreen.this);
//            m_progress.setMessage("Please wait....");
//            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            m_progress.setIndeterminate(true);
//            m_progress.setProgress(0);
//            m_progress.setCancelable(false);
//            m_progress.show();
//
//            MySalesReqPayLoad dmgreq = new MySalesReqPayLoad();
//            dmgreq.setAuctionOrDonation(typestr);
//            dmgreq.setOffset(page);
//            dmgreq.setLimit(limit);
//            dmgreq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
//
//
//            Call<MySalesResponsePayLoad> damageHistoryResPayLoadCall = restAPI.getMySalesListResPayLoadCall(dmgreq);
//            damageHistoryResPayLoadCall.enqueue(new Callback<MySalesResponsePayLoad>() {
//                @Override
//                public void onResponse(Call<MySalesResponsePayLoad> call, Response<MySalesResponsePayLoad> response) {
//                    if (response.isSuccessful()) {
//
//                        MySalesResponsePayLoad responsePayLoad = response.body();
//
//                        Gson gson = new Gson();
//                        gson.toJson(responsePayLoad);
//                        Log.v("damage his", "damge his res" + gson.toJson(responsePayLoad));
//
//                        message = responsePayLoad.getStatus();
//
//                        if (responsePayLoad.getStatusCode() == 200 && responsePayLoad.getMessage().size() > 0) {
//                            loadHistoryData(responsePayLoad,typestr);
//                        } else {
//                            CustomErrorToast(message);
//                        }
//                    }
//
//
//                    if (m_progress != null && m_progress.isShowing()) {
//                        m_progress.dismiss();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<MySalesResponsePayLoad> call, Throwable t) {
//                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
//
//                    if (m_progress != null && m_progress.isShowing()) {
//                        m_progress.dismiss();
//                    }
//
//                }
//            });
//
//        } catch (Exception e) {
//
//            if (m_progress != null && m_progress.isShowing()) {
//                m_progress.dismiss();
//            }
//
//        }
//    }
//    private void loadHistoryData(MySalesResponsePayLoad responsePayLoad,String type) {
//        if (responsePayLoad.getMessage() != null) {
//            if (responsePayLoad.getMessage().size() > 0) {
//                recyclerView.setVisibility(View.VISIBLE);
//                donation_recycler_view.setVisibility(View.VISIBLE);
//                ProductAdapter ca = new ProductAdapter(responsePayLoad, getApplicationContext(),type);
//                recyclerView.setAdapter(ca);
//            } else {
//                recyclerView.setVisibility(View.GONE);
//                donation_recycler_view.setVisibility(View.GONE);
//            }
//        }
//    }
//    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
//
//        Context mContext;
//        MySalesResponsePayLoad damageHistoryResPayLoad;
//        String type;
//
//        public ProductAdapter(MySalesResponsePayLoad damageHistoryResPayLoad, Context context,String type1) {
//            this.damageHistoryResPayLoad = damageHistoryResPayLoad;
//            mContext = context;
//            type =type1;
//        }
//
//        @Override
//        public int getItemCount() {
//            return damageHistoryResPayLoad.getMessage().size();
//
//        }
//
//
//        @Override
//        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View itemView = LayoutInflater.
//                    from(viewGroup.getContext()).
//                    inflate(R.layout.mysales_list_inflator, viewGroup, false);
//
//            return new ProductViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") int position) {
//
//            productViewHolder.item_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getProductName() != null ? damageHistoryResPayLoad.getMessage().get(position).getProductName() : "");
//
//            String url = ApplicationContext.BASE_URL +"/" + damageHistoryResPayLoad.getMessage().get(position).getImageUrl().replace(".png",".jpg");
//
//            Glide.with(UserManagementScreen.this)
////                    .load("http://192.168.1.207:8080/img/mob.jpg")
//                    .load(url)
//                    .error(R.mipmap.icon6)
//                    .placeholder(R.mipmap.icon6)
//                    .into(productViewHolder.item_img);
//
//
////            Glide.with(MySalesScreen.this)
////                    .load(damageHistoryResPayLoad.getMessage().get(position).getImageUrl())
////                    .centerCrop()
////                    .placeholder(R.mipmap.icon6)
////                    .error(R.mipmap.icon6)
////                    .into(productViewHolder.item_img);
//
//
//            productViewHolder.liner_ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(UserManagementScreen.this, MySalesEditScreen.class);
//                    intent.putExtra("cargoId", damageHistoryResPayLoad.getMessage().get(position).getAuctionId());
//                    intent.putExtra("barcode", damageHistoryResPayLoad.getMessage().get(position).getDonationId());
//                    intent.putExtra("type", type);
//                    startActivity(intent);
//
//                }
//            });
//
//
//        }
//
//        public class ProductViewHolder extends RecyclerView.ViewHolder {
//
//            protected TextView item_txt;
//            protected ImageView item_img;
//            protected LinearLayout liner_ll;
//
//            public ProductViewHolder(View v) {
//                super(v);
//                item_txt = v.findViewById(R.id.item_txt);
//                item_img = v.findViewById(R.id.item_img);
//                liner_ll = v.findViewById(R.id.liner_ll);
//
//
//            }
//        }
//
//    }

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

    private void initializeUiElements() {
//
//        //=== Set Tool Bar and Drawer layout ===
//        setToolBar(getApplicationContext(), "User Management", "yes");
//        String pushToken = FirebaseInstanceId.getInstance().getToken();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        setToolBar(getApplicationContext(), "User Management", "yes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navHeader = navigationView.getHeaderView(0);
        name_txt = navHeader.findViewById(R.id.name_txt);


        if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getUserName() != null) {
            name_txt.setText(SharedPrefHelper.getLogin(context).getMessage().getUserName());
        }


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



        hideLeftMenuItem();
    }


    private void hideLeftMenuItem() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_newauction).setVisible(false);
        nav_Menu.findItem(R.id.nav_mysales).setVisible(false);
        nav_Menu.findItem(R.id.nav_mywins).setVisible(false);

    }


    public void getCategoryList() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgress(0);
            progressDialog.show();


            final Call<HomeApproveListRes> adminSlideResCall = restAPI.getHomeCategorysListResCall();
            adminSlideResCall.enqueue(new Callback<HomeApproveListRes>() {
                @Override
                public void onResponse(Call<HomeApproveListRes> call, Response<HomeApproveListRes> response) {
                    if (response.isSuccessful()) {
                        homeCategorysListRes = response.body();
                        if (homeCategorysListRes.getStatusCode() == 200 && homeCategorysListRes.getStatus().equalsIgnoreCase("success")) {

                            if (homeCategorysListRes.getMessage().size() > 0)
                                loadCategDaoryData(homeCategorysListRes);


//                            if (homeCategorysListRes.getBanners().size() > 0) {
//
//                                for (int i = 0; i < homeCategorysListRes.getBanners().size(); i++) {
//                                    slider_image_array.add(homeCategorysListRes.getBanners().get(i).getImage());
//                                }
//
//                                slidingMethod();
//
//                            }

                        } else {
                            CustomErrorToast(homeCategorysListRes.getStatus());
                        }
                    }
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<HomeApproveListRes> call, Throwable t) {

                    CustomErrorToast(getResources().getString(R.string.server_not_responding));

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                }
            });

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public void getHomeDenyListResCall() {
        try {
            progressDialog1 = new ProgressDialog(this);
            progressDialog1.setMessage("Please Wait......");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog1.setIndeterminate(true);
            progressDialog1.setProgress(0);
            progressDialog1.show();


            final Call<HomeDenyListRes> adminSlideResCall = restAPI.getHomeDenyListResCall();
            adminSlideResCall.enqueue(new Callback<HomeDenyListRes>() {
                @Override
                public void onResponse(Call<HomeDenyListRes> call, Response<HomeDenyListRes> response) {
                    if (response.isSuccessful()) {
                        HomeDenyListRes homeCategorysListRes = response.body();
                        if (homeCategorysListRes.getStatusCode() == 200 && homeCategorysListRes.getStatus().equalsIgnoreCase("success")) {

                            if (homeCategorysListRes.getMessage().size() > 0)
                                loadProductData(homeCategorysListRes);

//                            if (homeCategorysListRes.getBanners().size() > 0) {
//
//                                for (int i = 0; i < homeCategorysListRes.getBanners().size(); i++) {
//                                    slider_image_array.add(homeCategorysListRes.getBanners().get(i).getImage());
//                                }
//
//                                slidingMethod();
//
//                            }

                        } else {
                            CustomErrorToast(homeCategorysListRes.getStatus());
                        }
                    }
                    if (progressDialog1.isShowing()) {
                        progressDialog1.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<HomeDenyListRes> call, Throwable t) {

                    CustomErrorToast(getResources().getString(R.string.server_not_responding));

                    if (progressDialog1.isShowing()) {
                        progressDialog1.dismiss();
                    }

                }
            });

        } catch (Exception e) {
            e.fillInStackTrace();

            if (progressDialog1.isShowing()) {
                progressDialog1.dismiss();
            }

        }
    }

    private void loadCategDaoryData(HomeApproveListRes homeSlideRes) {
        if (homeSlideRes.getMessage() != null) {
            if (homeSlideRes.getMessage().size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                donation_recycler_view.setVisibility(View.GONE);
                CategoryAdapter ca = new CategoryAdapter(homeSlideRes.getMessage(), getApplicationContext());
                recyclerView.setAdapter(ca);
            } else {
                recyclerView.setVisibility(View.GONE);
                donation_recycler_view.setVisibility(View.GONE);
            }
        }
    }

    private void loadProductData(HomeDenyListRes homeSlideRes) {
        if (homeSlideRes.getMessage() != null) {
            if (homeSlideRes.getMessage().size() > 0) {
                donation_recycler_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ProductAdapter ca = new ProductAdapter(homeSlideRes.getMessage(), getApplicationContext());
                donation_recycler_view.setAdapter(ca);
            } else {
                donation_recycler_view.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
//            logoutDialog();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if (id == R.id.nav_newregister) {
            startActivity(new Intent(UserManagementScreen.this, UserManagementScreen.class));
        }
        if (id == R.id.nav_newauction) {
            startActivity(new Intent(UserManagementScreen.this, NewAuctionDonation.class));
        }
        if (id == R.id.nav_mysales) {
            startActivity(new Intent(UserManagementScreen.this, MySalesScreen.class));
        }
        if (id == R.id.nav_mywins) {
            startActivity(new Intent(UserManagementScreen.this, MyWinsScreen.class));
        }
        if (id == R.id.nav_all_auctiondonations) {
            startActivity(new Intent(UserManagementScreen.this, HomeActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ProductViewHolder> {

        Context mContext;
        List<HomeApproveListRes.Message> cartListRes;


        public CategoryAdapter(List<HomeApproveListRes.Message> cartListRes, Context context) {
            this.cartListRes = cartListRes;
            this.mContext = context;

        }

        @Override
        public int getItemCount() {
            return cartListRes.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public CategoryAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView =
                    LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_categories_inflator, viewGroup, false);

            return new CategoryAdapter.ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CategoryAdapter.ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") final int position) {

//            productViewHolder.firstname_txt.setText(cartListRes.get(position).getFirstName() + " " + cartListRes.get(position).getLastName());
            productViewHolder.firstname_txt.setText(cartListRes.get(position).getFirstName());
            productViewHolder.lastName_txt.setText(cartListRes.get(position).getLastName() != null
                    ? cartListRes.get(position).getLastName() : "");
            productViewHolder.email_txt.setText(cartListRes.get(position).getEmail() != null
                    ? cartListRes.get(position).getEmail() : "");
            productViewHolder.phoneNumber_txt.setText(cartListRes.get(position).getPhoneNumber() != null
                    ? cartListRes.get(position).getPhoneNumber() : "");


            productViewHolder.approve_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkInternet()) {

                        ApprovedSaveReq approvedSaveReq = new ApprovedSaveReq();
                        approvedSaveReq.setApproved("Y");
                        approvedSaveReq.setRegistrationId(cartListRes.get(position).getRegistrationId());
                        approvedSaveReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
                        getApprovedSaveReqCall(approvedSaveReq);
                    } else {
                        CustomErrorToast(getResources().getString(R.string.server_not_responding));
                    }

                }
            });


            productViewHolder.deny_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInternet()) {
                        ApprovedSaveReq approvedSaveReq = new ApprovedSaveReq();
                        approvedSaveReq.setApproved("N");
                        approvedSaveReq.setRegistrationId(cartListRes.get(position).getRegistrationId());
                        approvedSaveReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
                        getApprovedSaveReqCall(approvedSaveReq);
                    } else {
                        CustomErrorToast(getResources().getString(R.string.server_not_responding));
                    }

                }
            });


//            Glide.with(HomeActivity.this)
//                    .load(cartListRes.get(position).getImage())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .into(productViewHolder.imageView);


        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            protected TextView firstname_txt, lastName_txt, email_txt, phoneNumber_txt;
            protected Button approve_btn, deny_btn;

            public ProductViewHolder(View v) {
                super(v);
                firstname_txt = (TextView) v.findViewById(R.id.firstname_txt);
                lastName_txt = (TextView) v.findViewById(R.id.lastName_txt);
                email_txt = (TextView) v.findViewById(R.id.email_txt);
                phoneNumber_txt = (TextView) v.findViewById(R.id.phoneNumber_txt);
                approve_btn = (Button) v.findViewById(R.id.approve_btn);
                deny_btn = (Button) v.findViewById(R.id.deny_btn);
            }
        }

    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

        Context mContext;
        List<HomeDenyListRes.Message> cartListRes;


        public ProductAdapter(List<HomeDenyListRes.Message> cartListRes, Context context) {
            this.cartListRes = cartListRes;
            this.mContext = context;

        }

        @Override
        public int getItemCount() {
            return cartListRes.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView =
                    LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_products_inflator, viewGroup, false);

            return new ProductAdapter.ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProductAdapter.ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") final int position) {

//            productViewHolder.firstname_txt.setText(cartListRes.get(position).getFirstName() + " " + cartListRes.get(position).getLastName());
            productViewHolder.firstname_txt.setText(cartListRes.get(position).getFirstName());
            productViewHolder.lastName_txt.setText(cartListRes.get(position).getLastName() != null
                    ? cartListRes.get(position).getLastName() : "");
            productViewHolder.email_txt.setText(cartListRes.get(position).getEmail() != null
                    ? cartListRes.get(position).getEmail() : "");
            productViewHolder.phoneNumber_txt.setText(cartListRes.get(position).getPhoneNumber() != null
                    ? cartListRes.get(position).getPhoneNumber() : "");

            if (cartListRes.get(position).getStatus().equalsIgnoreCase("Activated")) {
                productViewHolder.deny_btn.setText("Deactivate");
            } else if (cartListRes.get(position).getStatus().equalsIgnoreCase("Deactivated")) {
                productViewHolder.deny_btn.setText("Activated");
            }

            productViewHolder.deny_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkInternet()) {

                        DenySaveReq approvedSaveReq = new DenySaveReq();
                        if (cartListRes.get(position).getStatus().equalsIgnoreCase("Activated")){
                            approvedSaveReq.setApproved("N");
                        }else {
                            approvedSaveReq.setApproved("Y");
                        }
                        approvedSaveReq.setRegistrationId(cartListRes.get(position).getRegistrationId());
                        approvedSaveReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
                        getDenySaveReqCall(approvedSaveReq);
                    } else {
                        CustomErrorToast(getResources().getString(R.string.server_not_responding));
                    }

                }
            });


        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            protected TextView firstname_txt, lastName_txt, email_txt, phoneNumber_txt;
            protected Button approve_btn, deny_btn;

            public ProductViewHolder(View v) {
                super(v);
                firstname_txt = (TextView) v.findViewById(R.id.firstname_txt);
                lastName_txt = (TextView) v.findViewById(R.id.lastName_txt);
                email_txt = (TextView) v.findViewById(R.id.email_txt);
                phoneNumber_txt = (TextView) v.findViewById(R.id.phoneNumber_txt);
                approve_btn = (Button) v.findViewById(R.id.approve_btn);
                deny_btn = (Button) v.findViewById(R.id.deny_btn);
            }
        }

    }

    public void getApprovedSaveReqCall(ApprovedSaveReq approvedSaveReq) {
        try {
            progressDialog2 = new ProgressDialog(this);
            progressDialog2.setMessage("Please Wait......");
            progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog2.setIndeterminate(true);
            progressDialog2.setProgress(0);
            progressDialog2.show();


            final Call<ApprovedDenySaveRes> adminSlideResCall = restAPI.getApproveSaveResCall(approvedSaveReq);
            adminSlideResCall.enqueue(new Callback<ApprovedDenySaveRes>() {
                @Override
                public void onResponse(Call<ApprovedDenySaveRes> call, Response<ApprovedDenySaveRes> response) {
                    if (response.isSuccessful()) {
                        ApprovedDenySaveRes homeCategorysListRes = response.body();
                        if (homeCategorysListRes.getStatusCode() == 200 && homeCategorysListRes.getStatus().equalsIgnoreCase("success")) {
                            CustomErrorToast(homeCategorysListRes.getMessage());

                            startActivity(new Intent(UserManagementScreen.this,UserManagementScreen.class));
                            finish();

//                            if (checkInternet()) {
//                                getCategoryList();
//                            } else {
//                                CustomErrorToast(getResources().getString(R.string.server_not_responding));
//                            }

                        } else {
                            CustomErrorToast(homeCategorysListRes.getStatus());
                        }
                    }
                    if (progressDialog2.isShowing()) {
                        progressDialog2.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ApprovedDenySaveRes> call, Throwable t) {

                    CustomErrorToast(getResources().getString(R.string.server_not_responding));

                    if (progressDialog2.isShowing()) {
                        progressDialog2.dismiss();
                    }

                }
            });

        } catch (Exception e) {
            e.fillInStackTrace();

            if (progressDialog2.isShowing()) {
                progressDialog2.dismiss();
            }

        }
    }

    public void getDenySaveReqCall(DenySaveReq approvedSaveReq) {
        try {
            m_progress1 = new ProgressDialog(this);
            m_progress1.setMessage("Please Wait......");
            m_progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress1.setIndeterminate(true);
            m_progress1.setProgress(0);
            m_progress1.show();


            final Call<ApprovedDenySaveRes> adminSlideResCall = restAPI.getDenySaveResCall(approvedSaveReq);
            adminSlideResCall.enqueue(new Callback<ApprovedDenySaveRes>() {
                @Override
                public void onResponse(Call<ApprovedDenySaveRes> call, Response<ApprovedDenySaveRes> response) {
                    if (response.isSuccessful()) {
                        ApprovedDenySaveRes homeCategorysListRes = response.body();
                        if (homeCategorysListRes.getStatusCode() == 200 && homeCategorysListRes.getStatus().equalsIgnoreCase("success")) {
                            CustomErrorToast(homeCategorysListRes.getMessage());


                            startActivity(new Intent(UserManagementScreen.this,UserManagementScreen.class));
                            finish();

//                            if (checkInternet()) {
//                                getHomeDenyListResCall();
//                            } else {
//                                CustomErrorToast(getResources().getString(R.string.server_not_responding));
//                            }

                        } else {
                            CustomErrorToast(homeCategorysListRes.getStatus());
                        }
                    }
                    if (m_progress1.isShowing()) {
                        m_progress1.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ApprovedDenySaveRes> call, Throwable t) {

                    CustomErrorToast(getResources().getString(R.string.server_not_responding));

                    if (m_progress1.isShowing()) {
                        m_progress1.dismiss();
                    }

                }
            });

        } catch (Exception e) {
            e.fillInStackTrace();

            if (m_progress1.isShowing()) {
                m_progress1.dismiss();
            }

        }
    }



}