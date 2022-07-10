package com.gannon.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.content.Loader;

import com.gannon.Login.activity.LoginActivity;
import com.gannon.R;
import com.gannon.home.HomeActivity;
import com.gannon.mysales.MySalesScreen;
import com.gannon.mywins.MyWinsScreen;
import com.gannon.sharedpref.ObscuredSharedPreferences;
import com.gannon.sharedpref.SharedPrefHelper;
import com.gannon.uploadAuctionDonation.FileUtils;
import com.gannon.uploadAuctionDonation.activity.NewAuctionDonation;
import com.gannon.usermanagement.UserManagementScreen;
import com.gannon.webview.WebViewImageUpload;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class SuperCompatActivity extends AppCompatActivity {

    public Context context, myContext;
    public String pushToken;
    private Toolbar toolbar;
    private ProgressDialog pg_dialog;
    private ConstraintLayout mainLayout;
    public LinearLayout linearmLeftDrawer;
    public DrawerLayout mDrawerLayout;
    private ImageView nav_back, toolbar_icon,homemenu_img;
    private TextView home_txt;
    private Loader loader;
    private String versionName;

    private LinearLayout userManagement_menu, home_menu, AUCTIONDONATION_menu, MySales_menu, MyWins_menu;


    private final Pattern pattern = Pattern
            .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        context = getApplicationContext();
        // pushToken = FirebaseInstanceId.getInstance().getToken();
        Log.v("Push Token", "" + pushToken);

    }


    public void setCustomTheme(Context context) {
        setTheme(R.style.Theme_Gannon);
    }


    @Override
    protected void onResume() {
        super.onResume();
        closedrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    public void setToolBar(final Context myContext, String title, String cartIcon) {

        this.myContext = myContext;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav_back = (ImageView) toolbar.findViewById(R.id.nav_back);
        toolbar_icon = (ImageView) toolbar.findViewById(R.id.notifica_icon);

        TextView textView = toolbar.findViewById(R.id.txt_title);
        textView.setText("" + title);

    }


    @SuppressLint("NewApi")
    public void setupdrawerLayout() {

        try {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.mainDrawerLayout);
            linearmLeftDrawer = (LinearLayout) findViewById(R.id.mLeftDrawer);
//            main_layout = (LinearLayout) findViewById(R.id.main_layout);
            // mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            mDrawerLayout.closeDrawer(linearmLeftDrawer);
            mDrawerLayout.getParent().requestDisallowInterceptTouchEvent(false);


            TextView user_name = findViewById(R.id.user_name);
            TextView version_txt = findViewById(R.id.version_txt);
            user_name.setText(SharedPrefHelper.getLogin(context).getMessage().getUserName());


            try {
                versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            userManagement_menu = (LinearLayout) findViewById(R.id.userManagement_menu);
            home_menu = (LinearLayout) findViewById(R.id.home_menu);
            AUCTIONDONATION_menu = (LinearLayout) findViewById(R.id.AUCTIONDONATION_menu);
            MySales_menu = (LinearLayout) findViewById(R.id.MySales_menu);
            MyWins_menu = (LinearLayout) findViewById(R.id.MyWins_menu);
            home_txt =  findViewById(R.id.home_txt);
            homemenu_img =  findViewById(R.id.homemenu_img);


            if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null) {

                if (SharedPrefHelper.getLogin(context) != null && SharedPrefHelper.getLogin(context).getMessage() != null && SharedPrefHelper.getLogin(context).getMessage().getAdminFlag() == true) {
                    userManagement_menu.setVisibility(View.VISIBLE);
                    home_menu.setVisibility(View.VISIBLE);
                    AUCTIONDONATION_menu.setVisibility(View.GONE);
                    MySales_menu.setVisibility(View.GONE);
                    MyWins_menu.setVisibility(View.GONE);
                    home_txt.setText("ALL AUCTION/DONATION");
                    homemenu_img.setBackground(getResources().getDrawable(R.drawable.auction));

                    AUCTIONDONATION_menu.setClickable(false);
                    MySales_menu.setClickable(false);
                    MyWins_menu.setClickable(false);


                } else {
                    userManagement_menu.setVisibility(View.GONE);
                    home_menu.setVisibility(View.VISIBLE);
                    AUCTIONDONATION_menu.setVisibility(View.VISIBLE);
                    MySales_menu.setVisibility(View.VISIBLE);
                    MyWins_menu.setVisibility(View.VISIBLE);

                    userManagement_menu.setClickable(false);



                    home_txt.setText("HOME");
                    homemenu_img.setBackground(getResources().getDrawable(R.drawable.home));


                }
            }


        } catch (Exception e) {
            Log.d("tag", "slider exce-->" + e.getMessage());
        }
    }


    public void userManagement(View v) {
        mDrawerLayout.closeDrawer(linearmLeftDrawer);
        startActivity(new Intent(getApplicationContext(), UserManagementScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//        main_layout.setClickable(false);

    }

    public void menuHome(View v) {
        mDrawerLayout.closeDrawer(linearmLeftDrawer);
        startActivity(new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void AUCTIONDONATION(View v) {
        mDrawerLayout.closeDrawer(linearmLeftDrawer);
        startActivity(new Intent(getApplicationContext(), NewAuctionDonation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void MySales(View v) {
        mDrawerLayout.closeDrawer(linearmLeftDrawer);
        startActivity(new Intent(getApplicationContext(), MySalesScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void MyWins(View v) {
        mDrawerLayout.closeDrawer(linearmLeftDrawer);
        startActivity(new Intent(getApplicationContext(), MyWinsScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void Terms_condition(View v) {
//        mDrawerLayout.closeDrawer(linearmLeftDrawer);

        Intent intent = new Intent(SuperCompatActivity.this, WebViewImageUpload.class);
        intent.putExtra("titleStr", "Terms & Conditions");
        intent.putExtra("weburl", ApplicationContext.BASE_URL+"/docs/TermsConditions.html");
        startActivity(intent);
        finish();

    }

    public void PrivacyPolicy(View v) {
//        mDrawerLayout.closeDrawer(linearmLeftDrawer);

        Intent intent = new Intent(SuperCompatActivity.this, WebViewImageUpload.class);
        intent.putExtra("titleStr", "Privacy Policy");
        intent.putExtra("weburl", ApplicationContext.BASE_URL+"/docs/privacypolicy.html");
        startActivity(intent);
        finish();    }


    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public RestAPI getRestAPIObj() {

        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "URL_DTS", Context.MODE_PRIVATE);
        String url = prefsLogin.getString("savedUrl", "");
        if (url.length() > 0) {
            ApplicationContext.BASE_URL = url;
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApplicationContext.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create()).build();
        RestAPI restAPI = retrofit.create(RestAPI.class);
        return restAPI;
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public float pxFromDp(float dp, Context mContext) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    public ProgressDialog initializeProgressDialog(Context context) {
        pg_dialog = new ProgressDialog(context);
        pg_dialog.setMessage("Please wait.");
        pg_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg_dialog.setIndeterminate(true);
        pg_dialog.setProgress(0);
        pg_dialog.setCancelable(false);
        return pg_dialog;
    }

    public boolean chkEmailPattern(String email) {
        Matcher m = pattern.matcher(email);
        return m.matches();
    }

    public String getVersionCode(Activity activity) {

        String appVersion = "";
        PackageManager manager = activity.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            appVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersion;
    }

    public String getResourceStr(Context context, int id) {
        String returnVal = null;
        try {
            returnVal = getResources().getString(id);
        } catch (Exception e) {
            returnVal = context.getResources().getString(id);
        }
        return returnVal;
    }

    public void closedrawer() {
        try {
            if (mDrawerLayout != null) {
                if (mDrawerLayout.isDrawerOpen(linearmLeftDrawer))
                    mDrawerLayout.closeDrawer(linearmLeftDrawer);
            }
        } catch (Exception e) {
            Log.d("tag", "slider exce-->" + e.getMessage());
        }
    }

    public String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String hex = Integer.toHexString(b & 0xFF);
                    if (hex.length() == 1)
                        hex = "0".concat(hex);
                    res1.append(hex.concat(":"));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }


    public void smoothScrollToPosition(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
            }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }


    public void CustomSuccessToast(String msg) {

        Toast newToast = Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_SHORT);
        newToast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = getLayoutInflater();
        View tastyToast = inflater.inflate(R.layout.tasty_toast, (ViewGroup) findViewById(R.id.tastyLayout));
        TextView toastText = (TextView) tastyToast.findViewById(R.id.textView1);
        toastText.setText(msg);
        newToast.setView(tastyToast);
        newToast.show();
    }

    public void CustomErrorToast(String msg) {
        Toast newToast = Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_SHORT);
        newToast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = getLayoutInflater();
        View tastyToast = inflater.inflate(R.layout.tasty_toast_error, (ViewGroup) findViewById(R.id.tastyLayout));
        TextView toastText = (TextView) tastyToast.findViewById(R.id.textView1);
        toastText.setText(msg);
        newToast.setView(tastyToast);
        newToast.show();
    }


    public void CustomOKAlertDialog(String msg) {

        final Dialog dialogComp = new Dialog(SuperCompatActivity.this);
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

    public String getRealPathFromURI(Uri contentURI) {
        /*Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);*/

        String result;
        Cursor cursor = getContentResolver().query(contentURI, null,
                null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            try {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            } catch (Exception e) {
                result = contentURI.getPath();
            }
            cursor.close();
        }
        return result;
    }


    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        MediaType mediaType;
        //String imagePath = getFilePathFromURI(NewPostActivity.this, fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = Uri.fromFile(new File(getRealPathFromURI(fileUri)));
        }
        File file = FileUtils.getFile(this, fileUri);
        try {
            mediaType = MediaType.parse(getContentResolver().getType(fileUri));
        } catch (Exception e) {
            mediaType = MediaType.parse("image/*");
        }
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(mediaType, file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

    }


    public void logoutDialog() {

        final Dialog dialogComp = new Dialog(SuperCompatActivity.this);
        dialogComp.setCancelable(true);
        dialogComp.setCanceledOnTouchOutside(false);
        dialogComp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComp.setContentView(R.layout.app_exit_dialog);
        dialogComp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogComp.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final Button closeDialog = (Button) dialogComp.findViewById(R.id.closeDialog);
        final Button clearLogin = (Button) dialogComp.findViewById(R.id.clearLogin);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComp.dismiss();
            }
        });

        clearLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefHelper.clearLoginData(context);
                dialogComp.dismiss();
                Intent intent = new Intent(SuperCompatActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });


        dialogComp.show();
    }


}
