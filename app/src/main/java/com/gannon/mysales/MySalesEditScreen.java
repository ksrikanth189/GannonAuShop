package com.gannon.mysales;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.gannon.BuildConfig;
import com.gannon.R;
import com.gannon.home.HomeActivity;
import com.gannon.mysales.model.ImageDeleteReq;
import com.gannon.mysales.model.ImageDeleteRes;
import com.gannon.mysales.model.MySalesEditReqPayLoad;
import com.gannon.mysales.model.MySalesEditResponsePayLoad;
import com.gannon.mysales.model.MySalesUpdateReq;
import com.gannon.notifications.NotificationActivity;
import com.gannon.notifications.model.NotificationsUpdateReq;
import com.gannon.notifications.model.NotificationsUpdateRes;
import com.gannon.uploadAuctionDonation.FileUploadRespPojo;
import com.gannon.uploadAuctionDonation.activity.MultiPhotoSelectActivity;
import com.gannon.uploadAuctionDonation.activity.NewAuctionDonation;
import com.gannon.uploadAuctionDonation.interactor.model.SaveResponsePayLoad;
import com.gannon.utils.ApplicationContext;
import com.gannon.utils.RestAPI;
import com.gannon.utils.SuperCompatActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MySalesEditScreen extends SuperCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int GALLERY_REQUEST_CODE = 102;
    private static final int CAMERA_REQUEST = 105;
    public ConnectivityManager conmanger;
    Button buttonsave, buttonclear,buttonHistory;
    byte[] byteArray;
    Bitmap bitmap;
    ImageView userImage;
    Bitmap bitmap_sig;

    Call<SaveResponsePayLoad> damageSaveResponsePayLoadCall;
    String multiimages;
    Button damage_date, damage_time, choose_images;
    RecyclerView damage_images;
    EditText productname_edt, productdes_edt, auctionamount_edt;
    SaveResponsePayLoad damage_saveResponsePayLoad;
    private int cargoId, barcode;

    private int PICK_IMAGE_REQUEST = 1;
    private int cameraId = 0;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> arrayListImages = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private RestAPI restAPI;
    private ActionBarDrawerToggle mDrawerToggle;
    private Calendar now, minDate;
    private DatePickerDialog dpd;
    private TimePickerDialog time;
    private boolean view_str_dts = false;
    private Button upload_images;
    private ProgressDialog progressDialog, progressDialog1,m_progress1;

    private ArrayList<String> multiPartImagesList = new ArrayList<>();
    private Uri captureImageUri;

    private Button type_btn;
    LinearLayout auction_ll, auctionamount_ll,action_intial_amt_ll;
    TextView closedate_txt, auctionAmount_txt, initialAmount_txt;
    Button auctionStatus_edt;

    private ProgressDialog m_progress;

    MySalesEditResponsePayLoad mySalesEditResponsePayLoad;
    String type;

    String imagePath = "";
    File imageFilePath;
    private ArrayList<File> imageFilePathImages = new ArrayList<File>();
    String imageVersion = "old";


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
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.mysales_edit_activity);

        initializeUiElements();


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            cargoId = 0;
            barcode = 0;
            type = null;

        } else {
            cargoId = extras.getInt("cargoId");
            barcode = extras.getInt("barcode");
            type = extras.getString("type");

        }

        now = Calendar.getInstance();

        // String date = "" + String.format("%02d", now.get(Calendar.DAY_OF_MONTH)) + "-" + String.format("%02d", now.get(Calendar.MONTH)) + "-" + now.get(Calendar.YEAR);
        String date = "" + String.format("%02d", now.get(Calendar.YEAR)) + "-" + String.format("%02d", now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);

        damage_date.setText(date);

        dpd = DatePickerDialog.newInstance(
                MySalesEditScreen.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );


        dpd.setThemeDark(true);
        minDate = Calendar.getInstance();

        dpd.setMinDate(minDate);

        boolean is24HourMode = false;


        time = TimePickerDialog.newInstance(MySalesEditScreen.this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), is24HourMode);
        //String timeStr = "" +  now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE)+ ":" + now.get(Calendar.SECOND);
        String timeStr = "" + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);

        damage_time.setText(timeStr);

        time.setThemeDark(true);
        minDate = Calendar.getInstance();

        dpd.setMinDate(minDate);

        damage_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        damage_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                time.show(getFragmentManager(), "DatepickerdialogAni");
            }
        });


        if (!checkInternet()) {
            CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
        } else {
            getMySalesService(type);
        }


        if (type != null && type.equalsIgnoreCase("auction")){
            action_intial_amt_ll.setVisibility(View.VISIBLE);
            buttonHistory.setVisibility(View.VISIBLE);
            closedate_txt.setText("Auction close date");


        }else {
            action_intial_amt_ll.setVisibility(View.GONE);
            buttonHistory.setVisibility(View.GONE);
            closedate_txt.setText("Donation close date");

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

    public void showProgress() {
        if (!isFinishing()) {
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initializeUiElements() {

        //=== Set Tool Bar and Drawer layout ===
        setToolBar(getApplicationContext(), "My Sales Edit ", "yes");
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
        progressDialog = initializeProgressDialog(this);

        conmanger = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


        buttonsave = (Button) findViewById(R.id.buttonsave);
        buttonclear = ((Button) findViewById(R.id.buttonclear));
        buttonHistory = ((Button) findViewById(R.id.buttonHistory));

        type_btn = (Button) findViewById(R.id.type_btn);
        damage_date = (Button) findViewById(R.id.damage_date);
        damage_time = (Button) findViewById(R.id.damage_time);
        choose_images = (Button) findViewById(R.id.choose_images);
        damage_images = (RecyclerView) findViewById(R.id.damage_images);
        productname_edt = (EditText) findViewById(R.id.productname_edt);
        productdes_edt = (EditText) findViewById(R.id.productdes_edt);
        auctionamount_edt = (EditText) findViewById(R.id.auctionamount_edt);
        upload_images = findViewById(R.id.upload_images);

        auction_ll = findViewById(R.id.auction_ll);
        auctionamount_ll = findViewById(R.id.auctionamount_ll);
        closedate_txt = findViewById(R.id.closedate_txt);
        auctionStatus_edt = findViewById(R.id.auctionStatus_edt);
        auctionAmount_txt = findViewById(R.id.auctionAmount_txt);
        initialAmount_txt = findViewById(R.id.initialAmount_txt);
        action_intial_amt_ll = findViewById(R.id.action_intial_amt_ll);

        /*auction*/

//        upload_images.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (arrayListImages.size() > 0)
//                    multiPartImagesUpload();
//                else
//                    CustomErrorToast("Please select or capture images");
//            }
//        });



        choose_images.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog();
            }
        });

        damage_images.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MySalesEditScreen.this, LinearLayoutManager.HORIZONTAL, false);
        damage_images.setLayoutManager(mLayoutManager);


        arrayListImages.clear();
        multiPartImagesList.clear();



        buttonclear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MySalesEditScreen.this, MySalesScreen.class);
