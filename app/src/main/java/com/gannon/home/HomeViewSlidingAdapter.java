package com.gannon.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.gannon.R;
import com.gannon.home.model.HomeApproveListRes;
import com.gannon.home.model.HomeListEditResponsePayLoad;
import com.gannon.utils.ApplicationContext;

import java.util.ArrayList;


/**
 * Created by srikanth on
 */


public class HomeViewSlidingAdapter extends PagerAdapter {
    Activity activity;
    ArrayList<String> image_arraylist;
    HomeListEditResponsePayLoad homeCategoryListRes;
    Dialog customDialog;
    private LayoutInflater layoutInflater;

    public HomeViewSlidingAdapter(Activity activity, ArrayList<String> image_arraylist, HomeListEditResponsePayLoad homeCategoryListRes) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.homeCategoryListRes = homeCategoryListRes;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slider_inflater, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.image);

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(activity, VLCActivity.class);
//                intent.putExtra("weburl",homeCategoryListRes.getSlides().get(position).getUrl());
//                activity.startActivity(intent);
//
//
///*
//                if (homeCategoryListRes.getSliders_details().size() > 0) {
//
//                   String imageUrl = homeCategoryListRes.getSliders_details().get(position).getImage();
//                    String title = homeCategoryListRes.getSliders_details().get(position).getTitle();
//                    String weburl = homeCategoryListRes.getSliders_details().get(position).getUrl();
//
//                    Intent intent5 = new Intent(activity, WebViewImageActivitys.class);
//                    intent5.putExtra("titleStr",title);
//                    intent5.putExtra("weburl", weburl);
//                    intent5.putExtra("imageUrl", imageUrl);
//                    activity.startActivity(intent5);
//
//                }
//*/
//
//
//            }
//        });


//        if (damageHistoryResPayLoad.getMessage().get(position).getImageUrl() != null) {
//            url = ApplicationContext.BASE_URL + "/" + damageHistoryResPayLoad.getMessage().get(position).getImageUrl().replace(".png", ".jpg");
//        }
//        Glide.with(HomeActivity.this)
//                .load(url)
//                .error(R.mipmap.icon6)
//                .placeholder(R.mipmap.icon6)
//                .into(productViewHolder.item_img);
//



        Glide.with(activity.getApplicationContext())
                .load(ApplicationContext.BASE_URL + "/" +image_arraylist.get(position))
                .placeholder(R.mipmap.icon6) // optional
                .error(R.mipmap.icon6)         // optional
                .into(im_slider);


        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


//    public void customSlideDialog(String details_str, String name, String image_url) {
//
//        customDialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
//        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
//        customDialog.setContentView(R.layout.custom_slide_dialog_inflater);
//
//        customDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
//                    activity.finish();
//                return false;
//            }
//        });
//
//        ImageView imageView = customDialog.findViewById(R.id.imageView);
//        // WebView webView = customDialog.findViewById(R.id.webview);
//        TextView details_txt = customDialog.findViewById(R.id.details_txt);
//        details_txt.setText(details_str);
//
//        Glide.with(activity)
//                .load(image_url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .thumbnail(0.5f)
//                .crossFade()
//                .into(imageView);
//
//
//        TextView close_btn = customDialog.findViewById(R.id.close_btn);
//        close_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                customDialog.dismiss();
//            }
//        });
//
//
//        customDialog.setCancelable(false);
//        customDialog.show();
//    }

}


