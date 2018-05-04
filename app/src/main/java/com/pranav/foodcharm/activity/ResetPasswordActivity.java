package com.pranav.foodcharm.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.pranav.foodcharm.AppControllerYokhel;
import com.pranav.foodcharm.Common;
import com.pranav.foodcharm.R;
import com.pranav.foodcharm.adapter.LocationListAdapter;
import com.pranav.foodcharm.interfaces.LocationClickListener;
import com.pranav.foodcharm.model.LocationListModel;
import com.pranav.foodcharm.view.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener,LocationListener, LocationClickListener {

    TextView et_next,tv_sp_country_code;

    private static final String TAG = "ResetPasswordActivity";
    private TextView tv_done;
    private Dialog dialogMapMain_location;
    private Dialog dialogMapMain;
    private ProgressDialog progressDialog;
    private List<LocationListModel> locationList = new ArrayList<>();
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 2;
    private static final int PERMISSION_REQUEST_CROSS_LOCATION = 1;
    public static boolean done_once = true;
    private RecyclerView recyclerView_dialog;
    LocationListAdapter adapterDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);

        inItView();
        init();
        setListener();
    }

    private void setListener(){
        tv_sp_country_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           /* case R.id.et_next:

                *//*Intent intent=new Intent(ResetPasswordActivity.this,SignupStepFour.class);
                startActivity(intent);*//*

                break;*/

            case R.id.tv_sp_country_code:
                showLocationList(ResetPasswordActivity.this);
                break;
        }
    }

    private void inItView(){
        tv_sp_country_code=(TextView) findViewById(R.id.tv_sp_country_code);
    }

    private void init() {
        readCountryCode();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showDialogLocation(ResetPasswordActivity.this, getResources().getString(R.string.location_permission));
            } else {
                if (Common.isGpsOn(ResetPasswordActivity.this)) {
                    Common.checkGpsAndsaveLocationAddress(ResetPasswordActivity.this, TAG, this);
                    done_once = false;
                    new ResetPasswordActivity.GetLocation().execute();

                } else {
                    displayLocationSettingsRequest(ResetPasswordActivity.this);
                }

            }
        } else {
            if (Common.isGpsOn(ResetPasswordActivity.this)) {
                done_once = false;
                Common.checkGpsAndsaveLocationAddress(ResetPasswordActivity.this, TAG, this);
                new ResetPasswordActivity.GetLocation().execute();
            } else {
                displayLocationSettingsRequest(ResetPasswordActivity.this);
            }
        }

    }

    class GetLocation extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ResetPasswordActivity.this);
            }
            progressDialog.setMessage(getResources().getString(R.string.txt_getting_location));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... uri) {
            try {
//                Common.checkGpsAndsaveLocationAddress(Register.this, TAG,this);
                if (AppControllerYokhel.latitute != 0.0 && AppControllerYokhel.longitude != 0.0 && AppControllerYokhel.latitute != 0 && AppControllerYokhel.longitude != 0) {
                    Common.getAddressFromLocation(AppControllerYokhel.latitute, AppControllerYokhel.longitude, ResetPasswordActivity.this, TAG);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try {
                if (AppControllerYokhel.country != null && !AppControllerYokhel.country.isEmpty()) {
                    progressDialog.dismiss();
                    readCountryCode();
                } else {
                    progressDialog.dismiss();
                    readCountryCode();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showLocationList(final Context context) {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_location_list,
                    null, false);

            if (dialogMapMain_location != null) {
                dialogMapMain_location.dismiss();
            }
            try {
                dialogMapMain_location = new Dialog(context);
                dialogMapMain_location.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMapMain_location.setContentView(view);
                dialogMapMain_location.setCancelable(false);
                dialogMapMain_location.setCanceledOnTouchOutside(true);
                dialogMapMain_location.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_done = (TextView) view.findViewById(R.id.tv_done);
            recyclerView_dialog = (RecyclerView) view.findViewById(R.id.recyclerView);

            adapterDetail = new LocationListAdapter(ResetPasswordActivity.this, locationList, this);
            recyclerView_dialog.setAdapter(adapterDetail);
            recyclerView_dialog.setLayoutManager(new LinearLayoutManager(ResetPasswordActivity.this));
//            doSearch();

            tv_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMapMain_location.dismiss();
                }
            });
            dialogMapMain_location.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogLocation(final Context context, String msg) {
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
                dialogMapMain.setCancelable(false);
                dialogMapMain.setCanceledOnTouchOutside(false);
                dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } catch (Exception e) {
                e.printStackTrace();
            }
            TextView ok = (TextView) view.findViewById(R.id.exit_ok);
            ok.setText("OK");
            TextView cancel = (TextView) view.findViewById(R.id.exit_cancel);
            cancel.setVisibility(View.VISIBLE);
            cancel.setText("Cancel");
            ((TextView) view.findViewById(R.id.msg)).setText(msg);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMapMain.dismiss();
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SYSTEM_ALERT_WINDOW},
                            PERMISSION_REQUEST_CROSS_LOCATION);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMapMain.dismiss();
                    readCountryCode();
                }
            });

            dialogMapMain.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayLocationSettingsRequest(final Context mContext) {
        try {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.i(TAG, "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                            try {
                                done_once = false;
                                status.startResolutionForResult(ResetPasswordActivity.this, AppControllerYokhel.REQUEST_CHECK_SETTINGS);
                                Log.e(TAG, "--isCanceled--" + status.isCanceled() + "---isInterrupted---" + status.isInterrupted() + "--status.isSuccess()-- " + status.isSuccess());
                            } catch (IntentSender.SendIntentException e) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "requestCode---" + requestCode);
        Log.e(TAG, "resultCode---" + resultCode);
        try {
            if (resultCode != RESULT_OK) {

                return;
            }
            Bitmap bitmap;

            switch (requestCode) {
                case AppControllerYokhel.REQUEST_CHECK_SETTINGS: {
                    Log.e(TAG, resultCode + "  resultCode---intent---" + data.getData());
                    switch (resultCode) {
                        case Activity.RESULT_OK: {
                            done_once = false;
                            Common.checkGpsAndsaveLocationAddress(ResetPasswordActivity.this, TAG, this);
                            new ResetPasswordActivity.GetLocation().execute();
                            break;
                        }
                        case Activity.RESULT_CANCELED: {
                            done_once = true;
                            readCountryCode();
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CROSS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SYSTEM_ALERT_WINDOW},
                            PERMISSION_REQUEST_FINE_LOCATION);

                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    readCountryCode();
                }
                return;
            }
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    if (Common.isGpsOn(ResetPasswordActivity.this)) {
                        done_once = false;
                        Common.checkGpsAndsaveLocationAddress(ResetPasswordActivity.this, TAG, this);
                        new ResetPasswordActivity.GetLocation().execute();

                    } else {
                        displayLocationSettingsRequest(ResetPasswordActivity.this);
                    }

                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    readCountryCode();
                }
                return;
            }
        }
    }

    private void readCountryCode() {
        locationList.clear();

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
                    "CountryCode.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String myjsonstring = sb.toString();
        // Try to parse JSON
        try {
            // Creating JSONObject from String
            JSONObject jsonObjMain = new JSONObject(myjsonstring);

            // Creating JSONArray from JSONObject
            JSONArray jsonArray = jsonObjMain.getJSONArray("countries");

            // JSONArray has x JSONObject
            for (int i = 0; i < jsonArray.length(); i++) {
                LocationListModel locationListModel = new LocationListModel();
                // Creating JSONObject from JSONArray
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String strCode = jsonObj.getString("code");
                String strName = jsonObj.getString("name");

                locationListModel.setNameWithCode(strName + "(" + strCode + ")");
                locationListModel.setName(strName);
                locationListModel.setCode(strCode);

                locationList.add(locationListModel);
            }
            setCountryCodeAdapter();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void setCountryCodeAdapter() {

        if (AppControllerYokhel.country != null && !AppControllerYokhel.country.isEmpty()) {
            for (int i = 0; i < locationList.size(); i++) {
                if (AppControllerYokhel.country.equals(locationList.get(i).getName())) {
                    tv_sp_country_code.setText("("+locationList.get(i).getCode()+")");
                    break;
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationItemClicked(LocationListAdapter.MyViewHolder holder, int position, String countryCode, String nameWithCode) {
        tv_sp_country_code.setText("("+countryCode+")");
        if (dialogMapMain_location != null) {
            dialogMapMain_location.dismiss();
        }
    }
}