//                Intent intent = new Intent(MySalesEditScreen.this, MySalesEditScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        buttonsave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

//                if (arrayListImages.size() > 0)
//                    multiPartImagesUpload();
//                else
//                    CustomErrorToast("Please select or capture images");

                    if (productname_edt.getText().toString().length() == 0) {
                        CustomErrorToast(getResources().getString(R.string.plz_enter_productname));
                    } else if (productdes_edt.getText().toString().length() == 0) {
                        CustomErrorToast(getResources().getString(R.string.plz_enter_productdes));
                    } else if (damage_date.getText().toString().equalsIgnoreCase("Select Date")) {
                        CustomErrorToast(getResources().getString(R.string.pls_select_date));
                    } else if (damage_time.getText().toString().equalsIgnoreCase("Select Time")) {
                        CustomErrorToast(getResources().getString(R.string.pls_select_time));
                    } else {

//                        if(mySalesEditResponsePayLoad.getMessage().getImagesList().size() > 0){
//
//                        }
                        if (arrayListImages.size() > 0) {
                            multiPartImagesUpload();
                        }else {
//                            CustomErrorToast("Please select or capture images");

                            if (checkInternet()) {
                                NewAuctionDonationDetailstSave();
                            } else {
                                CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                            }

                        }
                    }
            }
        });


        buttonHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MySalesEditScreen.this,MySalesHistoryScreen.class);
                intent.putExtra("cargoId",cargoId);
                intent.putExtra("barcode",barcode);
                startActivity(intent);
            }
        });


    }

    /*save service */
    private void NewAuctionDonationDetailstSave() {

        try {

            showProgress();

            MySalesUpdateReq requestPayLoad = new MySalesUpdateReq();
            requestPayLoad.setAuctionOrDonation(type);
            requestPayLoad.setProductName(productname_edt.getText().toString());
            requestPayLoad.setProductDescription(productdes_edt.getText().toString());
            requestPayLoad.setAuctionCloseDate(damage_date.getText().toString().trim() + " " + damage_time.getText().toString().trim());
            requestPayLoad.setAuctionStatus(auctionStatus_edt.getText().toString());
            requestPayLoad.setAuctionId(cargoId);
            requestPayLoad.setDonationId(barcode);
            requestPayLoad.setImagesList(multiPartImagesList);

            Gson gson = new Gson();
            gson.toJson(requestPayLoad);
            Log.v("damage", "damge save" + gson.toJson(requestPayLoad));

            damageSaveResponsePayLoadCall = restAPI.getMySalesUpdateResponsePayLoadCall(requestPayLoad);
            damageSaveResponsePayLoadCall.enqueue(new Callback<SaveResponsePayLoad>() {
                @Override
                public void onResponse(Call<SaveResponsePayLoad> call, Response<SaveResponsePayLoad> response) {

                    hideProgress();
                    if (response.isSuccessful()) {

                        damage_saveResponsePayLoad = response.body();

                        if (damage_saveResponsePayLoad.getStatus().equalsIgnoreCase("success")) {

                            CustomErrorToast(damage_saveResponsePayLoad.getMessage());

                            Intent intent = new Intent(MySalesEditScreen.this, MySalesScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


                        } else {

                            CustomOKAlertDialog(damage_saveResponsePayLoad.getError());
                        }

                    }

                    hideProgress();


                }

                @Override
                public void onFailure(Call<SaveResponsePayLoad> call, Throwable t) {
                    CustomOKAlertDialog(damage_saveResponsePayLoad.getMessage());
                    hideProgress();


                }


            });


        } catch (Exception e) {

            //     hideProgress();
            e.getMessage();
        }

    }

    /*New Camera*/
    private byte[] getByteArrayImage(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuilder baf = new ByteArrayBuilder(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                userImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            if (captureImageUri != null)
                arrayListImages.add(captureImageUri.toString());

            imageAdapter = new ImageAdapter(arrayListImages);
            damage_images.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();

            imageVersion = "old";


            cameraMultiImagesDialog();

        }


        if (requestCode == GALLERY_REQUEST_CODE) {

            try {
                ArrayList<ArrayList<String>> getimages = (ArrayList<ArrayList<String>>) data.getExtras().get("getImages");
                arrayListImages.addAll(getimages.get(0));
                imageVersion = "old";

                ImageAdapter imageAdapter1 = new ImageAdapter(arrayListImages);
                damage_images.setAdapter(imageAdapter1);
                imageAdapter1.notifyDataSetChanged();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    for (int i = 0; i < arrayListImages.size(); i++) {
                        File file = new File(getimages.get(0).get(i));
                        imageFilePathImages.add(file);

                        imageVersion = "latest";
                    }
                }

            } catch (Exception e) {

            }


        }

        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
            byteArray = stream.toByteArray();

            //  imageString = bitmapToBase64(bitmap);

        }


    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void cameraMultiImagesDialog() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }


        builder.setTitle("Capture Multiple Images")
                .setMessage("Are you sure want continue to capture the more images ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with  capture

//                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", cameraId);
//                        startActivityForResult(cameraIntent, CAMERA_REQUEST);


                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            try {
                                captureImageUri = FileProvider.getUriForFile(getApplicationContext(),
                                        BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        } else {

                            // cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", cameraId);
                            captureImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "wwl_" +
                                    String.valueOf(System.currentTimeMillis()) + ".png"));
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                        //   finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }
    private void customDialog() {

        final Dialog customDialog = new Dialog(MySalesEditScreen.this);

//        temImageView = imageView;

        customDialog.setCancelable(true);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.select_or_capture_dialog);

        Button gallery_btn = (Button) customDialog.findViewById(R.id.gallery_btn);
        Button camera_btn = (Button) customDialog.findViewById(R.id.camera_btn);
        customDialog.show();

        gallery_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog.dismiss();
                /*multi images for gallary*/
                Intent intent = new Intent(MySalesEditScreen.this, MultiPhotoSelectActivity.class);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);

