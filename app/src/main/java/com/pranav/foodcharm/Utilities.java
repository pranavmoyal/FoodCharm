package com.pranav.foodcharm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * Created by parshant on 4/5/17.
 */

public class Utilities extends Activity {
    private static final Pattern hasUsernameSpecialChar1 = Pattern.compile("[\\W]");
    private static final Pattern hasUsernameAllChar = Pattern.compile("[a-zA-Z0-9]");
    private final static String salt = "enc#pwd#rel#u#rec";
    private final static int PIC_CROP = 2;
    private static Uri mCropImagedUri;
    private static Dialog dialogMapMain;

    public static void hideKeyboard(Context context) {
        try {
            View view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void ToastMessage(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public static void setKeyboardVisibility(final Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public static void setKeyboardVisibilityPan(final Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public static boolean checkNetworkConnection(Context paramContext) {
        int i = 1;
        boolean flag = true;
        ConnectivityManager connectivity = (ConnectivityManager) paramContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo localNetworkInfo1 = connectivity.getNetworkInfo(i);
            NetworkInfo localNetworkInfo2 = connectivity.getActiveNetworkInfo();
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            System.out.println("wifi" + localNetworkInfo1.isAvailable());
            System.out.println("info" + localNetworkInfo2);

            if (((localNetworkInfo2 == null) || (!localNetworkInfo2
                    .isConnected())) && (!localNetworkInfo1.isAvailable()))
                i = 0;
            if (info != null) {
                for (int j = 0; j < info.length; j++)
                    if (info[j].getState() == NetworkInfo.State.CONNECTED) {
                        i = 1;
                        break;
                    } else
                        i = 0;
            }

        } else
            i = 0;

        if (i == 0)
            flag = false;
        if (i == 1)
            flag = true;

        return flag;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();

    }

    public static boolean isValidName(String name) {
        String expression = "[():@#$%&_+\\*=~^`“‘,.:;!?/\\{}\\[\\]<>0123456789*$]+";
        CharSequence inputStr = name;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();

    }

    public static boolean isPasswordValid(String password) {
        boolean isValid = false;
        if (password.length() >= 8)
            isValid = true;
        return isValid;
    }


    /*

    public static boolean isPasswordValid(String password) {
        boolean isValid = false;

        String expression = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
        //String expression = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}";
        CharSequence inputStr = password;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }*/

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        CharSequence inputStr = phone;
        if (!Pattern.matches("[a-zA-Z]+", inputStr)) {
//            check = phone.length() == 10 ;
            if (phone.length() < 4 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }

        } else {
            check = false;
        }
        return check;
    }

    public static String validateUsername(String username) {

        StringBuilder retVal = new StringBuilder();
        if (username.isEmpty()) {
            retVal.append("Empty fields \n");
        }
        if (username.length() >= 5) {

            if (hasUsernameAllChar.matcher(username).find()) {

                if ((hasUsernameSpecialChar1.matcher(username).find())) {

                }
            } else {
                retVal.append("Username must have character or digit  \n");
            }

        } else {

            retVal.append("Username is too short. Needs to have atleast 5 characters \n");
        }
        if (retVal.length() == 0) {

            retVal.append("Success");
        }

        return retVal.toString();
    }

    public static String passwordEncryption(String password) {

        String md5 = "";
        if (null == password)
            return null;

        password = password + salt;//adding a salt to the string before it gets hashed.
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(password.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }

    /**
     * Crop the image
     *
     * @return returns <tt>true</tt> if crop supports by the device,otherwise false
     */
    public static boolean performCropImage(Uri mFinalImageUri, Activity context) {
        try {

            if (mFinalImageUri != null) {
                String path = mFinalImageUri.toString().replaceAll("file://", "");
                int aspectX = 750;
                int aspectY = 1011;

                Intent intent = new Intent(context, CropImage.class);
                //send the path to CropImage intent to get the photo you have just taken or selected from gallery
                intent.putExtra(CropImage.IMAGE_PATH, path);

                intent.putExtra(CropImage.SCALE, true);

                intent.putExtra("aspectX", aspectX);
                intent.putExtra("aspectY", aspectY);
             /*   intent.putExtra("outputX", 500);
                intent.putExtra("outputY", 500);*/

                File f = createNewFile("CROP_", context);
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    Log.e("io", ex.getMessage());
                }

                mCropImagedUri = Uri.fromFile(f);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                context.startActivityForResult(intent, PIC_CROP);
                return true;
            }

/*

            if (mFinalImageUri != null) {
                //call the standard crop action intent (the user device may not support it)
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                //indicate image type and Uri
                cropIntent.setDataAndType(mFinalImageUri, "image*//*");

              *//*  //you must setup two line below
                cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);*//*

                //set crop properties
                cropIntent.putExtra("crop", "true");
                //indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("scale", true);
                //indicate output X and Y
                cropIntent.putExtra("outputX", 500);
                cropIntent.putExtra("outputY", 500);
                //retrieve data on return
                cropIntent.putExtra("return-data", true);

                File f = createNewFile("CROP_", context);
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    Log.e("io", ex.getMessage());
                }

                mCropImagedUri = Uri.fromFile(f);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                context.startActivityForResult(cropIntent, PIC_CROP);
                return true;
            }*/
        } catch (Exception anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return false;
    }

    public static File createNewFile(String prefix, Activity context) {
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory() + "/mypics/");
        if (!newDirectory.exists()) {
            if (newDirectory.mkdir()) {
                Log.d(context.getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
            }
        }
        File file = new File(newDirectory, (prefix + System.currentTimeMillis() + ".jpg"));
        if (file.exists()) {
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    public static String getImageFilePath(Intent data, Activity context, Uri ImageUri) {
        String filePath = "";
        Uri selectedImageUri = null;
        if (data != null) {
            try {
                if (data.getData() != null) {
                    selectedImageUri = data.getData();
                    //get the returned data
                } else {
                    if (ImageUri != null) {
                        selectedImageUri = ImageUri;
                    }
                }
                //get the returned data
                // OI FILE Manager
                String filemanagerstring = selectedImageUri != null ? selectedImageUri.getPath() : null;

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri, context);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    //  Toast.makeText(context, "Unknown path", Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }
            } catch (Exception e) {
                //   Toast.makeText(context, "Internal error", Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
                e.printStackTrace();
            }
        } else {
            try {
                //Picking last saved image from camera

                String[] projection = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA};
                String sort = MediaStore.Images.ImageColumns._ID + " DESC";

                Cursor cursor = context.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sort);

                try {
                    cursor.moveToFirst();

                    String largeImagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
                    selectedImageUri = Uri.fromFile(new File(largeImagePath));
                } finally {
                    //cursor.close();
                }
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri, context);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    // Toast.makeText(context, "Unknown path", Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }
            } catch (Exception e) {
                Log.e("RetrofitError", e.getMessage());
                e.printStackTrace();
            }
        }
        return filePath;
    }

    @SuppressLint("NewApi")
    public static String getPath(Uri uri, Activity context) {
        try {
            if (uri.toString().contains("document")) {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(uri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = {MediaStore.Images.Media.DATA};

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor1 = context.getContentResolver().query(getUri(), column, sel, new String[]{id}, null);

                String filePath = "";
                int columnIndex = cursor1 != null ? cursor1.getColumnIndex(column[0]) : 0;

                if (cursor1.moveToFirst()) {
                    filePath = cursor1.getString(columnIndex);
                }
                return filePath;
            } else {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.managedQuery(uri, projection, null, null, null);
                if (cursor != null) {
                    // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
                    // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();

                    return cursor.getString(column_index);
                } else
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // By using this method get the Uri of Internal/External Storage for Media
    private static Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public static String createMd5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }

    public static <T> String checkNull(T object) {
        if (object == null)
            return "";
        else
            return object.toString();
    }

    public static boolean checkPlayServices(Context context, String TAG) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.v(TAG, "checkPlayServices utill This device is not supported.");
            return false;
        }
        return true;
    }


}
