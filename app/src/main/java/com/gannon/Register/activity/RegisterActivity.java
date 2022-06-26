package com.gannon.Register.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gannon.Login.activity.LoginActivity;
import com.gannon.R;
import com.gannon.Register.interactor.RegisterInteractor;
import com.gannon.Register.interactor.model.RegisterActivityReq;
import com.gannon.Register.interactor.model.RegisterActivityRes;
import com.gannon.Register.interactor.model.VerifyOtpReq;
import com.gannon.Register.interactor.model.VerifyOtpRes;
import com.gannon.Register.presenter.RegisterPresenter;
import com.gannon.Register.view.RegisterView;
import com.gannon.splash.activity.SplashActivity2;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends SuperCompatActivity implements RegisterView {
    private Context context;
    private EditText email_edt;
    private EditText mobile_edt;
    private EditText name_edt;
    private EditText password,studentId;
    private ProgressDialog progressDialog;
    private RegisterActivityReq registerActivityReq;
    private RestAPI restAPI;
    private RegisterPresenter userRegisterPresenter;
    Dialog dialog1;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_register);
        initializeUiElements();
    }

    public void serviceSuccess(RegisterActivityRes registerActivityRes) {
        if (registerActivityRes.getStatusCode()== 200) {
//            userDoctorRegisterPresenter.saveRegisterLogin(registerActivityRes);
            Toast.makeText(getApplicationContext(), registerActivityRes.getMessage(), Toast.LENGTH_SHORT).show();
//            customOTPDialog();

            startActivity(new Intent(RegisterActivity.this,SplashActivity2.class));
            return;
        }
        serviceFailed(context.getResources().getString(R.string.server_not_responding));
    }

    public void serviceFailed(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showSuccessToast(String str) {
        Context context2 = context;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(str);
        Toast.makeText(context2, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    public void closeApp(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void showValidationToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void navigateToHomeScreen() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void register(View view) {
        if (name_edt.getText().toString().length() == 0) {
            CustomErrorToast(getResources().getString(R.string.pls_enter_first_name));
        } else if (email_edt.getText().toString().length() == 0) {
            CustomErrorToast(getResources().getString(R.string.pls_enter_email));
        } else if (mobile_edt.getText().toString().length() == 0) {
            CustomErrorToast(getResources().getString(R.string.pls_enter_mobile));
        } else if (password.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.pls_enter_password), Toast.LENGTH_SHORT).show();
        } else if (studentId.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.pls_enter_password), Toast.LENGTH_SHORT).show();
        } else {
            RegisterActivityReq registerActivityReq2 = new RegisterActivityReq();
            registerActivityReq2.setFirstName(name_edt.getText().toString().trim());
            registerActivityReq2.setLastName(name_edt.getText().toString().trim());
            registerActivityReq2.setEmail(email_edt.getText().toString().trim());
            registerActivityReq2.setPhoneNumber(mobile_edt.getText().toString().trim());
            registerActivityReq2.setPassword(password.getText().toString().trim());
            registerActivityReq2.setStudentId(studentId.getText().toString().trim());
//            registerActivityReq2.setToken(FirebaseInstanceId.getInstance().getToken());
            if (checkInternet()) {
                userRegisterPresenter.signInBtnClicked(registerActivityReq2);
            } else {
                showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));
            }
        }
    }

    public void back_BTN(View view){
        startActivity(new Intent(this, SplashActivity2.class));
    }
    public void cancel_BTN(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        restAPI = getRestAPIObj();
        userRegisterPresenter = new RegisterPresenter(this, new RegisterInteractor(context, restAPI));
    }

    private void initializeUiElements() {
        context = this;
        setToolBar(getApplicationContext(), "Create Account", "yes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        if (VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);
        name_edt = (EditText) findViewById(R.id.name_edt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        mobile_edt = (EditText) findViewById(R.id.mobile_edt);
        password = (EditText) findViewById(R.id.password);
        studentId = (EditText) findViewById(R.id.studentId);
        userRegisterPresenter = new RegisterPresenter(this, new RegisterInteractor(context, restAPI));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        finish();

        startActivity(new Intent(RegisterActivity.this, SplashActivity2.class));

    }

    public void customOTPDialog() {

        dialog1 = new Dialog(RegisterActivity.this);
        dialog1.setContentView(R.layout.otp_dialog);

        TextView mobile_txt = (TextView) dialog1.findViewById(R.id.mobile_txt);
        final EditText otp_txt = (EditText) dialog1.findViewById(R.id.otp_txt);
        Button resend_otp = (Button) dialog1.findViewById(R.id.resend_otp);
        Button submit_button = (Button) dialog1.findViewById(R.id.submit_button);

        mobile_txt.setText(mobile_edt.getText().toString());

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

                if (checkInternet()) {
                    VerifyOtpReq verifyOtpReq = new VerifyOtpReq();
                    verifyOtpReq.setMobile(mobile_edt.getText().toString());
                    verifyOtpReq.setOtp(otp_txt.getText().toString());
                    callVerifyOtp(verifyOtpReq);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.plz_chk_your_net), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.setCancelable(false);
        dialog1.show();

    }



    private void callVerifyOtp(final VerifyOtpReq verifyOtpReq) {

        progressDialog.show();

        try {
            final Call<VerifyOtpRes> loginActivityResCall = restAPI.getVerifyOtpResCall(verifyOtpReq);
            loginActivityResCall.enqueue(new Callback<VerifyOtpRes>() {
                @Override
                public void onResponse(Call<VerifyOtpRes> call, Response<VerifyOtpRes> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        VerifyOtpRes VerifyOtpRes = response.body();
                        if (VerifyOtpRes.getStatus()== 200) {
                            CustomSuccessToast(VerifyOtpRes.getMessage());
                            navigateToHomeScreen();
                        } else {
                            CustomErrorToast(VerifyOtpRes.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<VerifyOtpRes> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            CustomErrorToast(getResources().getString(R.string.server_not_responding));
        }
    }

}
