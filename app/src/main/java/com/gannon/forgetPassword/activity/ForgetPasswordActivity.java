package com.gannon.forgetPassword.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gannon.Login.activity.LoginActivity;
import com.gannon.R;
import com.gannon.forgetPassword.interactor.ForgetPasswordInteractor;
import com.gannon.forgetPassword.interactor.ForgetPasswordInteractorInt;
import com.gannon.forgetPassword.presenter.ForgetPasswordPresenter;
import com.gannon.forgetPassword.view.ForgetPasswordView;
import com.gannon.splash.activity.SplashActivity;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;


public class ForgetPasswordActivity extends SuperCompatActivity implements ForgetPasswordView {

    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private ForgetPasswordPresenter adminForgetPasswordPresenter;
    private EditText forget_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_forget_password);

        initializeUiElements();

    }

    @Override
    public void serviceSuccess(String string) {


          CustomErrorToast(string);
//        CustomOKAlertDialog(string);
//        navigateToLoginScreen();
//        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT);
//        toast.show();


    }


    public void CustomOKAlertDialog(String msg) {

        final Dialog dialogComp = new Dialog(ForgetPasswordActivity.this);
        dialogComp.setCancelable(true);
        dialogComp.setCanceledOnTouchOutside(false);
        dialogComp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComp.setContentView(R.layout.ok_alert_dialog);
        dialogComp.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final LinearLayout ok_ll = (LinearLayout) dialogComp.findViewById(R.id.ok_ll);
        final Button ok_dialog = (Button) dialogComp.findViewById(R.id.ok_dialog);

        final TextView error_msg_txt = (TextView) dialogComp.findViewById(R.id.error_msg_txt);

        error_msg_txt.setText(msg);

        dialogComp.show();


        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));

                dialogComp.dismiss();
            }
        });

        ok_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComp.dismiss();

            }
        });

    }


    @Override
    public void serviceFailed(String str) {
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.show();

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
        CustomSuccessToast(str);
    }

    @Override
    public void closeApp(String str) {
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void showValidationToast(String str) {
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void navigateToLoginScreen() {

        startActivity(new Intent(this, SplashActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void submit(View view) {

        if (forget_password.getText().toString().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getText(R.string.pls_enter_mobile), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            if (checkInternet()) {
                adminForgetPasswordPresenter.forgetBtnClicked(forget_password.getText().toString());
            } else {
                showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));

            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        restAPI = getRestAPIObj();
        ForgetPasswordInteractorInt adminForgetPasswordInteractorInt = new ForgetPasswordInteractor(context, restAPI);
        adminForgetPasswordPresenter = new ForgetPasswordPresenter(this, adminForgetPasswordInteractorInt);
    }


    private void initializeUiElements() {

        setToolBar(getApplicationContext(), "Forget Password", "yes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        context = this;
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);

        forget_password = findViewById(R.id.forget_password);

        ForgetPasswordInteractorInt adminForgetPasswordInteractorInt = new ForgetPasswordInteractor(context, restAPI);
        adminForgetPasswordPresenter = new ForgetPasswordPresenter(this, adminForgetPasswordInteractorInt);

    }


}
