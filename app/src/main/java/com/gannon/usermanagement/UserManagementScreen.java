package com.gannon.usermanagement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.gannon.home.HomeListEditScreen;
import com.gannon.home.model.ApprovedDenySaveRes;
import com.gannon.home.model.ApprovedSaveReq;
import com.gannon.home.model.DenySaveReq;
import com.gannon.home.model.HomeApproveListRes;
import com.gannon.home.model.HomeDenyListRes;
import com.gannon.home.model.NotificationsCountReq;
import com.gannon.home.model.NotificationsCountRes;
import com.gannon.home.model.SearchProductReq;
import com.gannon.home.model.SearchProductRes;
import com.gannon.mysales.MySalesEditScreen;
import com.gannon.mysales.MySalesScreen;
import com.gannon.mysales.model.MySalesReqPayLoad;
import com.gannon.mysales.model.MySalesResponsePayLoad;
import com.gannon.mywins.MyWinsScreen;
import com.gannon.notifications.NotificationActivity;
import com.gannon.notifications.UserNotificationActivity;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.uploadAuctionDonation.activity.NewAuctionDonation;
import com.gannon.utils.ApplicationContext;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;
import com.gannon.webview.WebViewImageUpload;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by Srikanth.K on
 */

//public class UserManagementScreen extends SuperCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
public class UserManagementScreen extends SuperCompatActivity {


    private ProgressDialog m_progress, progressDialog, progressDialog1, progressDialog2, m_progress1,m_progress3;
    private Retrofit retrofit;
    private RestAPI restAPI;

    private TextView no_cat_display_id;
    private LinearLayout header_linear;
    private RecyclerView recyclerView, donation_recycler_view;
    private String cargoId = null, barcode = null;
    private String message;
    private ActionBarDrawerToggle mDrawerToggle;

    private TextView auction_list, donation_list;

    private ProgressBar idPBLoading;
    private ScrollView idNestedSV;
    int page = 0, limit = 20;

    private HomeApproveListRes homeCategorysListRes;

    private ImageView logout_img, search_imag, donat_search_imag;
    private AutoCompleteTextView autocomp_search, autocomp_donat_search;
    private UserMangSearchRes searchProductRes;
    private String typeStr = "auction";
    private LinearLayout auction_search_ll, donation_search_ll,notifica_ll;

    private TextView notifica_value;

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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.usermanagement_list_activity);
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
        idNestedSV = findViewById(R.id.idNestedSV);
        idPBLoading = findViewById(R.id.idPBLoading);

        auction_list.setText("Newly Registered");
        donation_list.setText("Registered Users");

        recyclerView.setLayoutManager(new LinearLayoutManager(UserManagementScreen.this, LinearLayoutManager.VERTICAL, false));
        donation_recycler_view.setLayoutManager(new LinearLayoutManager(UserManagementScreen.this, LinearLayoutManager.VERTICAL, false));

        if (!checkInternet()) {
            CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
        } else {
            UserMangSearchReq mangSearchReq = new UserMangSearchReq();
            mangSearchReq.setSearchValue(null);
            getCategoryList(mangSearchReq);
        }


        auction_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typeStr = "auction";

                auction_search_ll.setVisibility(View.VISIBLE);
                donation_search_ll.setVisibility(View.GONE);
                donation_recycler_view.setVisibility(View.GONE);

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));

                autocomp_search.setText("");
                autocomp_search.setAdapter(null);


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    UserMangSearchReq mangSearchReq = new UserMangSearchReq();
                    mangSearchReq.setSearchValue(null);
                    getCategoryList(mangSearchReq);
                }

            }
        });

        donation_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typeStr = "donation";


                auction_search_ll.setVisibility(View.GONE);
                donation_search_ll.setVisibility(View.VISIBLE);

                recyclerView.setVisibility(View.GONE);

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackground(getResources().getDrawable(R.drawable.rect_background_merron));

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackground(getResources().getDrawable(R.drawable.rect_background_black));

                autocomp_donat_search.setText("");
                autocomp_donat_search.setAdapter(null);


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    UserMangSearchReq mangSearchReq = new UserMangSearchReq();
                    mangSearchReq.setSearchValue(null);
                    getHomeDenyListResCall(mangSearchReq);
                }
            }
        });
    }

    private void getToggleAndSlider() {

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, (Toolbar) findViewById(R.id.toolbar),
                R.string.app_name, R.string.app_name
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    private void initializeUiElements() {
//
        //=== Set Tool Bar and Drawer layout ===
        setToolBar(getApplicationContext(), "User Management", "yes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
////
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//        navHeader = navigationView.getHeaderView(0);
//        name_txt = navHeader.findViewById(R.id.name_txt);


//        if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getUserName() != null) {
//            name_txt.setText(SharedPrefHelper.getLogin(context).getMessage().getUserName());
//        }


        context = getApplicationContext();

        setupdrawerLayout();
        getToggleAndSlider();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        //========================================
        context = getApplicationContext();
        restAPI = getRestAPIObj();


//        hideLeftMenuItem();


        autocomp_search = findViewById(R.id.autocomp_search);
        autocomp_donat_search = findViewById(R.id.autocomp_donat_search);
        logout_img = findViewById(R.id.logout_img);
        search_imag = findViewById(R.id.search_imag);
        donat_search_imag = findViewById(R.id.donat_search_imag);

        auction_search_ll = findViewById(R.id.auction_search_ll);
        donation_search_ll = findViewById(R.id.donation_search_ll);
        notifica_ll = findViewById(R.id.notifica_ll);
        notifica_ll.setVisibility(View.VISIBLE);
        notifica_value = findViewById(R.id.notifica_value);

        logout_img.setVisibility(View.VISIBLE);

        logout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutDialog();
            }
        });


        autocomp_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                donation_recycler_view.setAdapter(null);

