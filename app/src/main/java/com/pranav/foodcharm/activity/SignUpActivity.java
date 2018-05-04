package com.pranav.foodcharm.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

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
import com.pranav.foodcharm.CustomToast;
import com.pranav.foodcharm.InternalStorageContentProvider;
import com.pranav.foodcharm.R;
import com.pranav.foodcharm.Utilities;
import com.pranav.foodcharm.adapter.LocationListAdapter;
import com.pranav.foodcharm.interfaces.LocationClickListener;
import com.pranav.foodcharm.model.LocationListModel;
import com.pranav.foodcharm.model.User;
import com.pranav.foodcharm.view.CustomEditText;
import com.pranav.foodcharm.view.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import eu.janmuller.android.simplecropimage.CropImage;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,LocationListener, LocationClickListener {

    CustomEditText et_name,et_email,et_password,et_phone;
    TextView tv_food_spotting,tv_sp_country_code;
    ImageView iv_camera,iv_cancel;
    CircleImageView cv_profile;

    private static final String TAG = "SignUpActivity";
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

    private Dialog dialog;
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private File mFileTemp;
    private String filePath = "";

    private DatabaseHelper databaseHelper;
    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        init();
        inItView();
        setListener();
        initObjects();
    }

    private void inItView(){
        tv_food_spotting=(TextView) findViewById(R.id.tv_join_foodspotting);
        et_name=(CustomEditText) findViewById(R.id.et_name);
        et_email=(CustomEditText) findViewById(R.id.et_email);
        et_password=(CustomEditText) findViewById(R.id.et_password);
        tv_sp_country_code=(TextView) findViewById(R.id.tv_sp_country_code);
        et_phone=(CustomEditText) findViewById(R.id.et_phone);
        iv_camera=(ImageView) findViewById(R.id.iv_camera);
        cv_profile=(CircleImageView) findViewById(R.id.cv_profile);
        iv_cancel=(ImageView) findViewById(R.id.iv_cancel);

    }

    private void setListener(){
        tv_food_spotting.setOnClickListener(this);
        tv_sp_country_code.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_join_foodspotting:
                postDataToSQLite();
                break;

            case R.id.tv_sp_country_code:
                showLocationList(SignUpActivity.this);
                break;

            case R.id.iv_camera:
                try{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.System.canWrite(SignUpActivity.this)) {
                            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, 2909);
                        } else {
                            // continue with your code
                            showDialog(SignUpActivity.this);
                        }
                    } else {
                        // continue with your code
                        showDialog(SignUpActivity.this);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(this);
        user = new User();
    }

    private void postDataToSQLite() {
        String firstName = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String countryCode = tv_sp_country_code.getText().toString().trim();
        if (!firstName.isEmpty() && !firstName.equals(" ")) {
            if (!email.isEmpty() && Utilities.isEmailValid(email)) {
                if (phone.matches("-?\\d+(.\\d+)?") && Utilities.isValidMobile(phone)) {
                    if (!countryCode.isEmpty() && !countryCode.equals(" ")) {
                        if (Utilities.isPasswordValid(password)) {
                            Utilities.hideKeyboard(SignUpActivity.this);
                        } else
                        if (!password.isEmpty()) {
                            new CustomToast().Show_Toast(this, et_password, getResources().getString(R.string.success_message));
                        }else {
                            new CustomToast().Show_Toast(this, et_password, getResources().getString(R.string.enter_valid_password_length));
                        }
                    } else
                        new CustomToast().Show_Toast(this, et_phone,getResources().getString(R.string.please_select_country_code));
                } else
                    new CustomToast().Show_Toast(this, et_phone,getResources().getString(R.string.enter_valid_phone));
            } else
                new CustomToast().Show_Toast(this, et_email,getResources().getString(R.string.enter_valid_email));
        } else
            new CustomToast().Show_Toast(this, et_name,getResources().getString(R.string.enter_valid_first_name));


        if (!databaseHelper.checkUser(et_email.getText().toString().trim())) {

            user.setName(et_name.getText().toString().trim());
            user.setEmail(et_email.getText().toString().trim());
            user.setPassword(et_password.getText().toString().trim());

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            new CustomToast().Show_Toast(this, et_password, getResources().getString(R.string.success_message));
            Intent ob = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(ob);


        } else {
            // Snack Bar to show error message that record already exists
            new CustomToast().Show_Toast(this, et_password, getResources().getString(R.string.email_exist));
        }


    }

    public void showDialog(final Context context) {
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mFileTemp = new File(Environment.getExternalStorageDirectory(), AppControllerYokhel.TEMP_PHOTO_FILE_NAME);
            } else {
                mFileTemp = new File(context.getFilesDir(), AppControllerYokhel.TEMP_PHOTO_FILE_NAME);
            }

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_button_view,
                    null, false);

            if (dialog != null) {
                dialog.dismiss();
            }
            try {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(view);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Setting Dialog Title

            TextView gallery = (TextView) view.findViewById(R.id.tv_first);
            gallery.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_popup_gallery), null, null, null);
            TextView camera = (TextView) view.findViewById(R.id.tv_second);
            camera.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_popup_camera), null, null, null);


            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    openGallery();
                }
            });
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    takePicture();
                }
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                /*
                 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
                 */
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void startCropImage() {

        Intent intent = new Intent(getApplicationContext(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    private void init() {
        readCountryCode();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showDialogLocation(SignUpActivity.this, getResources().getString(R.string.location_permission));
            } else {
                if (Common.isGpsOn(SignUpActivity.this)) {
                    Common.checkGpsAndsaveLocationAddress(SignUpActivity.this, TAG, this);
                    done_once = false;
                    new SignUpActivity.GetLocation().execute();

                } else {
                    displayLocationSettingsRequest(SignUpActivity.this);
                }

            }
        } else {
            if (Common.isGpsOn(SignUpActivity.this)) {
                done_once = false;
                Common.checkGpsAndsaveLocationAddress(SignUpActivity.this, TAG, this);
                new SignUpActivity.GetLocation().execute();
            } else {
                displayLocationSettingsRequest(SignUpActivity.this);
            }
        }

    }

    class GetLocation extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(SignUpActivity.this);
            }
            progressDialog.setMessage(getResources().getString(R.string.txt_getting_location));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... uri) {
            try {
                if (AppControllerYokhel.latitute != 0.0 && AppControllerYokhel.longitude != 0.0 && AppControllerYokhel.latitute != 0 && AppControllerYokhel.longitude != 0) {
                    Common.getAddressFromLocation(AppControllerYokhel.latitute, AppControllerYokhel.longitude, SignUpActivity.this, TAG);
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

            adapterDetail = new LocationListAdapter(SignUpActivity.this, locationList, this);
            recyclerView_dialog.setAdapter(adapterDetail);
            recyclerView_dialog.setLayoutManager(new LinearLayoutManager(SignUpActivity.this));

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
                                status.startResolutionForResult(SignUpActivity.this, AppControllerYokhel.REQUEST_CHECK_SETTINGS);
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
                            Common.checkGpsAndsaveLocationAddress(SignUpActivity.this, TAG, this);
                            new SignUpActivity.GetLocation().execute();
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
                case REQUEST_CODE_GALLERY:
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();

                        startCropImage();

                    } catch (Exception e) {

                        Log.e(TAG, "Error while creating temp file", e);
                    }

                    break;
                case REQUEST_CODE_TAKE_PICTURE:

                    startCropImage();
                    break;
                case REQUEST_CODE_CROP_IMAGE:
                    filePath = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (filePath == null) {
                        return;
                    }
                    bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                    if (bitmap != null) {
                        cv_profile.setImageBitmap(bitmap);
                    }
                    break;
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
//                    ImagePicker.pickImage(Register.this, "Select your image:");
                    showDialog(SignUpActivity.this);
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }

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
                    if (Common.isGpsOn(SignUpActivity.this)) {
                        done_once = false;
                        Common.checkGpsAndsaveLocationAddress(SignUpActivity.this, TAG, this);
                        new SignUpActivity.GetLocation().execute();

                    } else {
                        displayLocationSettingsRequest(SignUpActivity.this);
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
