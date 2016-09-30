package avreye.mytarotadvisor.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appboy.Appboy;
import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.Object.AdvisorInfo;
import avreye.mytarotadvisor.Object.AdvisorProfilePictureResponse;
import avreye.mytarotadvisor.Object.GetAdvisorStatusReponse;
import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.Object.Payload;
import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.UserChatActivityAdapter;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.utils.Constants;
import avreye.mytarotadvisor.utils.Util;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivityforUser extends AppCompatActivity {
    Pubnub pubnub;
    Toolbar toolbar;
    ArrayList<Message> messages;
    UserChatActivityAdapter userChatActivityAdapter;
    ListView messageList;
    DatabaseHelper  databaseHelper;
    EditText editText_message;
    ImageView imageview_chat;
    ImageView imageview_video;
    ImageView button_send;
    Button button_requestvideo;
    RelativeLayout relativeLayout_chat;
    AppController appController;
    String AdvisorID;
    String AdvisorName;
    UserSession userSession;
    TextView textView1;
    TextView cost_info;
    Retrofit retrofit;
    Retrofit retrofit1;
    private ProgressDialog progressDialog;
    private UserSession mUserSession;
    boolean isVideoAttached;
    Uri videoUri = null;
    CognitoCachingCredentialsProvider credentialsProvider;
    static final int REQUEST_VIDEO_CAPTURE = 9000;
    private final static int CAMERA_RQ = 6969;
    AmazonS3Client s3Client;
    File file;
    static String VideoName = "";
    static String MessageType = "text";
    private TransferUtility transferUtility;
    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("advisor_id"))
            {
                //setContentView(R.layout.activity_chat_activityfor_user);
                AdvisorName = getIntent().getStringExtra("advisor_name");
                AdvisorID = getIntent().getStringExtra("advisor_id");
                Log.e("ChatActivityforUser","advisor_id Available");
            }
            Log.e("ChatActivityforUser","Extras Available");
        }
        else
        {
            Log.e("ChatActivityforUser","Extras null");
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VideoName = "";
        MessageType = "text";
        setContentView(R.layout.activity_chat_activityfor_user);
        mUserSession = new UserSession(this);
        isVideoAttached = false;
        progressDialog = new ProgressDialog(ChatActivityforUser.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait...");

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        userSession = new UserSession(this);
        appController = (AppController) getApplicationContext();
        appController.SetActivity(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) toolbar.findViewById(R.id.title_text);

        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        TextView textView_credits1 = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        textView_credits.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits1.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits.setTextSize(12f);


        onNewIntent(getIntent());


        textView1 = (TextView)toolbar.findViewById(R.id.toolbar_credits);
        cost_info = (TextView) findViewById(R.id.cost_info);
        cost_info.setText("you are requesting text reply 150 credit");
        textView1.setText(userSession.getUserCredits());
        textView.setText(Html.fromHtml(AdvisorName));
        if(PubnubService.pubnub != null)
            pubnub = PubnubService.pubnub;

        messageList = (ListView) findViewById(R.id.msgListView);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CLIENT_API_BASE_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit1 = new Retrofit.Builder()
                .baseUrl(Constants.Advisor_API_BASE_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        databaseHelper = new DatabaseHelper(this);
        messages = databaseHelper.getAllMessages(AdvisorID);

        userChatActivityAdapter = new UserChatActivityAdapter(this,messages,AdvisorID,userSession.getUserId(),AdvisorName+".jpg");
        assert messageList != null;
        messageList.setAdapter(userChatActivityAdapter);
        messageList.setSelection(userChatActivityAdapter.getCount() - 1);

        imageview_chat = (ImageView)findViewById(R.id.enable_chat);
        imageview_video = (ImageView)findViewById(R.id.enable_video);
        editText_message = (EditText) findViewById(R.id.messageEditText);
        editText_message.requestFocus();
        button_send = (ImageView) findViewById(R.id.sendMessageButton);
        button_requestvideo = (Button)findViewById(R.id.start_video_screen);
        relativeLayout_chat = (RelativeLayout)findViewById(R.id.chat_layout) ;
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText_message.getText().toString().isEmpty())
                {
                    Toast.makeText(ChatActivityforUser.this,"Please write question and situation",Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(userSession.getUserCredits()) < 150 && userSession.getUserType().contains("client"))
                {
                    Intent intent = new Intent(ChatActivityforUser.this, CreditsActivity.class);
                    intent.putExtra("message", "You do not have enough Credits");
                    startActivity(intent);
                }
                else {
                    send_message();
                }
            }
        });
        imageview_video.setSelected(false);
        imageview_chat.setSelected(true);
        imageview_chat.setEnabled(true);
        imageview_video.setEnabled(true);

        imageview_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout_chat.setVisibility(View.VISIBLE);
                button_requestvideo.setVisibility(View.GONE);
                editText_message.requestFocus();
                cost_info.setText("you are requesting text reply 150 credit");
                imageview_video.setSelected(false);
                imageview_chat.setSelected(true);
            }
        });
        imageview_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout_chat.setVisibility(View.GONE);
                button_requestvideo.setVisibility(View.VISIBLE);
                cost_info.setText("you are requesting text reply 450 credit");
                imageview_video.setSelected(true);
                imageview_chat.setSelected(false);


            }
        });

        button_requestvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                relativeLayout_chat.setVisibility(View.GONE);
                button_requestvideo.setVisibility(View.VISIBLE);
                cost_info.setText("you are requesting text reply 450 credit");
                getAdviserList(getIntent().getStringExtra("advisor_id"));
                ///////////////////////

            }
        });
       // Log.e("Notificatio",AdvisorID);
        HitAPI_getStatus(AdvisorID);

        //////////////////////////////////////
        transferUtility = Util.getTransferUtility(this);
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                Constants.COGNITO_POOL_ID,    /* Identity Pool ID */
                Regions.US_EAST_1           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );
    }

    void send_message()
    {
        SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        Log.e("rendom id", mFormatter.format(date));

        Random r = new Random();
        int i1 = (r.nextInt(8000000) + 65);
        String MessageReviewID = "a_" +  i1 + (char) (i1%256);



        Payload payload = new Payload();
        Payload.Aps aps = new Payload.Aps();
        Payload.PnApns pnApns = new Payload.PnApns();

        Payload.Data data = new Payload.Data();
        Payload.PnGcm pnGcm = new Payload.PnGcm();

        aps.setAlert("Your order has been completed");


        aps.setAppType("user");


        aps.setBadge(1);
        aps.setSenderId(userSession.getUserId());
        aps.setSenderDisplayname(userSession.getUserName());
        aps.setSound("default");
        pnApns.setAps(aps);
        payload.setPnApns(pnApns);

        data.setAlert("You have recieved a new order.");
        data.setAppType("advisor");
        data.setBadge(1);
        data.setSenderId(userSession.getUserId());
        data.setSenderDisplayname(userSession.getUserName());
        data.setSound("default");
        pnGcm.setData(data);
        payload.setPnGcm(pnGcm);




        payload.setMessageType(MessageType);
        payload.setMsgDate( mFormatter.format(date));

        payload.setReceiverDisplayname(AdvisorName);
        payload.setReceiverId(AdvisorID);


            payload.setReceiverType("advisor");
            payload.setSenderType("client");
            payload.setMsgText(editText_message.getText().toString());

        payload.setSenderDisplayname(userSession.getUserName());
        payload.setSenderId(userSession.getUserId());
        payload.setStatus("0");
        payload.setReviewStatus("0");
        payload.setVideoUrl(ChatActivityforUser.VideoName);
        payload.setReviewId(MessageReviewID);
        payload.setDob(userSession.getUserDOB());


        final Message message = new Message(0,payload.getPnApns().getAps().getSenderId(),payload.getPnApns().getAps().getSenderDisplayname(),
                payload.getReceiverId(),payload.getReceiverDisplayname(),payload.getMsgText(),payload.getMsgDate(),Integer.parseInt(payload.getStatus()),
                payload.getVideoUrl(),payload.getMessageType(),payload.getSenderType(),payload.getReceiverType(),payload.getReviewStatus(),
                MessageReviewID,""+( Integer.parseInt(payload.getReceiverId()) + Integer.parseInt(payload.getSenderId())),payload.getDob());

        messages.add(message);
        userChatActivityAdapter.SetMessagesArrayList(messages);
        userChatActivityAdapter.notifyDataSetChanged();
        messageList.setSelection(userChatActivityAdapter.getCount() - 1);
        databaseHelper.insertMessageToDB(message);

        JSONObject jsonObject  = null;
        try {
            jsonObject = new JSONObject(payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
                int temp = Integer.parseInt(userSession.getUserCredits());
                temp -= 150;
                userSession.setUserCredits("" + temp);
                UpdateCredits(userSession.getUserId(),userSession.getUserCredits());

                Appboy.getInstance(ChatActivityforUser.this).logCustomEvent(AdvisorName+"_TextOrder"+"_Placed");

            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        Callback callback1 = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());


            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };

        Log.e("payload",jsonObject.toString());

            pubnub.publish("Channel_Advisor_" + getIntent().getStringExtra("advisor_id"), jsonObject, callback);
            pubnub.publish("chat", jsonObject, callback1);


            int temp = Integer.parseInt(userSession.getUserCredits());
            temp -= 150;
            textView1.setText(""+temp);

            editText_message.setText("");

        progressDialog.hide();
    }

    void UpdateCredits(final String id, final String credits) {
        APIService apiservice = retrofit.create(APIService.class);
        Call<MessageHistoryResponse> APICall = apiservice.updateCredits(id, credits);
        APICall.enqueue(new retrofit2.Callback<MessageHistoryResponse>() {
            @Override
            public void onResponse(Call<MessageHistoryResponse> call, Response<MessageHistoryResponse> response) {
                if (response.body() != null) {

                    if (response.body().getResult() == 1) {

                    } else {
                        Log.e("message history" ,"error");
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageHistoryResponse> call, Throwable t) {
              //  Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
               // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        appController.SetActivity(null);
        LocalBroadcastManager.getInstance(ChatActivityforUser.this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appController.SetActivity(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("message_receiver"));
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
          //  Toast.makeText(getApplicationContext(),"msg recved  "+ intent.getStringExtra("message"),Toast.LENGTH_SHORT).show();
            Payload payload  = new Gson().fromJson(intent.getStringExtra("message").toString(), Payload.class);
            Message message1 = new Message(2,payload.getPnApns().getAps().getSenderId(),payload.getPnApns().getAps().getSenderDisplayname(),
                    payload.getReceiverId(),payload.getReceiverDisplayname(),payload.getMsgText(),payload.getMsgDate(),Integer.parseInt(payload.getStatus()),
                    payload.getVideoUrl(),payload.getMessageType(),payload.getSenderType(),payload.getReceiverType(),payload.getReviewStatus(),
                    "none".toString(),""+( Integer.parseInt(payload.getReceiverId()) + Integer.parseInt(payload.getSenderId())),payload.getDob() );

            Log.e("senderid",payload.getSenderId());
            if(payload.getSenderId().contains(AdvisorID))
            {
                Log.e("insidesenderid",payload.getSenderId());
                messages.add(message1);
                userChatActivityAdapter.SetMessagesArrayList(messages);
                userChatActivityAdapter.notifyDataSetChanged();
                messageList.setSelection(userChatActivityAdapter.getCount() - 1);
            }
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
            ChatActivityforUser.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
        ChatActivityforUser.this.finish();
    }

    private void getAdviserList(final String id) {


        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.Advisor_API_BASE_URL + "getAdvisor", new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    AdvisorInfo userInfo = null;
                    JSONObject jObj = new JSONObject(response);
                    jObj = jObj.getJSONObject("message");
                    JSONArray jsonArray = jObj.names();
                    Log.e("All Arrys", "" + jsonArray.length());
                    for (int index = 0; index < jsonArray.length(); index++) {
                        JSONObject jsonObject = jObj.getJSONObject(jsonArray.getString(index));
                        userInfo = new Gson().fromJson(jsonObject.toString(), AdvisorInfo.class);
                        if (userInfo.getReviews().size() > 0) {
                            int totalReview = 0;
                            for (int i = 0; i < userInfo.getReviews().size(); i++) {
                                totalReview += Integer.parseInt(userInfo.getReviews().get(i).getReview());
                            }
                            userInfo.setRating(totalReview / userInfo.getReviews().size());
                        } else {
                            userInfo.setRating(0.0f);
                        }
                        Log.e("jsonObject", "" + jsonObject.toString());
                    }
                    if (userInfo != null) {
                        if(userInfo.getStatus().contains("0") || userInfo.getDeactivated().contains("1")) {

                        }
                       //write here code

                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(),ClientSendMessage.class);
                        intent.putExtra("advisor_id", userInfo.getId());
                        intent.putExtra("advisor_name", userInfo.getUsername());
                        intent.putExtra("advisor_description", userInfo.getShortDescription());
                        intent.putExtra("advisor_instruction", userInfo.getInstructions());
                        intent.putExtra("advisor_picture", userInfo.getDisplayPicture());
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, "adviser");
    }
    boolean HitAPI_getStatus(String id) {

        progressDialog = new ProgressDialog(ChatActivityforUser.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("wait...");
        progressDialog.show();
        APIService apiservice = retrofit1.create(APIService.class);
        Call<GetAdvisorStatusReponse> APICall = apiservice.getStatus(id);
        APICall.enqueue(new retrofit2.Callback<GetAdvisorStatusReponse>() {
            @Override
            public void onResponse(Call<GetAdvisorStatusReponse> call, Response<GetAdvisorStatusReponse> response) {
                if (response.body() != null) {
                    if (!response.body().getMessage().contains("1")) {

                        editText_message.setEnabled(false);
                        button_send.setEnabled(false);
                        imageview_chat.setEnabled(false);
                        imageview_video.setEnabled(false);
                        cost_info.setText("I'M CURRENTLY UNAVAILABLE");
                        cost_info.setGravity(Gravity.CENTER);
                    }
                    progressDialog.dismiss();

                } else {


                    progressDialog.dismiss();
                    Toast.makeText(ChatActivityforUser.this,"Error occourd while getting status",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetAdvisorStatusReponse> call, Throwable t) {

            }


        });
        return true;
    }
    void RequestVideoRecording()
    {
//        File saveFolder = new File(getApplicationContext().getFilesDir().getPath() , "videoss");
//        if (!saveFolder.mkdirs())
//            throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");

        new MaterialCamera(ChatActivityforUser.this)                               // Constructor takes an Activity
                .allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
                .autoSubmit(false)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
                .saveDir(getApplicationContext().getFilesDir().getPath())                               // The folder recorded videos are saved to
                .showPortraitWarning(true)                         // Whether or not a warning is displayed if the user presses record in portrait orientation
                .defaultToFrontFacing(true)                       // Whether or not the camera will initially show the front facing camera
                .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
                .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
                .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
                .videoEncodingBitRate(1024000)                     // Sets a custom bit rate for video recording.
                .audioEncodingBitRate(50000)                       // Sets a custom bit rate for audio recording.
                .videoFrameRate(24)                                // Sets a custom frame rate (FPS) for video recording.
                .qualityProfile(MaterialCamera.QUALITY_LOW)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                .videoPreferredAspect(16f/9f)
                .maxAllowedFileSize(1024 * 1024 * 5)               // Sets a max file size of 5MB, recording will stop if file reaches this limit. Keep in mind, the FAT file system has a file size limit of 4GB.
                .iconRecord(R.drawable.mcam_action_capture)        // Sets a custom icon for the button used to start recording
                .iconStop(R.drawable.mcam_action_stop)             // Sets a custom icon for the button used to stop recording
                .iconFrontCamera(R.drawable.mcam_camera_front)     // Sets a custom icon for the button used to switch to the front camera
                .iconRearCamera(R.drawable.mcam_camera_rear)       // Sets a custom icon for the button used to switch to the rear camera
                .iconPlay(R.drawable.evp_action_play)              // Sets a custom icon used to start playback
                .iconPause(R.drawable.evp_action_pause)            // Sets a custom icon used to pause playback
                .iconRestart(R.drawable.evp_action_restart)        // Sets a custom icon used to restart playback
                .labelRetry(R.string.mcam_retry)                   // Sets a custom button label for the button used to retry recording, when available
                .labelUseVideo(R.string.mcam_use_video)            // Sets a custom button label for the button used to confirm a recording
                .start(CAMERA_RQ);                                 // Starts the camera activity, the result will be sent back to the current Activity
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CAPTURE) {
                videoUri = data.getData();
                Log.e("video path", getPath(ChatActivityforUser.this, videoUri));
                file = new File(getPath(ChatActivityforUser.this, videoUri));
                Log.e("Client Send Message", "send activty result called");
                ChatActivityforUser.MessageType = "video";
            }


            if (requestCode == CAMERA_RQ) {

                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();

                    videoUri = data.getData();
                    Log.e("video path", getPath(ChatActivityforUser.this, videoUri));
                    file = new File(getPath(ChatActivityforUser.this, videoUri));
                    Log.e("Client Send Message", "send activty result called");
                    ChatActivityforUser.MessageType = "video";


                    progressDialog.show();
                        ChatActivityforUser.S3Example s3Example = new ChatActivityforUser.S3Example();
                        s3Example.execute();

                } else if(data != null) {
                    Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private class S3Example extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            s3Client = new AmazonS3Client(new BasicAWSCredentials(Constants.AWS_ACCESS_KEY, Constants.AWS_SECRET_KEY));
            File fileToUpload = file;
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            mydate = mydate.replaceAll("\\s", "");
            ChatActivityforUser.VideoName = Constants.BUCKET_URL + "sjk" + mydate + ".mp4";
            TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, "sjk" + mydate + ".mp4",
                    fileToUpload, CannedAccessControlList.PublicRead);
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d("Uploading", "onStateChanged: " + id + ", " + state);
                    if (state == TransferState.COMPLETED) {
                        send_message();
                    }

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    Log.d("Uploading", String.format("onProgressChanged: %d, total: %d, current: %d",
                            id, bytesTotal, bytesCurrent));
                }

                @Override
                public void onError(int id, Exception ex) {
                    Log.e("error", ex.toString());

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
    void GetProfilePicture(final String id) {
        APIService apiservice = retrofit1.create(APIService.class);
        Call<AdvisorProfilePictureResponse> APICall = apiservice.getAdvisorPicture(id);
        APICall.enqueue(new retrofit2.Callback<AdvisorProfilePictureResponse>() {
            @Override
            public void onResponse(Call<AdvisorProfilePictureResponse> call, Response<AdvisorProfilePictureResponse> response) {

                if (response.body() != null) {

                    if (response.body().getResult() == 1) {

                      //  return response.body().getMessage();
                        //Picasso.with(MainActivity.this).load(Constants.Advisor_IMAGE_URL  + response.body().getMessage()).into(selectableRoundedImageView);

                    } else {
                        Log.e("message history" ,"error");
                    }
                }
            }

            @Override
            public void onFailure(Call<AdvisorProfilePictureResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
//327194