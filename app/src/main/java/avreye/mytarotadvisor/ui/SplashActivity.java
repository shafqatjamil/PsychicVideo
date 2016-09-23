package avreye.mytarotadvisor.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import Billing.IabHelper;
import Billing.IabResult;
import avreye.mytarotadvisor.Object.GetMyCreditsResponse;
import avreye.mytarotadvisor.Object.InAppPurchases;
import avreye.mytarotadvisor.Object.LoginResponse;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.credentail.ClientLoginActivity;
import avreye.mytarotadvisor.ui.credentail.RegisterationActivity;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    public static IabHelper mHelper;
    Retrofit retrofit;
    private DatabaseHelper databaseHelper;
    private Retrofit retrofit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MESSAGE_API_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit1 = new Retrofit.Builder()
                .baseUrl(Constants.CLIENT_API_BASE_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        if (new UserSession(SplashActivity.this).getIsUserLoggedIn()) {
           HitAPI(new UserSession(this).getUserId(),new UserSession(this).getUserType());
        }
        else
        {
            finish();
            Intent intent = new Intent(getApplicationContext(), RegisterationActivity.class);
            startActivity(intent);
        }
        ////////////////////
        String base64EncodedPublicKey = "MIIBIjANBgk" +
                "qhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmr1RaSYlG" +
                "CTabAd30BmmvJ6TDHP7iuakVU1GL/yEnGeM8A40t" +
                "DPd3ArypawO/6uxgp5ZGv+5vg3CuftqAZEDOTXSP/" +
                "Ge8U32lbmpORlYVtSEIdBC77ISIKWyrowcE9kIaPu2ucu" +
                "wdlq3Lh31YvFsuRoELBe8flg9qtbfswAnp5FTMFiFR99" +
                "k9rcgwHk81kpThcWBEtok4nbk6kBOtY3MyMOyD429X2qev" +
                "pJyI/MLsLPZMH5HDv1QWh/O00vpQ3Sh4Xbo9a+gM3nq0AOx/qN" +
                "w/CUi0Jiqh0YjlPsQHMvPiEMsbIbqwse0f8llhVvIIyFY6wD/OMDya" +
                "7+Ng7/M/OZZ5QIDAQAB";

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.e(TAG, "Problem setting up In-app Billing: " + result);
                    ArrayList<String> list = new ArrayList<>();
                    list.add("com.liquidsoftwaremobile.mytarotadvisor.credit1");


//                    IabHelper.QueryInventoryFinishedListener mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
//                        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//                            if (result.isFailure()) {
//                                Log.e(TAG, "Problem setting up In-app Billing errorwhile querying: " + result);
//                                return;
//                            }
//                            String applePrice = inventory.getSkuDetails("com.liquidsoftwaremobile.mytarotadvisor.credit1").getPrice();
//                            Log.e(TAG, "price: " + applePrice);
//                        }
//                    };
                    //   mHelper.queryInventoryAsync(true, list, null);
                } else {
                    Log.e(TAG, "Successful setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
            }
        });




    }



    void HitAPI(final String id, final String type) {
        Log.e("test" , id + "  " + type);
        APIService apiservice = retrofit.create(APIService.class);
        Call<MessageHistoryResponse> APICall = apiservice.getHistory(id, type);
        APICall.enqueue(new Callback<MessageHistoryResponse>() {
            @Override
            public void onResponse(Call<MessageHistoryResponse> call, Response<MessageHistoryResponse> response) {

                if (response.body() != null) {

                    if (response.body().getResult() == 1) {
                        ArrayList<MessageHistoryResponse.Message> Hmessages = response.body().getMessage();
                        databaseHelper.FlushDatabase();
                        for(int i = 0; i < Hmessages.size(); i++)
                        {
                            databaseHelper.insertMessageToDB((new Message(
                                    Integer.parseInt(Hmessages.get(i).getId()),
                                    Hmessages.get(i).getSenderId(),
                                    Hmessages.get(i).getSenderDisplayname(),
                                    Hmessages.get(i).getReceiverId(),
                                    Hmessages.get(i).getReceiverDisplayname(),
                                    Hmessages.get(i).getMsgText(),
                                    Hmessages.get(i).getMsgDate(),
                                    Integer.parseInt(Hmessages.get(i).getStatus()),
                                    Hmessages.get(i).getVideoUrl(),
                                    Hmessages.get(i).getMessageType(),
                                    Hmessages.get(i).getSenderType(),
                                    Hmessages.get(i).getReceiverType(),
                                    Hmessages.get(i).getReviewStatus(),
                                    Hmessages.get(i).getMessageReviewId(),
                                    ""+( Integer.parseInt(Hmessages.get(i).getReceiverId()) + Integer.parseInt(Hmessages.get(i).getSenderId())),
                                    Hmessages.get(i).getClientDob()
                                    )));


                        }
                    } else {
                        Log.e("message history" ,"error");
                    }

                    if (new UserSession(SplashActivity.this).getUserType().contains("client")) {
                        GetMyCredits(new UserSession(SplashActivity.this).getUserId());
                    }
                    else {
                        finish();
                        Intent serviceIntent = new Intent(SplashActivity.this, PubnubService.class);
                        startService(serviceIntent);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageHistoryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    void GetMyCredits(final String id) {
        APIService apiservice = retrofit1.create(APIService.class);
        Call<GetMyCreditsResponse> APICall = apiservice.getMyCredits(id);
        APICall.enqueue(new Callback<GetMyCreditsResponse>() {
            @Override
            public void onResponse(Call<GetMyCreditsResponse> call, Response<GetMyCreditsResponse> response) {

                if (response.body() != null) {

                    if (response.body().getResult() == 1) {

                        new UserSession(SplashActivity.this).setUserCredits(response.body().getMessage());

                    } else {
                        Log.e("message history" ,"error");
                    }

                    //   ArrayList<Message> test  = databaseHelper.getAllMessages(new UserSession(SplashActivity.this).getUserId());
                    finish();
                    Intent serviceIntent = new Intent(SplashActivity.this, PubnubService.class);
                    startService(serviceIntent);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<GetMyCreditsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
