package com.gannon.splash.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.gannon.BuildConfig;
import com.gannon.Login.activity.LoginActivity;
import com.gannon.Login.activity.LoginSettings;
import com.gannon.R;
import com.gannon.Register.activity.RegisterActivity;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.splash.interactor.model.CheckForUpdateRes;
import com.gannon.splash.presenter.SplashPresenter;
import com.gannon.splash.view.SplashView;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;


/**
 * Created by AndSri on JUNE 2022
 */

public class SplashActivity2 extends SuperCompatActivity implements SplashView {


    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);


        context = getApplicationContext();
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);


    }


    public void signIn(View view) {

//        startActivity(new Intent(SplashActivity2.this, HomeActivity.class));
        startActivity(new Intent(SplashActivity2.this, LoginActivity.class));
        finish();


    }

    public void REGISTER(View view) {
//        startActivity(new Intent(SplashActivity2.this, MySalesScreen.class));
        startActivity(new Intent(SplashActivity2.this, RegisterActivity.class));
        finish();


    }

    public void settings_txt(View view) {
        startActivity(new Intent(SplashActivity2.this, LoginSettings.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void serviceFailed(String str) {
        CustomErrorToast(str);
    }


    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showSuccessToast(String str) {
        CustomErrorToast(str);

    }

    @Override
    public void showValidationToast(String str) {
        CustomErrorToast(str);

    }

    @Override
    public void navigateToHomeScreen() {

//
//     startActivity(new Intent(SplashActivity.this, MainHomeActivity.class));
//     finish();

        if (SharedPrefHelper.getLogin(context) == null) {
            startActivity(new Intent(SplashActivity2.this, LoginActivity.class));
            finish();

        } else {
//            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();

        }


    }

    @Override
    public void closeApp(String str) {

    }

    @Override
    public void serviceSuccess(String str) {


        navigateToHomeScreen();

    }

    @Override
    public void serviceUpdateSuccess(CheckForUpdateRes checkForUpdateRes) {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        if (checkForUpdateRes.getVersion() != null &&
                !versionName.equals(checkForUpdateRes.getVersion().get(0).getVersion())) {
            updatePopup(checkForUpdateRes.getVersion().get(0).getUrl());
        } else {
            navigateToHomeScreen();
        }

    }

    /*version Dialog*/

    Dialog fbDialogue;

    public void updatePopup(final String play_url) {
        fbDialogue = new Dialog(SplashActivity2.this, android.R.style.Theme_Black_NoTitleBar);
        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.popup_version_update_layout);

        fbDialogue.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    finish();
                return false;
            }
        });

        Button update_textView = fbDialogue.findViewById(R.id.update_textView);
        update_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(play_url)));


            }
        });


        fbDialogue.setCancelable(false);
        fbDialogue.show();

    }

}

