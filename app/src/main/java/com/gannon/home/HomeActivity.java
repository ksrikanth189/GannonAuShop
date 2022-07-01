package com.gannon.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.gannon.home.model.SearchProductReq;
import com.gannon.home.model.SearchProductRes;
import com.gannon.myfavourite.MyFavouriteScreen;
import com.gannon.myfavourite.model.MyFavouriteUpdateReqPayLoad;
import com.gannon.mysales.MySalesEditScreen;
import com.gannon.mywins.MyWinsScreen;
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


//public class HomeActivity extends SuperCompatActivity{
public class HomeActivity extends SuperCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    Dialog fbDialogue;
    RecyclerView recyclerView, recyclerView1, home_all_recycle;
    int page_position = 0;
    HomeViewSlidingAdapter homeViewSlidingAdapter;
    ArrayList<String> slider_image_array = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    List<String> youtubePlaylist;
    private View navHeader;
    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog, progressDialog1, progressDialog2, m_progress, m_progress1, m_progress2;
    private TextView name_txt, marque_txt;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    private TextView[] dots;
    private int pagePosition;
    private HomeSlidingListRes homeAddsRes;
    private HomeApproveListRes homeCategorysListRes;
    private LinearLayout approve_deny_ll;
    private Boolean firstTime = null, adfirst = false;
    private ImageView menu_item_img, notifica_img, logout_img,
            home_img, fav_img, search_img, profile_img, filter_img;

    private TextView approve_list, deny_list;

    private LinearLayout auction_donation_ll, autocomp_ll;
    private TextView auction_list, donation_list;


    private NestedScrollView nestedSV;
    private ProgressBar loadingPB;
    int page = 0, limit = 2;

    AutoCompleteTextView autocomp_search;
    SearchProductRes searchProductRes;

    String typeStr = "auction";

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
        approve_deny_ll = findViewById(R.id.approve_deny_ll);


        vp_slider = findViewById(R.id.vp_slider);
        ll_dots = findViewById(R.id.ll_dots);
        approve_list = findViewById(R.id.approve_list);
        deny_list = findViewById(R.id.deny_list);

        auction_donation_ll = findViewById(R.id.auction_donation_ll);
        auction_list = findViewById(R.id.auction_list);
        donation_list = findViewById(R.id.donation_list);
        autocomp_search = findViewById(R.id.autocomp_search);
        autocomp_ll = findViewById(R.id.autocomp_ll);

        if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getUserName() != null) {
            name_txt.setText(SharedPrefHelper.getLogin(context).getMessage().getUserName());
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView1);
        home_all_recycle = (RecyclerView) findViewById(R.id.home_all_recycle);

        GridLayoutManager manager = new GridLayoutManager(this,
                1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


        GridLayoutManager manager1 = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(manager1);

        GridLayoutManager manager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        home_all_recycle.setLayoutManager(manager2);

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


        approve_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView1.setVisibility(View.GONE);


                if (checkInternet()) {
                    getCategoryList();
                } else {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
                }
            }
        });

        deny_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView.setVisibility(View.GONE);

                if (checkInternet()) {
                    getHomeDenyListResCall();
                } else {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
                }
            }
        });
//

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
//                autocomp_ll.setVisibility(View.VISIBLE);

//                getSearchService(autocomp_search.getText().toString().trim());

                customSearchDialog(typeStr);


//                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
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

        if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getAdminFlag() == true) {
            hideLeftMenuItem();
        } else {
            hideLeftMenuHomeItem();
            approve_deny_ll.setVisibility(View.GONE);
        }

