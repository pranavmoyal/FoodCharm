package com.pranav.foodcharm;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;


public class AppControllerYokhel extends MultiDexApplication {
    public static AppControllerYokhel mInstance;
    public static String SENDER_ID = "897270679235";
    public static final int REQUEST_CHECK_SETTINGS = 1001;
    public static Location mLastLocation;
    public static double latitute = 0.0;
    public static double longitude = 0.0;
    public static String mAddress = "";
    public static String city = "";
    public static String state = "";
    public static String country = "";
    public static String full_address = "";
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final String TEMP_VIDEO_FILE_NAME = "temp_video.mp4";
    FirebaseAnalytics mFirebaseAnalytics;

    public static int counter=0;
    public static Context context;
    public AppControllerYokhel() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MultiDex.install(this);
        try {
            Log.e("AppControllerYokhel", "call AppControllerYokhel");
            mInstance = this;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

    }
}
