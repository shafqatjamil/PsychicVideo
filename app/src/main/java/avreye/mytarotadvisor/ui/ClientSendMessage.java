package avreye.mytarotadvisor.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialcamera.MaterialCamera;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.Object.AdvisorInfo;
import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.Object.Payload;
import avreye.mytarotadvisor.Object.UpdateCreditResponse;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.helper.MlRoundedImageView;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.credentail.ClientLoginActivity;
import avreye.mytarotadvisor.utils.Constants;
import avreye.mytarotadvisor.utils.Util;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientSendMessage extends AppCompatActivity {
    Pubnub pubnub;
    Toolbar toolbar;
    EditText editText_specificQuestion;
    EditText edit_view_client_message_situation;
    Button button_submitQuestion;
    LinearLayout button_record_video;
    VideoView recorded_video_preview;
    ImageButton imageButton_close_preview;
    ImageButton imageButton_record;
    MlRoundedImageView mlRoundedImageView;
    DatabaseHelper databaseHelper;
    FrameLayout frameLayout_recordedVideo;
    Context mContext;
    static final int REQUEST_VIDEO_CAPTURE = 9000;
    private final static int CAMERA_RQ = 6969;
    AmazonS3Client s3Client;
    File file;
    static String VideoName = "";
    static String MessageType = "text";
    private TransferUtility transferUtility;
    UserSession userSession;
    Retrofit retrofit;
    Uri videoUri = null;
    String AdvisorName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_send_message);
        Log.e("Client Send Message", "activity started");
        progressDialog = new ProgressDialog(ClientSendMessage.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending Message...");
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        userSession = new UserSession(this);
        mContext = this;
        databaseHelper = new DatabaseHelper(getApplicationContext());

        pubnub = new Pubnub(Constants.PUBNUB_PUBLISH_KEY, Constants.PUBNUB_SUBSCRIBE_KEY);

        mlRoundedImageView = (MlRoundedImageView) findViewById(R.id.client_send_meesage_profile_pic);
        frameLayout_recordedVideo = (FrameLayout) findViewById(R.id.frameLayout_recordedVideo);
        Picasso.with(mContext).load(Constants.Advisor_IMAGE_URL + getIntent().getStringExtra("advisor_picture")).into(mlRoundedImageView);
        TextView textView = (TextView) findViewById(R.id.client_send_message_advisor_name);
        textView.setText(getIntent().getStringExtra("advisor_name"));
        AdvisorName = getIntent().getStringExtra("advisor_name");
        textView = (TextView) findViewById(R.id.client_send_message_advisor_description);
        textView.setText(Html.fromHtml(unescapeJavaString(getIntent().getStringExtra("advisor_description"))));

        textView = (TextView) findViewById(R.id.client_send_message__instruction);
        textView.setText(Html.fromHtml(unescapeJavaString(getIntent().getStringExtra("advisor_instruction"))));

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CLIENT_API_BASE_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        textView = (TextView) toolbar.findViewById(R.id.title_text);
        String sourceString = "VIDEO ORDER";//getIntent().getStringExtra("advisor_name");
        textView.setText(Html.fromHtml(sourceString));
        ((TextView) toolbar.findViewById(R.id.toolbar_credits)).setText(new UserSession(this).getUserCredits());
        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        TextView textView_credits1 = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        textView_credits.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits1.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits.setTextSize(12f);


        ImageButton imageButton_back = (ImageButton) toolbar.findViewById(R.id.tool_bar_backButton);
        imageButton_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        imageButton_back.setVisibility(View.VISIBLE);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientSendMessage.this, MainActivity.class);
                startActivity(intent);
                ClientSendMessage.this.finish();
            }
        });



        editText_specificQuestion = (EditText) findViewById(R.id.edit_view_client_message_specific_question);
        edit_view_client_message_situation = (EditText) findViewById(R.id.edit_view_client_message_situation);
        button_submitQuestion = (Button) findViewById(R.id.button_client_message_submit);
        button_record_video = (LinearLayout) findViewById(R.id.button_add_video);
        recorded_video_preview = (VideoView) findViewById(R.id.recorded_video_preview);
        recorded_video_preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.e("Client Send Message", "playng vide");
                Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                intent.putExtra("advisor_video", getPath(ClientSendMessage.this, videoUri));
                mContext.startActivity(intent);


