package com.gannon.mywins;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gannon.R;
import com.gannon.mysales.MySalesEditScreen;
import com.gannon.mysales.MySalesScreen;
import com.gannon.mywins.model.MyWinsReqPayLoad;
import com.gannon.mywins.model.MyWinsResponsePayLoad;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.utils.ApplicationContext;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by Srikanth.K on
 */

public class MyWinsScreen extends SuperCompatActivity {


    private ProgressDialog m_progress;
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
    String type;
    LinearLayout approve_deny_ll;



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
        setContentView(R.layout.mywins_recycle_activity);
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
        approve_deny_ll = findViewById(R.id.approve_deny_ll);

//        recyclerView.setLayoutManager(new LinearLayoutManager(MyWinsScreen.this, LinearLayoutManager.VERTICAL, false));
//        donation_recycler_view.setLayoutManager(new LinearLayoutManager(MyWinsScreen.this, LinearLayoutManager.VERTICAL, false));

        GridLayoutManager manager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager2);

        GridLayoutManager manager3 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        donation_recycler_view.setLayoutManager(manager3);


        approve_deny_ll.setVisibility(View.GONE);

        if (!checkInternet()) {
            CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
        } else {
            getMyWinsService("auction", page, limit);
        }


        // adding on scroll change listener method for our nested scroll view.
        idNestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    idPBLoading.setVisibility(View.VISIBLE);

                    getMyWinsService("auction", page, limit);

                }
            }
        });


        auction_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donation_recycler_view.setVisibility(View.GONE);

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackgroundColor(getResources().getColor(R.color.btn_bg));

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackgroundColor(getResources().getColor(R.color.black));


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getMyWinsService("auction", page, limit);
                }

            }
        });

        donation_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackgroundColor(getResources().getColor(R.color.btn_bg));

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackgroundColor(getResources().getColor(R.color.black));


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getMyWinsService("donation", page, limit);
                }

            }
        });


    }

    public void getMyWinsService(String typestr, int page, int limit) {

        try {


            m_progress = new ProgressDialog(MyWinsScreen.this);
            m_progress.setMessage("Please wait....");
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setIndeterminate(true);
            m_progress.setProgress(0);
            m_progress.setCancelable(false);
            m_progress.show();

            MyWinsReqPayLoad dmgreq = new MyWinsReqPayLoad();
            dmgreq.setAuctionOrDonation(typestr);
            dmgreq.setOffset(page);
            dmgreq.setLimit(limit);
            dmgreq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());


            Call<MyWinsResponsePayLoad> damageHistoryResPayLoadCall = restAPI.getMyWinsResponsePayLoadCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<MyWinsResponsePayLoad>() {
                @Override
                public void onResponse(Call<MyWinsResponsePayLoad> call, Response<MyWinsResponsePayLoad> response) {
                    if (response.isSuccessful()) {

                        MyWinsResponsePayLoad responsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(responsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(responsePayLoad));

                        message = responsePayLoad.getStatus();

                        if (responsePayLoad.getStatusCode() == 200 && responsePayLoad.getMessage().size() > 0) {
                            loadHistoryData(responsePayLoad);
                        } else {
                            CustomErrorToast("No data found");
                        }
                    }


                    if (m_progress != null && m_progress.isShowing()) {
                        m_progress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<MyWinsResponsePayLoad> call, Throwable t) {
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

        }
    }

    private void loadHistoryData(MyWinsResponsePayLoad responsePayLoad) {
        if (responsePayLoad.getMessage() != null) {
            if (responsePayLoad.getMessage().size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                donation_recycler_view.setVisibility(View.VISIBLE);
                ProductAdapter ca = new ProductAdapter(responsePayLoad, getApplicationContext());
                recyclerView.setAdapter(ca);
            } else {
                recyclerView.setVisibility(View.GONE);
                donation_recycler_view.setVisibility(View.GONE);
            }
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

        Context mContext;
        MyWinsResponsePayLoad damageHistoryResPayLoad;

        public ProductAdapter(MyWinsResponsePayLoad damageHistoryResPayLoad, Context context) {
            this.damageHistoryResPayLoad = damageHistoryResPayLoad;
            mContext = context;
        }

        @Override
        public int getItemCount() {

            return damageHistoryResPayLoad.getMessage().size();

        }


        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.auction_donations_list_inflator, viewGroup, false);

            return new ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") int position) {

            productViewHolder.item_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getProductName() != null ? damageHistoryResPayLoad.getMessage().get(position).getProductName() : "");
            productViewHolder.totalCount_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getTotalCount().toString());

            String url = "";

            if (damageHistoryResPayLoad.getMessage().get(position).getImageUrl() != null) {
                url = ApplicationContext.BASE_URL + "/" + damageHistoryResPayLoad.getMessage().get(position).getImageUrl().replace(".png", ".jpg");
            }

            Glide.with(MyWinsScreen.this)
//                    .load("http://192.168.1.207:8080/img/mob.jpg")
                    .load(url)
                    .error(R.mipmap.icon6)
                    .placeholder(R.mipmap.icon6)
                    .into(productViewHolder.item_img);


            productViewHolder.totalCount_txt.setVisibility(View.GONE);

//            productViewHolder.liner_ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MyWinsScreen.this, MySalesEditScreen.class);
//                    intent.putExtra("cargoId", damageHistoryResPayLoad.getMessage().get(position).getAuctionId());
//                    intent.putExtra("barcode", damageHistoryResPayLoad.getMessage().get(position).getDonationId());
//                    intent.putExtra("type", type);
//                    startActivity(intent);
//                }
//            });


        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            protected TextView item_txt,totalCount_txt;
            protected ImageView item_img;
            protected LinearLayout liner_ll;

            public ProductViewHolder(View v) {
                super(v);
                item_txt = v.findViewById(R.id.item_txt);
                totalCount_txt = v.findViewById(R.id.totalCount_txt);
                item_img = v.findViewById(R.id.item_img);
                liner_ll = v.findViewById(R.id.liner_ll);


            }
        }

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

    private void initializeUiElements() {

        //=== Set Tool Bar and Drawer layout ===
        setToolBar(getApplicationContext(), "My Wins", "yes");
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


    }


}