// Show only images, no videos or anything else

//
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        camera_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog.dismiss();

//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", cameraId);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);


                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                    try {
                        imageFilePath = createImageFile();
                        imageFilePathImages.add(imageFilePath);
                        captureImageUri = FileProvider.getUriForFile(getApplicationContext(),
                                BuildConfig.APPLICATION_ID + ".provider", imageFilePath);

                        imageVersion = "latest";

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    try {
//                        captureImageUri = FileProvider.getUriForFile(getApplicationContext(),
//                                BuildConfig.APPLICATION_ID + ".provider", createImageFile());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {

                    // cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", cameraId);
                    captureImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "wwl_" +
                            String.valueOf(System.currentTimeMillis()) + ".png"));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    imageVersion = "old";

                }

            }
        });
    }
    private File createImageFile() throws IOException {
        // Create an image file name

//
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/saved_images");
//        myDir.mkdirs();
//        Random generator = new Random();
//        int n = 10000;
//        n = generator.nextInt(n);
//        String fname = "Image-" + n + ".jpg";
//        File fileNougat = new File(myDir, fname);
//
////        File fileNougat = new File(Environment.getExternalStorageDirectory(), "iToms_" + String.valueOf(System.currentTimeMillis()) + ".jpeg");
////        imageUriNouGat = Uri.parse(file.getAbsolutePath());
//
//                imagePath = "file:" + fileNougat.getAbsolutePath();
//
//        return fileNougat;


        String imageFileName = "OS_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + File.separator+ "/Gannon");
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        imagePath = "file:" + file.getAbsolutePath();
        ///imageUriNouGat = Uri.fromFile(file);
        return file;


    }


    /*New multipart */
    private void multiPartImagesUpload() {

        progressDialog1 = new ProgressDialog(MySalesEditScreen.this);
        progressDialog1.setMessage("Images uploading please wait....");
        progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog1.setIndeterminate(true);
        progressDialog1.setProgress(0);
        progressDialog1.setCancelable(false);
        progressDialog1.show();


        List<MultipartBody.Part> body = new ArrayList<>();
        try {
            body.clear();


            if (imageVersion.equalsIgnoreCase("old")) {
                for (int i = 0; i < arrayListImages.size(); i++) {
                    String selectedImage = arrayListImages.get(i);
                    if (selectedImage.contains("file://") || selectedImage.contains("content")) {
                        body.add(prepareFilePart("files", Uri.parse(selectedImage)));
                    } else {
                        body.add(prepareFilePart("files", Uri.fromFile(new File(selectedImage))));
                    }
                }
            } else if (imageVersion.equalsIgnoreCase("latest")) {
                for (int i = 0; i < arrayListImages.size(); i++) {
                    body.add(prepareFilePart("files", imageFilePathImages.get(i)));
                }
            }

            final Call<FileUploadRespPojo> availableMonthResponsePayLoadCall = restAPI.fileUploadService(body);
            availableMonthResponsePayLoadCall.enqueue(new Callback<FileUploadRespPojo>() {

                @Override
                public void onResponse(Call<FileUploadRespPojo> call, Response<FileUploadRespPojo> response) {

                    if (response.isSuccessful()) {
                        Log.v("Tag", response.message());
                        FileUploadRespPojo res = response.body();
                        if (res.getStatusCode() == 200) {
                            for (int i = 0; i < res.getData().size(); i++) {
                                String path = res.getData().get(i);
                                multiPartImagesList.add(path);
                            }
                            Toast.makeText(MySalesEditScreen.this, "Images uploaded successfully", Toast.LENGTH_SHORT).show();


                            arrayListImages.clear();

                            if (productname_edt.getText().toString().length() == 0) {
                                CustomErrorToast(getResources().getString(R.string.plz_enter_productname));
                            } else if (productdes_edt.getText().toString().length() == 0) {
                                CustomErrorToast(getResources().getString(R.string.plz_enter_productdes));
                            } else if (damage_date.getText().toString().equalsIgnoreCase("Select Date")) {
                                CustomErrorToast(getResources().getString(R.string.pls_select_date));
                            } else if (damage_time.getText().toString().equalsIgnoreCase("Select Time")) {
                                CustomErrorToast(getResources().getString(R.string.pls_select_time));
                            } else {
                                if (checkInternet()) {
                                    NewAuctionDonationDetailstSave();
                                } else {
                                    CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                                }
                            }


                        }
                    }
                    if (progressDialog1.isShowing()) {
                        progressDialog1.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<FileUploadRespPojo> call, Throwable t) {
                    try {
                        if (progressDialog1.isShowing()) {
                            progressDialog1.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (Exception e) {

            try {
                if (progressDialog1.isShowing()) {
                    progressDialog1.dismiss();
                }
            } catch (Exception ee) {
                ee.printStackTrace();

            }
        }

    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, File fileUri) {

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, new FileOutputStream(fileUri));
        } catch (Throwable t) {
            Log.e("ERROR", "Error compressing file." + t.toString());
            t.printStackTrace();
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), fileUri);
        return MultipartBody.Part.createFormData("files", fileUri.getName(), requestFile);
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (view.getTag().equalsIgnoreCase("Datepickerdialog")) {
//            String date = "" + String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (++monthOfYear)) + "-" + year;
//            String date = "" + String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (++monthOfYear)) + "-" + year;

            String date = "" + year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);


            damage_date.setText(date);
            now.set(Calendar.YEAR, year);
            now.set(Calendar.MONTH, monthOfYear - 1);
            now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        String time1 = "" + String.format("%02d", hourOfDay) + ":" + String.format("%02d", (minute)) + ":" + String.format("%02d", (second));

        damage_time.setText(time1);
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);
    }






    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        private List<String> values;

        // Provide a suitable constructor (depends on the kind of dataset)
        public ImageAdapter(List<String> myDataset) {
            values = myDataset;
        }

        public void add(int position, String item) {
            values.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            values.remove(position);
            notifyItemRemoved(position);
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v =
                    inflater.inflate(R.layout.inflater_imageload, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final String imageUrl = values.get(position);


            if (values.get(position).contains("file://") || values.get(position).contains("content")) {
                Glide.with(MySalesEditScreen.this)
                        .load(values.get(position))
                        .error(R.mipmap.icon6)
                        .placeholder(R.mipmap.icon6)
                        .into(holder.imagesa);
            } else {
                Glide.with(MySalesEditScreen.this)
                        .load("file://" + values.get(position))
                        .error(R.mipmap.icon6)
                        .placeholder(R.mipmap.icon6)
                        .into(holder.imagesa);
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return values.size();
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            public View layout;
            // each data item is just a string in this case
            ImageView imagesa;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                imagesa = (ImageView) v.findViewById(R.id.image_cap);

            }
        }

    }

    public void showStatusList(View view) {
        showStatusPop();
    }

    public void showStatusPop() {

        ArrayList<String> strVesselList = new ArrayList<String>();
        strVesselList.add("OPEN");
        strVesselList.add("CLOSED");
//        for (int k = 0; k < strVesselList.size(); k++) {
//            strVesselList.add(strVesselList.get(k));
//        }

        final Dialog dialogComp = new Dialog(MySalesEditScreen.this);
        dialogComp.setCancelable(true);
        dialogComp.setCanceledOnTouchOutside(false);
        dialogComp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComp.setContentView(R.layout.popup_list_dialog);
        dialogComp.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ListView strListView = (ListView) dialogComp.findViewById(R.id.str_area);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.textview_inflator, R.id.strAreaId, strVesselList);
        strListView.setAdapter(adapter);

        strListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 0) {
                auctionStatus_edt.setText("" + strVesselList.get(position));

//                if (type_btn.getText().toString().equalsIgnoreCase("Auction")) {
//                    auction_ll.setVisibility(View.VISIBLE);
//                    closedate_txt.setText("Auction close date");
//                    auctionamount_ll.setVisibility(View.VISIBLE);
//
//                } else {
//                    auction_ll.setVisibility(View.VISIBLE);
//                    closedate_txt.setText("Donation close date");
//                    auctionamount_ll.setVisibility(View.GONE);
//                }
//                }
                dialogComp.dismiss();
            }
        });

        dialogComp.show();
    }


    public void getMySalesService(String type) {

        try {


            m_progress = new ProgressDialog(MySalesEditScreen.this);
            m_progress.setMessage("Please wait....");
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setIndeterminate(true);
            m_progress.setProgress(0);
            m_progress.setCancelable(false);
            m_progress.show();

            MySalesEditReqPayLoad dmgreq = new MySalesEditReqPayLoad();
            dmgreq.setAuctionId(cargoId);
            dmgreq.setDonationId(barcode);
            dmgreq.setAuctionOrDonation(type);


            Call<MySalesEditResponsePayLoad> damageHistoryResPayLoadCall = restAPI.getMySalesEditResponsePayLoadCall(dmgreq);
            damageHistoryResPayLoadCall.enqueue(new Callback<MySalesEditResponsePayLoad>() {
                @Override
                public void onResponse(Call<MySalesEditResponsePayLoad> call, Response<MySalesEditResponsePayLoad> response) {
                    if (response.isSuccessful()) {

                        mySalesEditResponsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(mySalesEditResponsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(mySalesEditResponsePayLoad));

                        if (mySalesEditResponsePayLoad.getStatusCode() == 200) {

                            productname_edt.setText(mySalesEditResponsePayLoad.getMessage().getProductName());
                            productdes_edt.setText(mySalesEditResponsePayLoad.getMessage().getProductDescription());
                            auctionStatus_edt.setText(mySalesEditResponsePayLoad.getMessage().getAuctionStatus());
                            auctionAmount_txt.setText("$ "+mySalesEditResponsePayLoad.getMessage().getAuctionAmount().toString());
                            initialAmount_txt.setText("$ "+mySalesEditResponsePayLoad.getMessage().getInitialAmount().toString());

                            String[] separated = mySalesEditResponsePayLoad.getMessage().getAuctionCloseDate().split(" ");

                            damage_date.setText(separated[0]);
                            damage_time.setText(separated[1]);

                            loadHistoryData(mySalesEditResponsePayLoad);
                        } else {
                            CustomOKAlertDialog(mySalesEditResponsePayLoad.getStatus());
                        }
                    }


                    if (m_progress != null && m_progress.isShowing()) {
                        m_progress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<MySalesEditResponsePayLoad> call, Throwable t) {
                    CustomErrorToast(getResources().getString(R.string.server_not_responding));
                }
            });

        } catch (Exception e) {

            if (m_progress != null && m_progress.isShowing()) {
                m_progress.dismiss();
            }

            CustomErrorToast(getResources().getString(R.string.server_not_responding));


        }
    }

    private void loadHistoryData(MySalesEditResponsePayLoad mySalesEditResponsePayLoad) {
        if (mySalesEditResponsePayLoad.getMessage() != null) {
            if (mySalesEditResponsePayLoad.getMessage().getImagesList().size() > 0) {
                damage_images.setVisibility(View.VISIBLE);
                ProductAdapter ca = new ProductAdapter(mySalesEditResponsePayLoad, getApplicationContext());
                damage_images.setAdapter(ca);
            } else {
                damage_images.setVisibility(View.GONE);
            }
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

        Context mContext;
        MySalesEditResponsePayLoad damageHistoryResPayLoad;

        public ProductAdapter(MySalesEditResponsePayLoad damageHistoryResPayLoad, Context context) {
            this.damageHistoryResPayLoad = damageHistoryResPayLoad;
            mContext = context;
        }

        @Override
        public int getItemCount() {

            return damageHistoryResPayLoad.getMessage().getImagesList().size();

        }


        @Override
        public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.images_list_inflator, viewGroup, false);

            return new ProductAdapter.ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProductAdapter.ProductViewHolder productViewHolder, @SuppressLint("RecyclerView") int position) {


            String url = "";

            if (damageHistoryResPayLoad.getMessage().getImagesList().get(position).getUrl() != null) {
                url = ApplicationContext.BASE_URL + "/" + damageHistoryResPayLoad.getMessage().getImagesList().get(position).getUrl().replace(".png", ".jpg");
            }
            Glide.with(MySalesEditScreen.this)
//                    .load("http://192.168.1.207:8080/img/mob.jpg")
                    .load(url)
                    .error(R.mipmap.icon6)
                    .placeholder(R.mipmap.icon6)
                    .into(productViewHolder.item_img);

            productViewHolder.close_img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageDeleteReq imageDeleteReq = new ImageDeleteReq();
                    imageDeleteReq.setImageId(damageHistoryResPayLoad.getMessage().getImagesList().get(position).getId());
                    getImageDeleteService(imageDeleteReq);
                }
            });

        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            protected ImageView item_img,close_img;

            public ProductViewHolder(View v) {
                super(v);
                item_img = v.findViewById(R.id.item_img);
                close_img = v.findViewById(R.id.close_img);


            }
        }

    }


    public void getImageDeleteService(ImageDeleteReq imageDeleteReq) {

        try {


            m_progress1 = new ProgressDialog(MySalesEditScreen.this);
            m_progress1.setMessage("Please wait....");
            m_progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress1.setIndeterminate(true);
            m_progress1.setProgress(0);
            m_progress1.setCancelable(false);
            m_progress1.show();


            Call<ImageDeleteRes> damageHistoryResPayLoadCall = restAPI.getImageDeleteResCall(imageDeleteReq);
            damageHistoryResPayLoadCall.enqueue(new Callback<ImageDeleteRes>() {
                @Override
                public void onResponse(Call<ImageDeleteRes> call, Response<ImageDeleteRes> response) {
                    if (response.isSuccessful()) {

                        ImageDeleteRes responsePayLoad = response.body();

                        Gson gson = new Gson();
                        gson.toJson(responsePayLoad);
                        Log.v("damage his", "damge his res" + gson.toJson(responsePayLoad));

                        if (responsePayLoad.getStatusCode() == 200) {
                            CustomErrorToast(responsePayLoad.getMessage());
                            if (!checkInternet()) {
                                CustomErrorToast(getResourceStr(context, R.string.plz_chk_your_net));
                            } else {
                                getMySalesService(type);
                            }
                        } else {
                            CustomErrorToast(responsePayLoad.getMessage());
                        }


                    }


                    if (m_progress1 != null && m_progress1.isShowing()) {
                        m_progress1.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ImageDeleteRes> call, Throwable t) {
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