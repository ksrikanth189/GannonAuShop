package com.gannon.Login.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.gannon.R;
import com.gannon.sharedpref.ObscuredSharedPreferences;
import com.gannon.utils.ApplicationContext;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LoginSettings extends SuperCompatActivity {
    Button save, reset;
    EditText edittexturl;
    ImageView lodinsettings_back;
    String urldata;
    HttpURLConnection c = null;
    URL url = null;
    int statusCode;
    private ProgressDialog progressDialog;
    private ActionBarDrawerToggle mDrawerToggle;
    private RestAPI restAPI;


    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.loginsettings);
//        setBackground();

        initializeUiElements();

        save = ((Button) findViewById(R.id.buttonsave));
        edittexturl = ((EditText) findViewById(R.id.editTexturl));
        edittexturl.setText(ApplicationContext.RESET_BASE_URL);
        reset = ((Button) findViewById(R.id.buttonreset));


        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "URL_DTS", Context.MODE_PRIVATE);
        String url = prefsLogin.getString("savedUrl", "");
        if (url.length() > 0) {
            edittexturl.setText(url);
        }

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (edittexturl.getText().toString().trim().length() != 0) {
                    urldata = edittexturl.getText().toString().trim();

                    if (checkInternet()) {

                        //ValidateUrl(urldata);

                        new ValidateUrlAsyncTask().execute();

                    } else {

                        //DialogClass.createDAlertDialog(LoginSettings.this, context.getResources().getString(R.string.plz_chk_your_net), "tst", null, null);

                        CustomErrorToast(context.getResources().getString(R.string.plz_chk_your_net));

                    }

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            LoginSettings.this);
                    alert.setTitle(context.getResources().getString(R.string.app_name));
                    alert.setMessage("Please enter URL");
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.dismiss();

                                }
                            });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }


            }
        });


        reset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                edittexturl.setText("");
                ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "URL_DTS", Context.MODE_PRIVATE);
                ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
                editor.putString("savedUrl", "");
                editor.commit();

                ApplicationContext.BASE_URL = ApplicationContext.RESET_BASE_URL;
                //ApplicationContext.BASE_URL = "https://itoms.2wth.com";
                //ApplicationContext.BASE_URL = "https://uat.infyztoms.com";
                //ApplicationContext.BASE_URL = "http://192.168.1.40";
                //ApplicationContext.BASE_URL = "http://192.168.1.138:8080";


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

        //=== Set Tool Bar and Drawer layout ===
        setToolBar(getApplicationContext(), "Login Settings", "yes");
//		String pushToken = FirebaseInstanceId.getInstance().getToken();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        //setupdrawerLayout();
        //getToggleAndSlider();
        restAPI = getRestAPIObj();

        progressDialog = initializeProgressDialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        //========================================
        context = getApplicationContext();

    }

    private class ValidateUrlAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {

            try {
                url = new URL(urldata);

				/*if (url.getProtocol().toLowerCase().equals("https")) {
                    trustAllHosts();
					HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
					https.setHostnameVerifier(DO_NOT_VERIFY);
					c = https;
				} else {
//					c = (HttpURLConnection) url.openConnection();
				}*/
                c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setConnectTimeout(20000);
                c.setDoOutput(true);
                c.connect();
                statusCode = c.getResponseCode();
                Log.v("TAG", "getResponseCode = " + c.getResponseCode());

            } catch (MalformedURLException e) {
                e.printStackTrace();

                //	DialogClass.createDAlertDialog(LoginSettings.this,"Invalid URL","tst",null,null);

            } catch (IOException e) {
                e.printStackTrace();

                //	DialogClass.createDAlertDialog(LoginSettings.this,"Invalid URL","tst",null,null);

            } catch (Exception e) {
                e.printStackTrace();

                //DialogClass.createDAlertDialog(LoginSettings.this,"Invalid URL","tst",null,null);

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {

            super.onPostExecute(o);

            try {

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (statusCode == 200 || statusCode == 301) {


				/*	ApplicationContext.BASE_URL = edittexturl.getText().toString().trim();
					spEditor.putString("saveUrl", edittexturl.getText().toString().trim());
					spEditor.commit();*/

                    ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "URL_DTS", Context.MODE_PRIVATE);
                    ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
                    editor.putString("savedUrl", edittexturl.getText().toString().trim());
                    editor.commit();

                    ApplicationContext.BASE_URL = edittexturl.getText().toString().trim();

                   // DialogClass.createDAlertDialog(LoginSettings.this, "URL saved", "tst", null, null);

                    CustomSuccessToast("URL saved");


                    finish();
                } else {

                  //  DialogClass.createDAlertDialog(LoginSettings.this, "Invalid URL", "tst", null, null);

                    CustomErrorToast("Invalid URL");

                    edittexturl.requestFocus();
                }
            } catch (Exception e) {
            }

        }
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void ValidateUrl(String urldata) {
//
//        progressDialog.show();
//        try {
//            final Call<String> checkForUpdateResCall = restAPI.getValidUrl();
//            checkForUpdateResCall.enqueue(new Callback<String>() {
//
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//
//                    if (progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                    if (response.isSuccessful()) {
//                        //CheckValidUrl privacyRes = response.body();
//                        //if (privacyRes.getStatusCode() == 200) {
//
//                        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "URL_DTS", Context.MODE_PRIVATE);
//                        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
//                        editor.putString("savedUrl", edittexturl.getText().toString().trim());
//                        editor.commit();
//
//                        ApplicationContext.BASE_URL = edittexturl.getText().toString().trim();
//
//                       // DialogClass.createDAlertDialog(LoginSettings.this, "URL saved", "tst", null, null);
//
//                        CustomSuccessToast("URL saved");
//
//                        finish();
//
//
//                    } else {
//
//                        CustomErrorToast("Invalid URL");
//
//                       // DialogClass.createDAlertDialog(LoginSettings.this, "Invalid URL", "tst", null, null);
//                        edittexturl.requestFocus();
//
//                    }
//                    //}
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    if (progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                }
//
//
//            });
//        } catch (Exception e) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//        }
//    }
}
