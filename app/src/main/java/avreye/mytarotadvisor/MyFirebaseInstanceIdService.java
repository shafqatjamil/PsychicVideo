package avreye.mytarotadvisor;

import android.util.Log;

import com.appboy.Appboy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.utils.Constants;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token in logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);


        Appboy.getInstance(getApplicationContext()).registerAppboyPushMessages(refreshedToken);
        new UserSession(this).setFCM_TOKEN(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project

        Pubnub pubnub = new Pubnub(Constants.PUBNUB_PUBLISH_KEY, Constants.PUBNUB_SUBSCRIBE_KEY);
        pubnub.enablePushNotificationsOnChannel(new UserSession(getApplicationContext()).getUserChannelName(), token, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                Log.e("sendRServerCalled", message.toString());
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
                Log.e("sendRServerCalledE", error.toString());
            }
        });
    }

}