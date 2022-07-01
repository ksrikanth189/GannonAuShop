package com.gannon.Login.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gannon.Login.interactor.LoginInteractor;
import com.gannon.Login.interactor.LoginInteractorInt;
import com.gannon.Login.interactor.model.LoginActivityReq;
import com.gannon.Login.interactor.model.LoginActivityRes;
import com.gannon.Login.presenter.LoginPresenter;
import com.gannon.Login.view.LoginView;
import com.gannon.R;
import com.gannon.Register.activity.RegisterActivity;
import com.gannon.forgetPassword.activity.ForgetPasswordActivity;
import com.gannon.home.HomeActivity;
import com.gannon.splash.activity.SplashActivity2;
import com.gannon.usermanagement.UserManagementScreen;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends SuperCompatActivity implements LoginView {

    private final int REQUEST_CODE_PERMISSION = 222;
    private final int REQUEST_READ_CODE_PERMISSION = 121;
    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private LoginPresenter userLoginPresenter;
    private LoginActivityReq loginActivityReq;
    private EditText userId, password;
    private String loginType;
    private boolean STORAGE_VAL = false;
    private boolean READ_STORAGE_VAL = false;
    private boolean CAMERA_VAL = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_login);

        initializeUiElements();


    }

    public void back_BTN(View view){
        startActivity(new Intent(this, SplashActivity2.class));
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        finish();

        startActivity(new Intent(LoginActivity.this, SplashActivity2.class));

    }

    @Override
    public void serviceSuccess(LoginActivityRes userLoginActivityRes) {
        if (userLoginActivityRes != null) {
            userLoginPresenter.saveLogin(userLoginActivityRes);

            if (userLoginActivityRes.getMessage().getAdminFlag() == true) {
                startActivity(new Intent(LoginActivity.this, UserManagementScreen.class));
            }else {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }

            Toast toast = Toast.makeText(getApplicationContext(), userLoginActivityRes.getStatus(), Toast.LENGTH_SHORT);
            toast.show();

        } else {
            serviceFailed(context.getResources().getString(R.string.server_not_responding));

        }
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
        Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show();
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

//        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(0, 0);
        finish();

    }


    public void signIn(View view) {

        if (userId.getText().toString().length() == 0) {
            CustomErrorToast(getResources().getString(R.string.pls_enter_email));
        } else if (password.getText().toString().length() == 0) {
            CustomErrorToast(getResources().getString(R.string.pls_enter_password));
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkAllPermissionsVal();
            } else {

                CAMERA_VAL = true;
                STORAGE_VAL = true;
                READ_STORAGE_VAL = true;
//                LOCATION_VAL = true;

                if (checkInternet()) {
                    userLoginPresenter.signInBtnClicked(userId.getText().toString(), password.getText().toString());
                } else {
                    showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));
                }
            }

        }
    }

    /*create account*/

    public void createAccount(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void forgetPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
    }

    @Override
    protected void onResume() {

        super.onResume();
        restAPI = getRestAPIObj();
        LoginInteractorInt userLoginInteractorInt = new LoginInteractor(context, restAPI);
        userLoginPresenter = new LoginPresenter(this, userLoginInteractorInt);
    }


    private void initializeUiElements() {

//        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "loginType", Context.MODE_PRIVATE);
//        loginType = prefsLogin.getString("loginType", "");


        context = this;
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);

        userId = findViewById(R.id.user_id);
        password = findViewById(R.id.password);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)) {
//                getGoogleAccounts();
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS}, 1);
//                //return false;
//            }
//        }


//        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
//        String deviceName = myDevice.getName();
////        String deviceName = Settings.System.getString(getContentResolver(), "device_name");
////        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
//        userId.setText(deviceName);


        LoginInteractorInt userLoginInteractorInt = new LoginInteractor(context, restAPI);
        userLoginPresenter = new LoginPresenter(this, userLoginInteractorInt);


    }



    private void checkAllPermissionsVal() {
        int readpermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writepermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camerapermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA);
//        int locationpermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != -1) {
            CAMERA_VAL = true;
        }
        if (readpermission != -1) {
            READ_STORAGE_VAL = true;
        }
        if (writepermission != -1) {
            STORAGE_VAL = true;
        }
//        if (locationpermission != -1) {
//            LOCATION_VAL = true;
//        }

        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
//        if (locationpermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }


        if (!listPermissionsNeeded.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CODE_PERMISSION);
            }
            return;
        }
        if (listPermissionsNeeded.isEmpty()) {
            CAMERA_VAL = true;
            STORAGE_VAL = true;
            READ_STORAGE_VAL = true;
            //  LOCATION_VAL = true;


            if (checkInternet()) {
                userLoginPresenter.signInBtnClicked(userId.getText().toString().trim(), password.getText().toString().trim());
            } else {
                showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));
            }

        }
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {

            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equals(Manifest.permission.CAMERA)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "camera granted");
                            CAMERA_VAL = true;

                            if (checkInternet()) {
                                userLoginPresenter.signInBtnClicked(userId.getText().toString().trim(), password.getText().toString().trim());
                            } else {
                                showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));
                            }

                        }
                    } else if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "write storage granted");
                            STORAGE_VAL = true;

                            if (checkInternet()) {
                                userLoginPresenter.signInBtnClicked(userId.getText().toString().trim(), password.getText().toString().trim());
                            } else {
                                showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));
                            }

                        }
                    } else if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "read storage granted");
                            READ_STORAGE_VAL = true;


                            if (checkInternet()) {
                                userLoginPresenter.signInBtnClicked(userId.getText().toString().trim(), password.getText().toString().trim());
                            } else {
                                showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));
                            }
                        }
                    }
//                    else if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                            Log.e("msg", "read storage granted");
//                            LOCATION_VAL = true;
//
//
//                            if (checkInternet()) {
//                                loginPresenter.signInBtnClicked(userId.getText().toString().trim(), password.getText().toString().trim(), rememberMe);
//                            } else {
//                                showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));
//                            }
//                        }
//                    }
                }
            } else {

            }
        } else if (requestCode == REQUEST_READ_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "read storage granted");
                        }
                    }
                }
            } else {

            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            getGoogleAccounts();
//        }
//    }

//    private void getGoogleAccounts() {
//        AccountManager am = AccountManager.get(this); // "this" references the current Context
//        Account[] accounts = am.getAccountsByType("com.google");
//
//        for (Account acc : accounts) {
//            System.out.println("http accounts " + acc);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                password.setText(acc.name);
//            }
//        }
//    }


    public void CustomOKAlertDialog(String msg) {

        final Dialog dialogComp = new Dialog(LoginActivity.this);
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

                startActivity(new Intent(LoginActivity.this,HomeActivity.class));

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


}