//                if (s.length() == 0) {
//
//                    if (!checkInternet()) {
//                        CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
//                    } else {
//                        UserMangSearchReq mangSearchReq = new UserMangSearchReq();
//                        mangSearchReq.setSearchValue(autocomp_search.getText().toString().trim());
//                        getCategoryList(mangSearchReq);
//                    }
//                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (autocomp_search.isPerformingCompletion()) {
                    // An item has been selected from the list. Ignore.
                    return;
                } else {
//                    getSearchService("Newly", autocomp_search.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        autocomp_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autocomp_search.dismissDropDown();
                hideKeyboard();
            }
        });


        autocomp_donat_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                recyclerView.setAdapter(null);

//                if (s.length() == 0) {
//
//                    if (!checkInternet()) {
//                        CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
//                    } else {
//                        UserMangSearchReq mangSearchReq = new UserMangSearchReq();
//                        mangSearchReq.setSearchValue(autocomp_donat_search.getText().toString().trim());
//                        getHomeDenyListResCall(mangSearchReq);
//                    }
//                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (autocomp_donat_search.isPerformingCompletion()) {
                    // An item has been selected from the list. Ignore.
                    return;
                } else {
//                    getSearchService("Old", autocomp_donat_search.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        autocomp_donat_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autocomp_donat_search.dismissDropDown();
                hideKeyboard();
            }
        });


        search_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    UserMangSearchReq mangSearchReq = new UserMangSearchReq();
                    mangSearchReq.setSearchValue(autocomp_search.getText().toString().trim());
                    getCategoryList(mangSearchReq);
                }
            }
        });

        donat_search_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    UserMangSearchReq mangSearchReq = new UserMangSearchReq();
                    mangSearchReq.setSearchValue(autocomp_donat_search.getText().toString().trim());
                    getHomeDenyListResCall(mangSearchReq);
                }

            }
        });


        NotificationsCountReq countReq = new NotificationsCountReq();
        countReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
        getNotificationsCountService(countReq);


        notifica_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserManagementScreen.this, UserNotificationActivity.class));
            }
        });
    }

    public void getCategoryList(UserMangSearchReq mangSearchReq) {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgress(0);
            progressDialog.show();


            final Call<HomeApproveListRes> adminSlideResCall = restAPI.getUserMangApproveListResCall(mangSearchReq);
            adminSlideResCall.enqueue(new Callback<HomeApproveListRes>() {
                @Override
                public void onResponse(Call<HomeApproveListRes> call, Response<HomeApproveListRes> response) {
                    if (response.isSuccessful()) {
                        homeCategorysListRes = response.body();
                        if (homeCategorysListRes.getStatusCode() == 200 && homeCategorysListRes.getStatus().equalsIgnoreCase("success")) {

                            if (homeCategorysListRes.getMessage().size() > 0) {
                                loadCategDaoryData(homeCategorysListRes);
                            } else {
                                recyclerView.setAdapter(null);
                            }

                        } else {
                            CustomErrorToast(homeCategorysListRes.getError());
                            recyclerView.setAdapter(null);
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

    public void getHomeDenyListResCall(UserMangSearchReq searchReq) {
        try {
            progressDialog1 = new ProgressDialog(this);
            progressDialog1.setMessage("Please Wait......");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog1.setIndeterminate(true);
            progressDialog1.setProgress(0);
            progressDialog1.show();


            final Call<HomeDenyListRes> adminSlideResCall = restAPI.getUserMangDenyListResCall(searchReq);
            adminSlideResCall.enqueue(new Callback<HomeDenyListRes>() {
                @Override
                public void onResponse(Call<HomeDenyListRes> call, Response<HomeDenyListRes> response) {
                    if (response.isSuccessful()) {
                        HomeDenyListRes homeCategorysListRes = response.body();
                        if (homeCategorysListRes.getStatusCode() == 200 && homeCategorysListRes.getStatus().equalsIgnoreCase("success")) {

                            if (homeCategorysListRes.getMessage().size() > 0) {
                                loadProductData(homeCategorysListRes);
                            } else {
                                donation_recycler_view.setAdapter(null);
                            }


//                            ArrayList<String> vinList = new ArrayList<String>();
//                            for (int i = 0; i < homeCategorysListRes.getMessage().size(); i++) {
//                                if (homeCategorysListRes.getMessage().get(i).getFirstName() != null)
//                                    vinList.add(homeCategorysListRes.getMessage().get(i).getFirstName());
//                            }
//
//                            if (vinList != null) {
//                                if (vinList.size() != 0) {
//                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                                            context,
//                                            R.layout.search_text_inflator, R.id.vinId, vinList);
//                                    autocomp_search.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                }
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
        logoutDialog();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        // getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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

            productViewHolder.firstname_txt.setText(": " + cartListRes.get(position).getFirstName() + "   " + cartListRes.get(position).getLastName());
//            productViewHolder.lastName_txt.setText(cartListRes.get(position).getLastName() != null
//                    ? cartListRes.get(position).getLastName() : "");
            productViewHolder.email_txt.setText(": " + cartListRes.get(position).getEmail() != null
                    ? ": " + cartListRes.get(position).getEmail() : "");

//            productViewHolder.phoneNumber_txt.setText(": " + cartListRes.get(position).getPhoneNumber() != null
//                    ? ": " + cartListRes.get(position).getPhoneNumber() : "");

            productViewHolder.studentId_txt.setText(": " + cartListRes.get(position).getStudentId() != null
                    ? ": " + cartListRes.get(position).getStudentId() : "");


            productViewHolder.approve_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkInternet()) {

                        ApprovedSaveReq approvedSaveReq = new ApprovedSaveReq();
                        approvedSaveReq.setApproved("Y");
                        approvedSaveReq.setRegistrationId(cartListRes.get(position).getRegistrationId());
                        approvedSaveReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
                        approvedSaveReq.setDenyReason(productViewHolder.denyReason_txt.getText().toString().trim());
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
                        approvedSaveReq.setDenyReason(productViewHolder.denyReason_txt.getText().toString().trim());
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

            protected TextView firstname_txt, lastName_txt, email_txt, phoneNumber_txt, studentId_txt;
            protected Button approve_btn, deny_btn;
            protected EditText denyReason_txt;

            public ProductViewHolder(View v) {
                super(v);
                studentId_txt = (TextView) v.findViewById(R.id.studentId_txt);
                firstname_txt = (TextView) v.findViewById(R.id.firstname_txt);
                lastName_txt = (TextView) v.findViewById(R.id.lastName_txt);
                email_txt = (TextView) v.findViewById(R.id.email_txt);
                phoneNumber_txt = (TextView) v.findViewById(R.id.phoneNumber_txt);
                approve_btn = (Button) v.findViewById(R.id.approve_btn);
                deny_btn = (Button) v.findViewById(R.id.deny_btn);
                denyReason_txt =  v.findViewById(R.id.denyReason_txt);
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

            productViewHolder.firstname_txt.setText(": " + cartListRes.get(position).getFirstName() + " " + cartListRes.get(position).getLastName());
//            productViewHolder.lastName_txt.setText(": "+cartListRes.get(position).getLastName() != null
//                    ? cartListRes.get(position).getLastName() : "");

            productViewHolder.email_txt.setText(": " + cartListRes.get(position).getEmail() != null
                    ? ": " + cartListRes.get(position).getEmail() : "");

            productViewHolder.studentId_txt.setText(": " + cartListRes.get(position).getStudentId() != null
                    ? ": " + cartListRes.get(position).getStudentId() : "");

//            productViewHolder.phoneNumber_txt.setText(": " + cartListRes.get(position).getPhoneNumber() != null
//                    ? ": " + cartListRes.get(position).getPhoneNumber() : "");
            productViewHolder.denyReason_txt.setText(cartListRes.get(position).getDenyReason() != null
                    ? ": " + cartListRes.get(position).getDenyReason() : ": ");

            if (cartListRes.get(position).getStatus().equalsIgnoreCase("Activated")) {
                productViewHolder.deny_btn.setText("Deactivate");
                productViewHolder.deny_btn.setBackgroundColor(getResources().getColor(R.color.btn_bg));
            } else if (cartListRes.get(position).getStatus().equalsIgnoreCase("Deactivated")) {
                productViewHolder.deny_btn.setText("Activate");
                productViewHolder.deny_btn.setBackgroundColor(getResources().getColor(R.color.green));

            }

            productViewHolder.deny_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkInternet()) {

                        DenySaveReq approvedSaveReq = new DenySaveReq();
                        if (cartListRes.get(position).getStatus().equalsIgnoreCase("Activated")) {
                            approvedSaveReq.setApproved("N");
                        } else {
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

            protected TextView firstname_txt, lastName_txt, email_txt, phoneNumber_txt, studentId_txt,denyReason_txt;
            protected Button approve_btn, deny_btn;

            public ProductViewHolder(View v) {
                super(v);
                studentId_txt = (TextView) v.findViewById(R.id.studentId_txt);
                firstname_txt = (TextView) v.findViewById(R.id.firstname_txt);
                lastName_txt = (TextView) v.findViewById(R.id.lastName_txt);
                email_txt = (TextView) v.findViewById(R.id.email_txt);
                phoneNumber_txt = (TextView) v.findViewById(R.id.phoneNumber_txt);
                approve_btn = (Button) v.findViewById(R.id.approve_btn);
                deny_btn = (Button) v.findViewById(R.id.deny_btn);
                denyReason_txt = v.findViewById(R.id.denyReason_txt);
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

                            startActivity(new Intent(UserManagementScreen.this, UserManagementScreen.class));
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


                            startActivity(new Intent(UserManagementScreen.this, UserManagementScreen.class));
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

    public void getSearchService(String typeStr, String searchVal) {

        try {


//            m_progress = new ProgressDialog(HomeActivity.this);
//            m_progress.setMessage("Please wait....");
//            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            m_progress.setIndeterminate(true);
//            m_progress.setProgress(0);
//            m_progress.setCancelable(false);
//            m_progress.show();

            UserMangAuctionSearchReq dmgreq = new UserMangAuctionSearchReq();

            dmgreq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId().toString());
            dmgreq.setSearchValue(searchVal);
            dmgreq.setType(typeStr);


            Call<UserMangSearchRes> damageHistoryResPayLoadCall = restAPI.getUserMangSearchResCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<UserMangSearchRes>() {
                @Override
                public void onResponse(Call<UserMangSearchRes> call, Response<UserMangSearchRes> response) {
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
                                        autocomp_search.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }
                                }


                                ArrayList<String> vinList1 = new ArrayList<String>();
                                for (int i = 0; i < searchProductRes.getMessage().size(); i++) {
                                    if (searchProductRes.getMessage().get(i) != null)
                                        vinList1.add(searchProductRes.getMessage().get(i));
                                }
                                if (vinList1 != null) {
                                    if (vinList1.size() != 0) {
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                                context,
                                                R.layout.search_text_inflator, R.id.vinId, vinList1);
                                        autocomp_donat_search.setAdapter(adapter);
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
                public void onFailure(Call<UserMangSearchRes> call, Throwable t) {
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


            m_progress3 = new ProgressDialog(UserManagementScreen.this);
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