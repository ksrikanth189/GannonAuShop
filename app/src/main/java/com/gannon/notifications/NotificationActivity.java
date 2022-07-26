package com.gannon.notifications;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gannon.R;
import com.gannon.home.HomeActivity;
import com.gannon.home.HomeListEditScreen;
import com.gannon.home.model.StatusSaveReq;
import com.gannon.notifications.model.NotificationsReq;
import com.gannon.notifications.model.NotificationsRes;
import com.gannon.notifications.model.NotificationsUpdateReq;
import com.gannon.notifications.model.NotificationsUpdateRes;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.uploadAuctionDonation.interactor.model.SaveResponsePayLoad;
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

public class NotificationActivity extends SuperCompatActivity {


    private ProgressDialog m_progress,m_progress1;
    private Retrofit retrofit;
    private RestAPI restAPI;

    private TextView no_cat_display_id;
    private LinearLayout header_linear;
    private RecyclerView recyclerView;
    private String message;
    private ActionBarDrawerToggle mDrawerToggle;

    private ProgressBar idPBLoading;
    private NestedScrollView idNestedSV;
    int page = 0, limit = 20;
    String type;



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
    protected void onResume() {
        super.onResume();

        if (!checkInternet()) {
            CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
        } else {
            getNotificationsList();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(NotificationActivity.this,HomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.recycle_activity);
        initializeUiElements();

        restAPI = getRestAPIObj();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        no_cat_display_id = (TextView) findViewById(R.id.no_cat_display_id);

        idNestedSV = (NestedScrollView) findViewById(R.id.idNestedSV);
        idPBLoading = findViewById(R.id.idPBLoading);

        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));

//        GridLayoutManager manager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(manager2);


//        if (!checkInternet()) {
//            CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
//        } else {
//            getNotificationsList();
//        }


