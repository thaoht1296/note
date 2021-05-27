package com.example.thaonote.dbhelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class IntentExtras {
    public static final String GITHUB_URL="https://github.com/thaoht170";
    public static final String FACEBOOK_URL="https://www.facebook.com/thaoht.1296/";
    public static final String OUTLOOK="thaoht.b17at169@stu.ptit.edu.vn";
    public static final String EMAIL="thao.itptit@gmail.com";
    public static final String YOUTUBE="https://www.youtube.com/channel/UC1yGsMqgaoa5aDXby9k2DqA";

    //find on github
    public static void findOnGithub(Context context){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
        context.startActivity(intent);
    }

    //find on facebook
    public static void findOnFacebook(Context context){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        context.startActivity(intent);
    }

    //find on outlook
    public static void rateOnPlayStore(Context context){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(OUTLOOK));
        context.startActivity(intent);
    }

    //find on youtube
    public static void findMoreApps(Context context){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE));
        context.startActivity(intent);
    }

    //find on email
    public static void shareThisApp(Context context){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,EMAIL);
        context.startActivity(Intent.createChooser(intent,"Thanks you"));
    }
}