//                recorded_video_preview.setVideoPath(getPath(ClientSendMessage.this, videoUri));
//                recorded_video_preview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//
//                    }
//                });
//                recorded_video_preview.start();


                return false;
            }
        });
        imageButton_close_preview = (ImageButton) findViewById(R.id.imageButton_close_preview);
        imageButton_record = (ImageButton) findViewById(R.id.imageButton_record);
        imageButton_close_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_record_video.setVisibility(View.VISIBLE);
                frameLayout_recordedVideo.setVisibility(View.GONE);
                VideoName = "";
                MessageType = "text";
                recorded_video_preview.stopPlayback();
             //   getContentResolver().delete(videoUri, null, null);


                File fdelete = new File(String.valueOf(videoUri));
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + videoUri);
                    } else {
                        System.out.println("file not Deleted :" + videoUri);
                    }
                }


                videoUri = null;

            }
        });
        button_record_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     dispatchTakeVideoIntent();
                RequestVideoRecording();
            }
        });
        imageButton_record.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //     dispatchTakeVideoIntent();
                RequestVideoRecording();

            }
        });

        button_submitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( (editText_specificQuestion.getText().toString().isEmpty() || edit_view_client_message_situation.getText().toString().isEmpty()) && MessageType.contains("text")) {
                    Toast.makeText(ClientSendMessage.this, "Please write question and situation", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(userSession.getUserCredits()) < 450) {
                    Intent intent = new Intent(mContext, CreditsActivity.class);
                    intent.putExtra("message", "You do not have enough Credits");
                    mContext.startActivity(intent);
                } else {


                    progressDialog.show();
                    if (MessageType.contains("video")) {
                        S3Example s3Example = new S3Example();
                        s3Example.execute();
                    } else {
                        send_message();
                    }
                }
            }
        });

        s3Client = new AmazonS3Client(new CognitoCachingCredentialsProvider(
                ClientSendMessage.this,
                Constants.COGNITO_POOL_ID,
                Regions.US_EAST_1
        ));
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3Client, getApplicationContext());


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoUri != null) {

            recorded_video_preview.seekTo(100);
        }
    }

    void send_message() {


        final Payload payload = new Payload();
        Payload.Aps aps = new Payload.Aps();
        Payload.PnApns pnApns = new Payload.PnApns();

        Payload.Data data = new Payload.Data();
        Payload.PnGcm pnGcm = new Payload.PnGcm();

        aps.setAlert("You have got a new order.");
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


        SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        mFormatter.format(date);
        Log.e("date", mFormatter.format(date));




//        String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss a z" ;
//        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        String dateTimeString =  sdf.format(new Date());
//        System.out.println(dateTimeString);   // current UTC time
//        long timeStamp=sdf.parse(dateTimeString).getTime(); //current UTC time in milisec
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date(timeStamp));
//        cal.add(Calendar.HOUR_OF_DAY, 5);
//        cal.add(Calendar.MINUTE, 30);
//        System.out.println(sdf.format(cal.getTime()));




        Random r = new Random();
        int i1 = (r.nextInt(8000000) + 65);
        String MessageReviewID = "a_" + i1 + (char) (i1 % 256);


        payload.setMessageType("video");
        payload.setMsgDate(mFormatter.format(date));
        payload.setMsgText(editText_specificQuestion.getText().toString() + "/~*/" + edit_view_client_message_situation.getText().toString());
        payload.setReceiverDisplayname(getIntent().getStringExtra("advisor_name"));
        payload.setReceiverId(getIntent().getStringExtra("advisor_id"));
        payload.setReceiverType("advisor");
        payload.setSenderDisplayname(userSession.getUserName());
        payload.setSenderId(userSession.getUserId());
        payload.setSenderType("client");
        payload.setStatus("0");
        payload.setReviewStatus("0");
        payload.setReviewId(MessageReviewID);
        payload.setReponseTime("0");

        payload.setVideoUrl(ClientSendMessage.VideoName);


        Message message = new Message(0, payload.getPnApns().getAps().getSenderId(), payload.getPnApns().getAps().getSenderDisplayname(),
                payload.getReceiverId(), payload.getReceiverDisplayname(), payload.getMsgText(), payload.getMsgDate(), Integer.parseInt(payload.getStatus()),
                payload.getVideoUrl(), "video", payload.getSenderType(), payload.getReceiverType(), payload.getReviewStatus(),
                MessageReviewID, "" + (Integer.parseInt(payload.getReceiverId()) + Integer.parseInt(payload.getSenderId())),payload.getDob());

        databaseHelper.insertMessageToDB(message);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
                Log.e("Message Sent", payload.toString());
                int temp = Integer.parseInt(userSession.getUserCredits());
                temp -= 450;
                userSession.setUserCredits("" + temp);
                UpdateCredits(userSession.getUserId(), userSession.getUserCredits());

                Appboy.getInstance(ClientSendMessage.this).logCustomEvent(AdvisorName+"_VideoOrder"+"_Placed");
                finish();
                Intent intent = new Intent(getApplicationContext(), VideoOrderConfirmationScreen.class);
                startActivity(intent);
            }

            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());

            }
        };
        Callback callback1 = new Callback() {
            public void successCallback(String channel, Object response) {

                progressDialog.dismiss();
            }


            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());

                progressDialog.dismiss();
            }

        };
        pubnub.publish("Channel_Advisor_" + getIntent().getStringExtra("advisor_id"), jsonObject, callback);
        pubnub.publish("chat", jsonObject, callback1);
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CAPTURE) {
                button_record_video.setVisibility(View.GONE);
                frameLayout_recordedVideo.setVisibility(View.VISIBLE);
                videoUri = data.getData();
                Log.e("video path", getPath(ClientSendMessage.this, videoUri));
                recorded_video_preview.setVideoURI(videoUri);
                recorded_video_preview.seekTo(100);
                file = new File(getPath(ClientSendMessage.this, videoUri));
                Log.e("Client Send Message", "send activty result called");
                ClientSendMessage.MessageType = "video";
            }


            if (requestCode == CAMERA_RQ) {

                if (resultCode == RESULT_OK) {
                 //   Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();

                    button_record_video.setVisibility(View.GONE);
                    frameLayout_recordedVideo.setVisibility(View.VISIBLE);
                    videoUri = data.getData();
                    Log.e("video path", getPath(ClientSendMessage.this, videoUri));
                    recorded_video_preview.setVideoURI(videoUri);
                    recorded_video_preview.seekTo(100);
                    file = new File(getPath(ClientSendMessage.this, videoUri));
                    Log.e("Client Send Message", "send activty result called");
                    ClientSendMessage.MessageType = "video";

                } else if(data != null) {
                    Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                    e.printStackTrace();
                   // Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
//            s3Client = new AmazonS3Client(new CognitoCachingCredentialsProvider(
//                    ClientSendMessage.this,
//                    Constants.COGNITO_POOL_ID,
//                    Regions.EU_WEST_1
//            ));
//            s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));
            File fileToUpload = file;
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            mydate = mydate.replaceAll("\\s", "");
            ClientSendMessage.VideoName = Constants.BUCKET_URL + "sjk" + mydate + ".mp4";
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ClientSendMessage.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    void UpdateCredits(final String id, final String credits) {
        APIService apiservice = retrofit.create(APIService.class);
        Call<UpdateCreditResponse> APICall = apiservice.updateCredits(id, credits);
        APICall.enqueue(new retrofit2.Callback<UpdateCreditResponse>() {
            @Override
            public void onResponse(Call<UpdateCreditResponse> call, Response<UpdateCreditResponse> response) {

                if (response.body() != null) {

                    if (response.body().getResult() == 1) {

                    } else {
                        Log.e("message history", "error");
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateCreditResponse> call, Throwable t) {
                //     Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                //     Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String unescapeJavaString(String st) {

        StringBuilder sb = new StringBuilder(st.length());

        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == st.length() - 1) ? '\\' : st
                        .charAt(i + 1);
                // Octal escape?
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code += st.charAt(i + 1);
                        i++;
                        if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code += st.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code, 8));
                    continue;
                }
                switch (nextChar) {
                    case '\\':
                        ch = '\\';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    // Hex Unicode: u????
                    case 'u':
                        if (i >= st.length() - 5) {
                            ch = 'u';
                            break;
                        }
                        int code = Integer.parseInt(
                                "" + st.charAt(i + 2) + st.charAt(i + 3)
                                        + st.charAt(i + 4) + st.charAt(i + 5), 16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
    }


    void RequestVideoRecording()
    {
//        File saveFolder = new File(getApplicationContext().getFilesDir().getPath() , "videoss");
//        if (!saveFolder.mkdirs())
//            throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");

        new MaterialCamera(ClientSendMessage.this)                               // Constructor takes an Activity
                .allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
                .autoSubmit(false)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
                .saveDir(getApplicationContext().getFilesDir().getPath())                               // The folder recorded videos are saved to
                .showPortraitWarning(false)                         // Whether or not a warning is displayed if the user presses record in portrait orientation
                .defaultToFrontFacing(true)                       // Whether or not the camera will initially show the front facing camera
                .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
                .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
                .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
                .videoEncodingBitRate(1024000)                     // Sets a custom bit rate for video recording.
                .audioEncodingBitRate(50000)                       // Sets a custom bit rate for audio recording.
                .videoFrameRate(24)                                // Sets a custom frame rate (FPS) for video recording.
                .qualityProfile(MaterialCamera.QUALITY_LOW)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                .videoPreferredAspect(16f/9f)
                .maxAllowedFileSize(1024 * 1024 * 500)               // Sets a max file size of 5MB, recording will stop if file reaches this limit. Keep in mind, the FAT file system has a file size limit of 4GB.
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
}