        // adding on scroll change listener method for our nested scroll view.
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
//                    getNotificationsList("auction", page, limit);
//
//                }
//            }
//        });
    }

    public void getNotificationsList() {

        try {

            m_progress = new ProgressDialog(NotificationActivity.this);
            m_progress.setMessage("Please wait....");
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setIndeterminate(true);
            m_progress.setProgress(0);
            m_progress.setCancelable(false);
            m_progress.show();

            NotificationsReq dmgreq = new NotificationsReq();
            dmgreq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());


            Call<NotificationsRes> damageHistoryResPayLoadCall = restAPI.getNotificationsResCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<NotificationsRes>() {
                @Override
                public void onResponse(Call<NotificationsRes> call, Response<NotificationsRes> response) {
                    if (response.isSuccessful()) {

                        NotificationsRes responsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(responsePayLoad);
                        Log.v("getNotifications ", "getNotifications res" + gson.toJson(responsePayLoad));


                        if (responsePayLoad != null && responsePayLoad.getStatusCode() == 200 && responsePayLoad.getMessage().size() > 0) {
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
                public void onFailure(Call<NotificationsRes> call, Throwable t) {
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

    private void loadHistoryData(NotificationsRes responsePayLoad) {
        if (responsePayLoad.getMessage() != null) {
            if (responsePayLoad.getMessage().size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                ProductAdapter ca = new ProductAdapter(responsePayLoad, getApplicationContext());
                recyclerView.setAdapter(ca);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

        Context mContext;
        NotificationsRes damageHistoryResPayLoad;

        public ProductAdapter(NotificationsRes damageHistoryResPayLoad, Context context) {
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
                    inflate(R.layout.notification_list_inflator, viewGroup, false);

            return new ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") int position) {

            productViewHolder.item_txt.setText(damageHistoryResPayLoad.getMessage().get(position).getMessage() != null ? damageHistoryResPayLoad.getMessage().get(position).getMessage() : "");

            String url = "";

            if (damageHistoryResPayLoad.getMessage().get(position).getImageUrl() != null) {
                url = ApplicationContext.BASE_URL + "/" + damageHistoryResPayLoad.getMessage().get(position).getImageUrl().replace(".png", ".jpg");
            }

            Glide.with(NotificationActivity.this)
                    .load(url)
                    .error(R.mipmap.icon6)
                    .placeholder(R.mipmap.icon6)
                    .into(productViewHolder.item_img);


            productViewHolder.totalCount_txt.setVisibility(View.GONE);

            if (damageHistoryResPayLoad.getMessage().get(position).getStatus().equalsIgnoreCase("UNREAD")){
                productViewHolder.liner_ll.setBackgroundColor(getResources().getColor(R.color.white));
            }else {
                productViewHolder.liner_ll.setBackgroundColor(getResources().getColor(R.color.hint_color));

            }

            productViewHolder.liner_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (damageHistoryResPayLoad.getMessage().get(position).getStatus().equalsIgnoreCase("UNREAD")){
                        NotificationsUpdateReq updateReq = new NotificationsUpdateReq();
                        updateReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
                        updateReq.setAuctionId(damageHistoryResPayLoad.getMessage().get(position).getAuctionId());
                        updateReq.setDonationId(damageHistoryResPayLoad.getMessage().get(position).getDonationId());
                        updateReq.setRead(1);
                        getNotificationsUpdateService(updateReq);

                    }else {

                        NotificationsUpdateReq updateReq = new NotificationsUpdateReq();
                        updateReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
                        updateReq.setAuctionId(damageHistoryResPayLoad.getMessage().get(position).getAuctionId());
                        updateReq.setDonationId(damageHistoryResPayLoad.getMessage().get(position).getDonationId());
                        updateReq.setRead(0);
                        getNotificationsUpdateService(updateReq);

                    }
                    Intent intent = new Intent(NotificationActivity.this, HomeListEditScreen.class);
                    intent.putExtra("cargoId", damageHistoryResPayLoad.getMessage().get(position).getAuctionId());
                    intent.putExtra("barcode", damageHistoryResPayLoad.getMessage().get(position).getDonationId());
                    intent.putExtra("type", damageHistoryResPayLoad.getMessage().get(position).getAuctionOrDonation());
                    intent.putExtra("screen", "Notification");
                    intent.putExtra("productname", "Product");
                    startActivity(intent);



                }
            });


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

    private void getToggleAndSlider() {

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, (Toolbar) findViewById(R.id.toolbar),
                R.string.app_name, R.string.app_name
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    private void initializeUiElements() {

        //=== Set Tool Bar and Drawer layout ===
        setToolBar(getApplicationContext(), "Notifications", "yes");
        String pushToken = FirebaseInstanceId.getInstance().getToken();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        context = getApplicationContext();

//        setupdrawerLayout();
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


    public void getNotificationsUpdateService(NotificationsUpdateReq statusSaveReq) {

        try {


            m_progress1 = new ProgressDialog(NotificationActivity.this);
            m_progress1.setMessage("Please wait....");
            m_progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress1.setIndeterminate(true);
            m_progress1.setProgress(0);
            m_progress1.setCancelable(false);
            m_progress1.show();


            Call<NotificationsUpdateRes> damageHistoryResPayLoadCall = restAPI.getNotificationsUpdateResCall(statusSaveReq);
            damageHistoryResPayLoadCall.enqueue(new Callback<NotificationsUpdateRes>() {
                @Override
                public void onResponse(Call<NotificationsUpdateRes> call, Response<NotificationsUpdateRes> response) {
                    if (response.isSuccessful()) {

                        NotificationsUpdateRes responsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(responsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(responsePayLoad));

                        if (responsePayLoad.getStatusCode() == 200) {
//                            CustomErrorToast(responsePayLoad.getStatus());

                            if (!checkInternet()) {
                                CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                            } else {
                                getNotificationsList();
                            }
                        } else {
                            CustomErrorToast(responsePayLoad.getError());
                        }


                    }


                    if (m_progress1 != null && m_progress1.isShowing()) {
                        m_progress1.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsUpdateRes> call, Throwable t) {
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