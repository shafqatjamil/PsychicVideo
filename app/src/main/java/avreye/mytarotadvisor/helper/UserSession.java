package avreye.mytarotadvisor.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserSession {
    private final String USER_TYPE = "user_type";
    private final String USER_ID = "user_id";
    private final String USER_NAME = "user_name";
    private final String USER_EMAIL = "user_email";
    private final String USER_DOB = "user_dob";
    private final String USER_CREDITS = "user_credits";
    private final String USER_ORDERS = "user_orders";
    private final String IS_LOGGED_IN = "is_logged_in";
    private final String USER_CHANNEL_NAME = "user_channel_name";
    private final String ADVISOR_STATUS = "advisor_status";
    private final String FCM_TOKEN = "fcm_token";

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private  Context mContext;

    public UserSession(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        this.mContext = context;
    }



    private static UserSession instance;

    public static UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }





    public String getADVISOR_STATUS() {
        return sp.getString(ADVISOR_STATUS, "");
    }


    public String getFCM_TOKEN() {
        return sp.getString(FCM_TOKEN, "");
    }


    public boolean setFCM_TOKEN(String status) {
        spEditor = sp.edit();
        spEditor.putString(FCM_TOKEN, status);
        spEditor.apply();
        return true;
    }
    public boolean setADVISOR_STATUS(String status) {
        spEditor = sp.edit();
        spEditor.putString(ADVISOR_STATUS, status);
        spEditor.apply();
        return true;
    }
    public boolean setUserId(String id) {
        spEditor = sp.edit();
        spEditor.putString(USER_ID, id);
        spEditor.apply();
        return true;
    }

    public String getUserId() {
        return sp.getString(USER_ID, "");
    }

    public boolean setUserName(String name) {
        spEditor = sp.edit();
        spEditor.putString(USER_NAME, name);
        spEditor.apply();
        return true;
    }

    public String getUserName() {
        return sp.getString(USER_NAME, "");
    }

    public boolean setUserEmail(String email) {
        spEditor = sp.edit();
        spEditor.putString(USER_EMAIL, email);
        spEditor.apply();
        return true;
    }

    public String getUserEmail() {
        return sp.getString(USER_EMAIL, "");
    }

    public boolean setUserType(String type) {
        spEditor = sp.edit();
        spEditor.putString(USER_TYPE, type);
        spEditor.apply();
        return true;
    }

    public String getUserType() {
        return sp.getString(USER_TYPE, "");
    }

    public boolean setUserDOB(String dob) {
        spEditor = sp.edit();
        spEditor.putString(USER_DOB, dob);
        spEditor.apply();
        return true;
    }

    public String getUserDOB() {
        return sp.getString(USER_DOB, "");
    }

    public boolean setUserCredits(String credits) {
        spEditor = sp.edit();
        spEditor.putString(USER_CREDITS, credits);
        spEditor.apply();
        return true;
    }

    public String getUserCredits() {
        return sp.getString(USER_CREDITS, "0");
    }

    public boolean setUserOrders(String orders) {
        spEditor = sp.edit();
        spEditor.putString(USER_ORDERS, orders);
        spEditor.apply();
        return true;
    }

    public String getUserOrders() {
        return sp.getString(USER_ORDERS, "0");
    }

    public boolean setIsLoggedIn(boolean islogin) {
        spEditor = sp.edit();
        spEditor.putBoolean(IS_LOGGED_IN, islogin);
        spEditor.apply();
        return true;
    }

    public boolean getIsUserLoggedIn() {
        return sp.getBoolean(IS_LOGGED_IN, false);
    }

    public boolean setUserChannelName(String channel_name) {
        spEditor = sp.edit();
        spEditor.putString(USER_CHANNEL_NAME, channel_name);
        spEditor.apply();
        return true;
    }
    public String getUserChannelName() {
        return sp.getString(USER_CHANNEL_NAME, "");
    }

    public boolean destroyUserSession() {
        spEditor = sp.edit();
        spEditor.remove(IS_LOGGED_IN);
        spEditor.remove(USER_EMAIL);
        spEditor.remove(USER_CREDITS);
        spEditor.remove(USER_DOB);
        spEditor.remove(USER_ID);
        spEditor.remove(USER_TYPE);
        spEditor.remove(USER_NAME);
        spEditor.remove(USER_ORDERS);
        spEditor.remove(USER_CHANNEL_NAME);
        spEditor.apply();
        new DatabaseHelper(mContext).FlushDatabase();
        return true;
    }

    public void setThumbnailUri(String key, String value)
    {
        spEditor = sp.edit();
        spEditor.putString(key, value);
        spEditor.apply();
    }
    public String getThumbnailUri(String key) {
        return sp.getString(key, "content://media/external/images/media/465");
    }
    public void setUserProfilePictureUrl(String id, String url)
    {
        spEditor = sp.edit();
        spEditor.putString(id, url);
        spEditor.apply();
    }
    public String getUserProfilePictureUrl(String id)
    {
        return sp.getString(id, "content://media/external/images/media/465");
    }
}