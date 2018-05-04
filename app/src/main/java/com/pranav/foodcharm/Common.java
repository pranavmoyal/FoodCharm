package com.pranav.foodcharm;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.pranav.foodcharm.view.TextView;

import java.util.List;
import java.util.Locale;


/**
 * Created by pranav on 5/5/17.
 */

public class Common {
    private static Dialog dialogMapMain;
    private static Dialog dialogMessage;
    public static final boolean showLogs = true;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static Dialog dialogAddBuddy;

    public static void showDialog(final Context context, String msg) {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog,
                    null, false);

            if (dialogMapMain != null) {
                dialogMapMain.dismiss();
            }

            try {
                dialogMapMain = new Dialog(context);
                dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMapMain.setContentView(view);
                //dialogMapMain.setCancelable(false);
                dialogMapMain.setCanceledOnTouchOutside(true);
                dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Setting Dialog Title

            TextView ok = (TextView) view.findViewById(R.id.exit_ok);
            ok.setText("OK");
            TextView cancel = (TextView) view.findViewById(R.id.exit_cancel);
            cancel.setVisibility(View.GONE);

            // Setting Dialog Message
            ((TextView) view.findViewById(R.id.msg)).setText(msg);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMapMain.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMapMain.dismiss();
                }
            });

            dialogMapMain.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isGpsOn(Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void checkGpsAndsaveLocationAddress(final Context context, String pageName, LocationListener locationListener) {
        try {
            if (Common.showLogs)
                Log.v(pageName, "checkGpsAndsaveLocationAddress isGpsOn true called");

            Location location = Common.getCurrentLocation(context, pageName, locationListener);
            AppControllerYokhel.mLastLocation = Common.getCurrentLocation(context, pageName, locationListener);

            if (location != null) {
                double latitude = location.getLatitude(), longitude = location.getLongitude();

                if (Common.showLogs)
                    Log.v(pageName, "checkGpsAndsaveLocationAddress latitude.." + latitude);
                if (Common.showLogs)
                    Log.v(pageName, "checkGpsAndsaveLocationAddress longitude)" + longitude);

                AppControllerYokhel.latitute = latitude;
                AppControllerYokhel.longitude = longitude;

                /*if (latitude != 0.0 && longitude != 0.0 && latitude != 0 && longitude != 0) {
                    if (Common.showLogs)
                        Log.v(pageName, "checkGpsAndsaveLocationAddress save user location");
//                    Common.getAddressFromLocation(latitude, longitude, context,pageName);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Geocoder gCoder = new Geocoder(context);
                            try {
                                final List<Address> list = gCoder.getFromLocation(
                                        AppControllerYokhel.latitute, AppControllerYokhel.longitude, 1);
                                if (list != null && list.size() > 0) {
                                    Address address = list.get(0);
                                    StringBuilder sb = new StringBuilder();
                                    if (address.getAddressLine(0) != null) {
                                        sb.append(address.getAddressLine(0)).append("\n");
                                    }
                                    sb.append(address.getLocality()).append(",");
                                    sb.append(address.getPostalCode()).append(",");
                                    sb.append(address.getCountryName());
                                    String strAddress = sb.toString();
                                    AppControllerYokhel.full_address = strAddress;
                                }

                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    if (Common.showLogs)
                        Log.v(pageName, "checkGpsAndsaveLocationAddress save user location latitude.." + latitude + " and longitude.." + longitude);
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAddressFromLocation(final double latitude, final double longitude, final Context context, final String pageName) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);

                    AppControllerYokhel.mAddress = address.getAddressLine(0);

                    if (AppControllerYokhel.mAddress.contains("Unnamed Rd")) {
                        AppControllerYokhel.mAddress = AppControllerYokhel.mAddress.replace("Unnamed Rd,", "");
                    } else if (AppControllerYokhel.mAddress.contains(address.getCountryName())) {
                        AppControllerYokhel.mAddress = AppControllerYokhel.mAddress.replace(address.getCountryName(), "");
                    } else if (AppControllerYokhel.mAddress.contains(AppControllerYokhel.mAddress)) {
                        AppControllerYokhel.mAddress = address.getAddressLine(0);
                    }
                    if (address.getSubAdminArea() == null) {
                        AppControllerYokhel.city = address.getLocality();
                    } else {
                        AppControllerYokhel.city = address.getSubAdminArea();
                    }

                    AppControllerYokhel.state = address.getAdminArea();
                    AppControllerYokhel.country = address.getCountryName();

                    AppControllerYokhel.full_address = AppControllerYokhel.mAddress + ", " + AppControllerYokhel.city + ", " + AppControllerYokhel.state + ", " + AppControllerYokhel.country;

                    if (AppControllerYokhel.full_address.contains("Unnamed Rd")) {
                        AppControllerYokhel.full_address = AppControllerYokhel.full_address.replace("Unnamed Rd,", "");
                    } else if (AppControllerYokhel.full_address.contains("null")) {
                        AppControllerYokhel.full_address = AppControllerYokhel.full_address.replace("null,", AppControllerYokhel.city);
                    }

                    AppControllerYokhel.latitute = latitude;
                    AppControllerYokhel.longitude = longitude;

                    if (Common.showLogs)
                        Log.v("Common", "getAddressFromLocation() mAddress is " + AppControllerYokhel.mAddress);
                    if (Common.showLogs)
                        Log.v("Common", "getAddressFromLocation() city is " + AppControllerYokhel.city);
                    if (Common.showLogs)
                        Log.v("Common", "getAddressFromLocation() state is " + AppControllerYokhel.state);
                    if (Common.showLogs)
                        Log.v("Common", "getAddressFromLocation() country is " + AppControllerYokhel.country);
                    if (Common.showLogs)
                        Log.v("Common", "getAddressFromLocation() full_address is " + AppControllerYokhel.full_address);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Location getCurrentLocation(Context context, String pageName, LocationListener locationListener) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location;
            if (locationManager != null) {
                boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (Common.showLogs)
                    Log.v(pageName, "Location networkIsEnabled " + networkIsEnabled);
                if (Common.showLogs) Log.v(pageName, "Location gpsIsEnabled " + gpsIsEnabled);

                if (networkIsEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location == null) {
                        if (gpsIsEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location == null) {
                                return null;
                            } else {
                                return location;
                            }
                        } else {
                            return null;
                        }
                    } else {
                        return location;
                    }

                } else if (gpsIsEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    return location;
                } else {
                    return null;
                }
            } else {
                if (Common.showLogs)
                    Log.v(pageName, "Location Service Location Manager is null");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPMPermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }


}
