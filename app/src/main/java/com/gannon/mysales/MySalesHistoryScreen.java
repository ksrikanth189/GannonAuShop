package com.gannon.mysales;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gannon.R;
import com.gannon.mysales.model.MySalesHistoryReqPayLoad;
import com.gannon.mysales.model.MySalesHistoryResponsePayLoad;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.mysales.model.MySalesReqPayLoad;
import com.gannon.mysales.model.MySalesResponsePayLoad;
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

public class MySalesHistoryScreen extends SuperCompatActivity {


    private ProgressDialog m_progress;
    private Retrofit retrofit;
    private RestAPI restAPI;

    private TextView no_cat_display_id;
    private LinearLayout header_linear;
    private RecyclerView recyclerView;
    private int cargoId, barcode;
    private String message;
    private ActionBarDrawerToggle mDrawerToggle;

    private TextView auction_list, donation_list;

    private ProgressBar idPBLoading;
    private NestedScrollView idNestedSV;
    int page = 0, limit = 20;


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
        setContentView(R.layout.recycleview_activity);
        initializeUiElements();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            cargoId = 0;
            barcode = 0;

        } else {
            cargoId = extras.getInt("cargoId");
            barcode = extras.getInt("barcode");

        }


        restAPI = getRestAPIObj();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        no_cat_display_id = (TextView) findViewById(R.id.no_cat_display_id);

        auction_list = (TextView) findViewById(R.id.auction_list);
        donation_list = (TextView) findViewById(R.id.donation_list);
        idNestedSV = (NestedScrollView) findViewById(R.id.idNestedSV);
        idPBLoading = findViewById(R.id.idPBLoading);

        recyclerView.setLayoutManager(new LinearLayoutManager(MySalesHistoryScreen.this, LinearLayoutManager.VERTICAL, false));

        if (!checkInternet()) {
            CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
        } else {
            getMySalesService("auction", page, limit);
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

                    getMySalesService("auction", page, limit);

                }
            }
        });


        auction_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackgroundColor(getResources().getColor(R.color.btn_bg));

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackgroundColor(getResources().getColor(R.color.black));


                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getMySalesService("auction", page, limit);
                }

            }
        });

        donation_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                donation_list.setTextColor(getResources().getColor(R.color.white));
                donation_list.setBackgroundColor(getResources().getColor(R.color.btn_bg));

                auction_list.setTextColor(getResources().getColor(R.color.white));
                auction_list.setBackgroundColor(getResources().getColor(R.color.black));



                if (!checkInternet()) {
                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                } else {
                    getMySalesService("donation", page, limit);
                }

            }
        });


    }

    public void getMySalesService(String typestr, int page, int limit) {

        try {


            m_progress = new ProgressDialog(MySalesHistoryScreen.this);
            m_progress.setMessage("Please wait....");
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setIndeterminate(true);
            m_progress.setProgress(0);
            m_progress.setCancelable(false);
            m_progress.show();

            MySalesHistoryReqPayLoad dmgreq = new MySalesHistoryReqPayLoad();
            dmgreq.setAuctionOrDonation(typestr);
            dmgreq.setAuctionId(cargoId);
            dmgreq.setDonationId(barcode);


            Call<MySalesHistoryResponsePayLoad> damageHistoryResPayLoadCall = restAPI.getMySalesHistoryResponsePayLoadCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<MySalesHistoryResponsePayLoad>() {
                @Override
                public void onResponse(Call<MySalesHistoryResponsePayLoad> call, Response<MySalesHistoryResponsePayLoad> response) {
                    if (response.isSuccessful()) {

                        MySalesHistoryResponsePayLoad responsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(responsePayLoad);
                        Log.v("History", "History res" + gson.toJson(responsePayLoad));

                        message = responsePayLoad.getStatus();

                        if (responsePayLoad.getStatusCode() == 200 && responsePayLoad.getMessage().size() > 0) {
                            loadHistoryData(responsePayLoad);
                        } else {
                            CustomErrorToast(message);
                        }
                    }


                    if (m_progress != null && m_progress.isShowing()) {
                        m_progress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<MySalesHistoryResponsePayLoad> call, Throwable t) {

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

            CustomErrorToast("Error " + getResources().getString(R.string.server_not_responding));


        }
    }

    private void loadHistoryData(MySalesHistoryResponsePayLoad responsePayLoad) {
        if (responsePayLoad.getMessage() != null) {
            if (responsePayLoad.getMessage().size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                no_cat_display_id.setVisibility(View.GONE);
                ProductAdapter ca = new ProductAdapter(responsePayLoad, getApplicationContext());
                recyclerView.setAdapter(ca);
            } else {
                recyclerView.setVisibility(View.GONE);
                no_cat_display_id.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

        Context mContext;
        MySalesHistoryResponsePayLoad damageHistoryResPayLoad;

        public ProductAdapter(MySalesHistoryResponsePayLoad damageHistoryResPayLoad, Context context) {
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
                    inflate(R.layout.mysales_history_inflator, viewGroup, false);

            return new ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") int position) {

            productViewHolder.name_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getAuctionUser() != null ? damageHistoryResPayLoad.getMessage().get(position).getAuctionUser() : "");
            productViewHolder.date_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getAuctionDate() != null ? damageHistoryResPayLoad.getMessage().get(position).getAuctionDate() : "");
            productViewHolder.amount_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getAuctionAmount() != 0 ? damageHistoryResPayLoad.getMessage().get(position).getAuctionAmount() : 0);


//            productViewHolder.liner_ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    Intent intent = new Intent(MySalesHistoryScreen.this,MySalesEditScreen.class);
////                    intent.putExtra("cargoId",damageHistoryResPayLoad.getMessage().get(position).getAuctionId());
////                    intent.putExtra("barcode",damageHistoryResPayLoad.getMessage().get(position).getDonationId());
////                    startActivity(intent);
//
//                }
//            });


        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            protected TextView name_txt, date_txt, amount_txt;
            protected ImageView item_img;
            protected LinearLayout liner_ll;

            public ProductViewHolder(View v) {
                super(v);
                name_txt = v.findViewById(R.id.name_txt);
                date_txt = v.findViewById(R.id.date_txt);
                amount_txt = v.findViewById(R.id.amount_txt);
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
        setToolBar(getApplicationContext(), "MySales History", "yes");
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