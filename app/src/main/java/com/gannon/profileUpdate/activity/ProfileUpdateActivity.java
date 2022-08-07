package com.gannon.profileUpdate.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gannon.Login.activity.LoginActivity;
import com.gannon.R;
import com.gannon.home.HomeActivity;
import com.gannon.profileUpdate.interactor.ProfileUpdateInteractor;
import com.gannon.profileUpdate.interactor.ProfileUpdateInteractorInt;
import com.gannon.profileUpdate.interactor.model.ProfileGetReq;
import com.gannon.profileUpdate.interactor.model.ProfileGetRes;
import com.gannon.profileUpdate.interactor.model.ProfileUpdateReq;
import com.gannon.profileUpdate.presenter.ProfileUpdatePresenter;
import com.gannon.profileUpdate.view.ProfileUpdateView;
import com.gannon.sharedpref.ObscuredSharedPreferences;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileUpdateActivity extends SuperCompatActivity implements ProfileUpdateView {

    String selectedVal, loginType;
    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private ProfileUpdatePresenter adminProfileUpdatePresenter;
    private EditText dob_edt, address_edt;
    private EditText firstname_edt, lastname_edt, email_edt, mobile_edt, password_edt;
    private RadioButton radioMale, radioFemale, radioOthers;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_profile_update);

        initializeUiElements();

        if (checkInternet()) {

            if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null) {
                ProfileGetReq profileGetReq = new ProfileGetReq();
                profileGetReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
                callProfileGet(profileGetReq);
            }
        } else {
            CustomErrorToast(getResources().getString(R.string.server_not_responding));
        }
    }

    @Override
    public void serviceSuccess(String string) {

        navigateToHomeScreen();
        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT);
        toast.show();


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
    public void navigateToHomeScreen() {
        startActivity(new Intent(this, ProfileUpdateActivity.class));
//        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void submit(View view) {

       /* if (mobile_edt.getText().toString().length() == 0) {
            CustomErrorToast(getResources().getString(R.string.pls_enter_mobile));
        } else*/
        if (password_edt.getText().toString().length() == 0) {
            CustomErrorToast(getResources().getString(R.string.pls_enter_password));
        } else {
            if (checkInternet()) {

                ProfileUpdateReq profileUpdateReq = new ProfileUpdateReq();
                profileUpdateReq.setUserId(SharedPrefHelper.getLogin(context).getMessage().getUserId());
                profileUpdateReq.setPhoneNumber(mobile_edt.getText().toString().trim());
                profileUpdateReq.setPassword(password_edt.getText().toString().trim());
//                profileUpdateReq.setName(name_txt.getText().toString().trim());
//
//                RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
//                profileUpdateReq.setGender(rb.getText().toString());
//                profileUpdateReq.setAddress(address_edt.getText().toString());
//
//                profileUpdateReq.setType(loginType);
                adminProfileUpdatePresenter.changePasssBtnClicked(profileUpdateReq);
            } else {
                showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));

            }
        }
    }

    public void Back(View view) {
        startActivity(new Intent(ProfileUpdateActivity.this, HomeActivity.class));
    }

    @Override
    protected void onResume() {

        super.onResume();
        restAPI = getRestAPIObj();
        ProfileUpdateInteractorInt adminProfileUpdateInteractorInt = new ProfileUpdateInteractor(context, restAPI);
        adminProfileUpdatePresenter = new ProfileUpdatePresenter(this, adminProfileUpdateInteractorInt);
    }

    private void initializeUiElements() {

        setToolBar(getApplicationContext(), "Profile Update", "yes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);


        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "loginType", Context.MODE_PRIVATE);
        loginType = prefsLogin.getString("loginType", "");

        firstname_edt = findViewById(R.id.firstname_edt);
        lastname_edt = findViewById(R.id.lastname_edt);
        email_edt = findViewById(R.id.email_edt);
        mobile_edt = findViewById(R.id.mobile_edt);
        password_edt = findViewById(R.id.password_edt);
        dob_edt = findViewById(R.id.dob_edt);
        address_edt = findViewById(R.id.address_edt);

        radioGroup = findViewById(R.id.radioGroup);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioOthers = findViewById(R.id.radioOthers);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    selectedVal = rb.getText().toString();

                }

            }
        });

//        name_txt.setText(SharedPrefHelper.getLogin(context).getResult().getName() != null ? SharedPrefHelper.getLogin(context).getResult().getName() : "");
//        email_edt.setText(SharedPrefHelper.getLogin(context).getResult().getEmail() != null ? SharedPrefHelper.getLogin(context).getResult().getEmail() : "");
//        mobile_edt.setText(SharedPrefHelper.getLogin(context).getResult().getMobile() != null ? SharedPrefHelper.getLogin(context).getResult().getMobile() : "");
//        dob_edt.setText(SharedPrefHelper.getLogin(context).getResult().getDate() != null ? SharedPrefHelper.getLogin(context).getResult().getDob() : "");
//        address_edt.setText(SharedPrefHelper.getLogin(context).getResult().getAddress() != null ? SharedPrefHelper.getLogin(context).getResult().getAddress() : "");
//
//
//        if (SharedPrefHelper.getLogin(context).getResult().getGender().equalsIgnoreCase("male")) {
//            radioMale.setChecked(true);
//            radioFemale.setChecked(false);
//            radioOthers.setChecked(false);
//
//        } else if (SharedPrefHelper.getLogin(context).getResult().getGender().equalsIgnoreCase("female")){
//            radioMale.setChecked(false);
//            radioOthers.setChecked(false);
//            radioFemale.setChecked(true);
//
//        }else {
//            radioMale.setChecked(false);
//            radioFemale.setChecked(false);
//            radioOthers.setChecked(true);
//
//        }

        ProfileUpdateInteractorInt adminProfileUpdateInteractorInt = new ProfileUpdateInteractor(context, restAPI);
        adminProfileUpdatePresenter = new ProfileUpdatePresenter(this, adminProfileUpdateInteractorInt);

    }


    private void callProfileGet(final ProfileGetReq verifyOtpReq) {

        progressDialog.show();

        try {
            final Call<ProfileGetRes> loginActivityResCall = restAPI.getProfileGetResCall(verifyOtpReq);
            loginActivityResCall.enqueue(new Callback<ProfileGetRes>() {
                @Override
                public void onResponse(Call<ProfileGetRes> call, Response<ProfileGetRes> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        ProfileGetRes ProfileGetRes = response.body();
                        if (ProfileGetRes.getStatusCode() == 200 && ProfileGetRes.getMessage() != null) {

                            firstname_edt.setText(ProfileGetRes.getMessage().getFirstName() != null ? ProfileGetRes.getMessage().getFirstName() : "");
                            lastname_edt.setText(ProfileGetRes.getMessage().getLastName() != null ? ProfileGetRes.getMessage().getLastName() : "");
                            email_edt.setText(ProfileGetRes.getMessage().getEmail() != null ? ProfileGetRes.getMessage().getEmail() : "");
                            mobile_edt.setText(ProfileGetRes.getMessage().getPhoneNumber() != null ? ProfileGetRes.getMessage().getPhoneNumber() : "");
                            password_edt.setText(ProfileGetRes.getMessage().getPassword() != null ? ProfileGetRes.getMessage().getPassword() : "");


                        } else {
                            CustomErrorToast(ProfileGetRes.getStatus());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileGetRes> call, Throwable t) {
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
