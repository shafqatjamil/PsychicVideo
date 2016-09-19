package avreye.mytarotadvisor;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import avreye.mytarotadvisor.Object.Payload;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.utils.Constants;
public class PubnubService extends Service {
    String channel = "Channel_User_84";
    public static Pubnub pubnub;
    PowerManager.WakeLock wl = null;
    DatabaseHelper databaseHelper;
    AppController appController;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String pnMsg = msg.obj.toString();
            final Toast toast = Toast.makeText(getApplicationContext(), pnMsg, Toast.LENGTH_SHORT);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 200);
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    private void notifyUser(Object message) {
        Message msg = handler.obtainMessage();
        try {
            final String obj =  message.toString();
            msg.obj = obj;
            handler.sendMessage(msg);
            Log.i("Received msg : ", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onCreate() {
        super.onCreate();




        databaseHelper = new DatabaseHelper(getApplicationContext());
        appController = (AppController) getApplicationContext();
        channel =  new UserSession(this).getUserChannelName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SubscribeAtBoot");
        if (wl != null) {
            wl.acquire();
            Log.e("PUBNUB", "Partial Wake Lock : " + wl.isHeld());
        }
        Log.e("PUBNUB", "PubnubService created...");
        pubnub = new Pubnub(Constants.PUBNUB_PUBLISH_KEY, Constants.PUBNUB_SUBSCRIBE_KEY);
        try {
            pubnub.subscribe(channel.toString(), new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());


                        }
                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }
                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }
                        @Override
                        public void successCallback(String channel, Object message) {
                            Payload payload  = new Gson().fromJson(message.toString(), Payload.class);
                            Log.e("recieved msg",payload.toString());
                            avreye.mytarotadvisor.Object.Message message1 = new avreye.mytarotadvisor.Object.Message(2,payload.getPnApns().getAps().getSenderId(),payload.getPnApns().getAps().getSenderDisplayname(),
                                    payload.getReceiverId(),payload.getReceiverDisplayname(),payload.getMsgText(),payload.getMsgDate(),Integer.parseInt(payload.getStatus()),
                                    payload.getVideoUrl(),payload.getMessageType(),payload.getSenderType(),payload.getReceiverType(),payload.getReviewStatus(),
                                    "none".toString(),""+( Integer.parseInt(payload.getReceiverId()) + Integer.parseInt(payload.getSenderId())) );
                            databaseHelper.insertMessageToDB(message1);
                            if(appController.GetActivity() == null)
                            {

                                Log.e("test recieving msg",payload.getMsgText());
                              //  notifyUser(payload.getMsgText());
                            }
                            else
                            {
                                Intent i = new Intent("message_receiver");
                                i.putExtra("message", message.toString());
                                LocalBroadcastManager.getInstance(PubnubService.this).sendBroadcast(i);
                            }
                        }
                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );
        } catch (PubnubException e) {
            e.printStackTrace();
        }
    }
    private void sendUpdateMessage() {
        Intent intent = new Intent("message_receiver");
        LocalBroadcastManager.getInstance(PubnubService.this).sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wl != null) {
            wl.release();
            Log.i("PUBNUB", "Partial Wake Lock : " + wl.isHeld());
            Toast.makeText(this, "Partial Wake Lock : " + wl.isHeld(), Toast.LENGTH_LONG).show();
            wl = null;
        }
        Toast.makeText(this, "PubnubService destroyed...", Toast.LENGTH_LONG).show();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}