package tech.misfit.ifarmer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import tech.misfit.ifarmer.R;
import tech.misfit.ifarmer.view.activities.BaseActivity;

public class ShareInfo {

    private static final ShareInfo ourInstance = new ShareInfo();
    public static final String INDONESIA = "in";
    public static final String ENGLISH = "en";
    public static final int BENEFICIARY_ADD_CODE = 305;

    public static ShareInfo getInstance() {
        return ourInstance;
    }

    private ShareInfo() {
    }

    static {
        System.loadLibrary("native-lib");
    }

    public native String getBaseUrl();
    public native String getRootBaseUrl();

    public int getPxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public int spToPx(float sp) {
        return (int) (sp * Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    public static void goNextPage(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public static void goPreviousPage(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.activity_in_back, R.anim.activity_out_back);
    }

    /*public UserResponseModel.UserAccessModelModel getAccessToken(Context context) {
        String accessInfo = LocalStorage.getInstance().getStringData(context, "user_access_info");
        if (accessInfo == null) return null;
        return new Gson().fromJson(accessInfo, UserResponseModel.class).getUserModel();
    }*/

    public String getMobileNumber(Context context){
        String mobileNumber = LocalStorage.getInstance().getStringData(context, "mobile_number");
        mobileNumber = mobileNumber==null ? "" : mobileNumber;
        return mobileNumber;
    }

    public void setMobileNumber(Context context, String mobileNumber){
        LocalStorage.getInstance().setData(context,"mobile_number",mobileNumber);
    }

    public String getAccountNumber(Context context){
        String mobileNumber = LocalStorage.getInstance().getStringData(context, "account_number");
        mobileNumber = mobileNumber==null ? "" : mobileNumber;
        return mobileNumber;
    }

    public void setAccountNumber(Context context, String accountNumber){
        LocalStorage.getInstance().setData(context,"account_number",accountNumber);
    }

    public String getAuthenticationToken(Context context){
        String auth_token = LocalStorage.getInstance().getStringData(context, "auth_token");
        auth_token = auth_token==null ? "" : auth_token;
        return auth_token;
    }

    public void setAuthenticationToken(Context context, String authToken){
        LocalStorage.getInstance().setData(context,"auth_token", authToken);
    }

    public void saveFCMToken(Context context, String fcmToken){
        Log.e("fcm_token", "SAVE_FCM_TOKEN: "+ String.valueOf(fcmToken));
        LocalStorage.getInstance().setData(context,"fcm_token", fcmToken);
    }

    public String getFCMToken(Context context){
        String fcmToken = LocalStorage.getInstance().getStringData(context, "fcm_token");
        fcmToken = fcmToken==null ? "" : fcmToken;
        Log.e("fcm_token", "GET_FCM_TOKEN: "+ String.valueOf(fcmToken));
        return fcmToken;
    }

    public ArrayList<String> getLocalCachedVideoPaths(Context context){
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(LocalStorage.getInstance().getStringData(context, "local_video_paths"), listType);
    }

    public void saveLocalCachedVideoPaths(Context context, ArrayList<String> localVideoPaths){
        LocalStorage.getInstance().setData(context,"local_video_paths", new Gson().toJson(localVideoPaths));
    }

    public String getUpdatedTime(Context context, String key){

        return LocalStorage.getInstance().getStringData(context,key);
    }

    public void saveUpdatedTime(Context context, String updatedTime, String key){
        LocalStorage.getInstance().setData(context,key, new Gson().toJson(updatedTime));
    }

    public void logout(Context context) {
        LocalStorage.getInstance().clearData(context, "auth_token");
        // todo: GO TO LOGIN SCREEN.
        ((BaseActivity) context).goToLoginScreen();
    }

    public String getAppID() {
        return "Xrd528*wetoLksM3!";
    }

    public String getHandsetModel() {
        return "Android....";
    }


    public String getRequestId() {
        Random rand = new Random();
        return String.valueOf(System.currentTimeMillis() + "" + rand.nextInt(100000));
    }

    public static String getLanguageType(Context context) {
        String language = LocalStorage.getInstance().getStringData(context,"language");
        if(language==null) language = INDONESIA; // default language
        return language;
    }

    public static void setLanguageType(Context context, String language) {
        LocalStorage.getInstance().setData(context,"language",language);
    }

    public String getIMEI(Context context){
        try{
            TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String imei;
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                imei = mngr.getImei();
            } else {
                imei = mngr.getDeviceId();
            }
            return imei;
        } catch (Exception e) {
            return null;
        }
    }

}