//        if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getAdminFlag() == false) {
//
//            if (checkInternet()) {
//                getHomeAllListService("auction");
//            } else {
//                CustomErrorToast(getResources().getString(R.string.server_not_responding));
//            }
//
//        }


        if (checkInternet()) {
            getHomeAllListService("auction");
        } else {
            CustomErrorToast(getResources().getString(R.string.server_not_responding));
        }

        auction_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typeStr = "auction";
                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));

                if (checkInternet()) {
                    getHomeAllListService("auction");
                } else {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
                }
            }
        });
        donation_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typeStr = "donation";

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));


                if (checkInternet()) {
                    getHomeAllListService("donation");
                } else {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
                }
            }
        });

    }


    private void hideLeftMenuItem() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_newregister).setVisible(true);
        nav_Menu.findItem(R.id.nav_all_auctiondonations).setVisible(true);
        nav_Menu.findItem(R.id.nav_newauction).setVisible(false);
        nav_Menu.findItem(R.id.nav_mysales).setVisible(false);
        nav_Menu.findItem(R.id.nav_mywins).setVisible(false);

    }

    private void hideLeftMenuHomeItem() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_newregister).setVisible(false);
        nav_Menu.findItem(R.id.nav_all_auctiondonations).setVisible(false);
        nav_Menu.findItem(R.id.nav_newauction).setVisible(true);
        nav_Menu.findItem(R.id.nav_mysales).setVisible(true);
        nav_Menu.findItem(R.id.nav_mywins).setVisible(true);

    }

    public void logoutDialog() {

        final Dialog dialogComp = new Dialog(HomeActivity.this);
        dialogComp.setCancelable(true);
        dialogComp.setCanceledOnTouchOutside(false);
        dialogComp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComp.setContentView(R.layout.app_exit_dialog);
        dialogComp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogComp.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final Button closeDialog = (Button) dialogComp.findViewById(R.id.closeDialog);
        final Button clearLogin = (Button) dialogComp.findViewById(R.id.clearLogin);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComp.dismiss();
            }
        });

        clearLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefHelper.clearLoginData(context);
                dialogComp.dismiss();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });


        dialogComp.show();
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


    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

//    private void slidingMethod() {
//
//        if (slider_image_array != null) {
//
//            homeViewSlidingAdapter = new HomeViewSlidingAdapter(HomeActivity.this, slider_image_array, homeCategorysListRes);
//            vp_slider.setAdapter(homeViewSlidingAdapter);
//
//
//            vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                    addBottomDots(position);
//
//                    pagePosition = position;
//
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            });
//
//
//        }
//
//
//    }

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            logoutDialog();
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
            approve_deny_ll.setVisibility(View.GONE);

            startActivity(new Intent(HomeActivity.this, UserManagementScreen.class));


//            if (checkInternet()) {
//                getCategoryList();
//            } else {
//                CustomErrorToast(getResources().getString(R.string.server_not_responding));
//            }

//            if (checkInternet()) {
//                getHomeDenyListResCall();
//            } else {
//                CustomErrorToast(getResources().getString(R.string.server_not_responding));
//            }
        }
        if (id == R.id.nav_newauction) {
            startActivity(new Intent(HomeActivity.this, NewAuctionDonation.class));
        }
        if (id == R.id.nav_mysales) {
            startActivity(new Intent(HomeActivity.this, MySalesScreen.class));
        }
        if (id == R.id.nav_mywins) {
            startActivity(new Intent(HomeActivity.this, MyWinsScreen.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //
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
                recyclerView1.setVisibility(View.GONE);
                CategoryAdapter ca = new CategoryAdapter(homeSlideRes.getMessage(), getApplicationContext());
                recyclerView.setAdapter(ca);
            } else {
                recyclerView.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.GONE);
            }
        }
    }

    private void loadProductData(HomeDenyListRes homeSlideRes) {
        if (homeSlideRes.getMessage() != null) {
            if (homeSlideRes.getMessage().size() > 0) {
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ProductAdapter ca = new ProductAdapter(homeSlideRes.getMessage(), getApplicationContext());
                recyclerView1.setAdapter(ca);
            } else {
                recyclerView1.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
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
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView =
                    LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_categories_inflator, viewGroup, false);

            return new ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") final int position) {

            productViewHolder.firstname_txt.setText(cartListRes.get(position).getFirstName() + " " + cartListRes.get(position).getLastName());
//            productViewHolder.lastName_txt.setText(cartListRes.get(position).getLastName() != null
//                    ? cartListRes.get(position).getLastName() : "");
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
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView =
                    LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_products_inflator, viewGroup, false);

            return new ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") final int position) {

            productViewHolder.firstname_txt.setText(cartListRes.get(position).getFirstName() + " " + cartListRes.get(position).getLastName());
//            productViewHolder.lastName_txt.setText(cartListRes.get(position).getLastName() != null
//                    ? cartListRes.get(position).getLastName() : "");
            productViewHolder.email_txt.setText(cartListRes.get(position).getEmail() != null
                    ? cartListRes.get(position).getEmail() : "");
            productViewHolder.phoneNumber_txt.setText(cartListRes.get(position).getPhoneNumber() != null
                    ? cartListRes.get(position).getPhoneNumber() : "");

            if (cartListRes.get(position).getStatus().equalsIgnoreCase("Activated")) {
                productViewHolder.deny_btn.setText(" Deactivated ");
            } else if (cartListRes.get(position).getStatus().equalsIgnoreCase("Deactivated")) {
                productViewHolder.deny_btn.setText(" Activated ");
            }

            productViewHolder.deny_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkInternet()) {

                        DenySaveReq approvedSaveReq = new DenySaveReq();
                        approvedSaveReq.setApproved("Y");
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


//                             recreate();

                            approve_deny_ll.setVisibility(View.VISIBLE);


                            if (checkInternet()) {
                                getCategoryList();
                            } else {
                                CustomErrorToast(getResources().getString(R.string.server_not_responding));
                            }

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

                            approve_deny_ll.setVisibility(View.VISIBLE);

                            if (checkInternet()) {
                                getHomeDenyListResCall();
                            } else {
                                CustomErrorToast(getResources().getString(R.string.server_not_responding));
                            }

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


    public void getHomeAllListService(String str) {

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


            Call<HomeAllListRes> damageHistoryResPayLoadCall = restAPI.getHomeAllListResCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<HomeAllListRes>() {
                @Override
                public void onResponse(Call<HomeAllListRes> call, Response<HomeAllListRes> response) {
                    if (response.isSuccessful()) {

                        HomeAllListRes mySalesEditResponsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(mySalesEditResponsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(mySalesEditResponsePayLoad));

                        if (mySalesEditResponsePayLoad.getStatusCode() == 200) {
                            loadHistoryData(mySalesEditResponsePayLoad, str);
                        } else {
                            CustomOKAlertDialog(mySalesEditResponsePayLoad.getStatus());
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
                    startActivity(intent);

                }
            });


            if (damageHistoryResPayLoad.getMessage().get(position).isFavouriteCheck()) {
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

                    if (damageHistoryResPayLoad.getMessage().get(position).isFavouriteCheck()) {
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
                                getHomeAllListService("auction");
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

        final Dialog customDialog = new Dialog(HomeActivity.this);
        customDialog.setCancelable(true);

        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.search_home_dialog);
        customDialog.getWindow().setGravity(Gravity.CENTER);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) customDialog.findViewById(R.id.autocomp_search);
//
//        //===========Auto complete text view for vin=======
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
                    getSearchService(typeStr, autoCompleteTextView.getText().toString().trim(), autoCompleteTextView);
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

                Intent intent = new Intent(HomeActivity.this, HomeListEditScreen.class);
                intent.putExtra("cargoId", searchProductRes.getMessage().get(position).getAuctionId());
                intent.putExtra("barcode", searchProductRes.getMessage().get(position).getDonationId());
                intent.putExtra("type", typeStr);
                startActivity(intent);
                ///srikanth

                customDialog.dismiss();

                hideKeyboard();
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

            SearchProductReq dmgreq = new SearchProductReq();

            dmgreq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
            dmgreq.setSearchString(searchVal);
            dmgreq.setAuctionOrDonation(typeStr);
            dmgreq.setOffset(0);
            dmgreq.setLimit(20);


            Call<SearchProductRes> damageHistoryResPayLoadCall = restAPI.getSearchProductResCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<SearchProductRes>() {
                @Override
                public void onResponse(Call<SearchProductRes> call, Response<SearchProductRes> response) {
                    if (response.isSuccessful()) {

                        searchProductRes = response.body();

                        Gson gson = new Gson();
                        gson.toJson(searchProductRes);
                        Log.v("damage his", "damge his res" + gson.toJson(searchProductRes));

                        if (searchProductRes.getStatusCode() == 200) {
                            if (searchProductRes.getMessage() != null && searchProductRes.getMessage().size() > 0) {

                                ArrayList<String> vinList = new ArrayList<String>();

                                for (int i = 0; i < searchProductRes.getMessage().size(); i++) {
                                    if (searchProductRes.getMessage().get(i).getProductName() != null)
                                        vinList.add(searchProductRes.getMessage().get(i).getProductName());
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
                public void onFailure(Call<SearchProductRes> call, Throwable t) {
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


}
