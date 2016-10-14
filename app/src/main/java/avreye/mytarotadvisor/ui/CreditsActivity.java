package avreye.mytarotadvisor.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.appboy.Appboy;

import java.util.ArrayList;
import java.util.List;
import avreye.mytarotadvisor.Object.InAppPurchases;
import avreye.mytarotadvisor.Object.LoginResponse;
import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.Object.UpdateCreditResponse;
import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.CreditScreenAdapter;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.credentail.ClientLoginActivity;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreditsActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{

    BillingProcessor bp;


    private static final String TAG = "CreditsScreen";
    CreditScreenAdapter creditScreenAdapter;
    ListView inapplist;
    ArrayList<InAppPurchases.Message> inapps;
    TextView textView_my_credits;
    TextView textView_message;
    Retrofit retrofit;
    Retrofit retrofit1;
    TextView textView_title;
    private TextView headerTitle;
    private TextView MyCredits;
    private Toolbar toolbar;
    ImageButton imageButton_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

/////////////////////////////////////////////////////////////////////////////////////////////////////
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

        bp = new BillingProcessor(this, base64EncodedPublicKey,"12658234756384564268", this);
////////////////////////////////////////////////////////////////////////////////////////////////////
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        textView_my_credits = (TextView) findViewById(R.id.mycredits);
        textView_message = (TextView) findViewById(R.id.mycredits_message);
        if(getIntent().getStringExtra("message").toString().contains("no message"))
            textView_message.setVisibility(View.INVISIBLE);
        else
            textView_message.setVisibility(View.VISIBLE);
        textView_my_credits.setText(" Balance : " + new UserSession(this).getUserCredits().toString() + " credits");




        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        headerTitle = (TextView) toolbar.findViewById(R.id.title_text);
        MyCredits = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        MyCredits.setText(new UserSession(this).getUserCredits().toString());
        imageButton_back = (ImageButton) toolbar.findViewById(R.id.tool_bar_backButton);
        imageButton_back.setVisibility(View.VISIBLE);
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
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreditsActivity.this, MainActivity.class);
                startActivity(intent);
                CreditsActivity.this.finish();
            }
        });

/////////////////////////////////////






        /////////////////////////////////////////////////////

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.PURCHASE_CREDITS_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit1 = new Retrofit.Builder()
                .baseUrl(Constants.CLIENT_API_BASE_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        inapps = new ArrayList<InAppPurchases.Message>();
        inapplist = (ListView) findViewById(R.id.credits_list_view);
        creditScreenAdapter = new CreditScreenAdapter(this, inapps,bp,this);
        assert inapplist != null;
        inapplist.setAdapter(creditScreenAdapter);
        HitAPI();

    }

    @Override
    public void onResume() {
        super.onResume();
        creditScreenAdapter.SetUserList(inapps);
    }
    public void onStart() {
        super.onStart();
        Appboy.getInstance(CreditsActivity.this).openSession(CreditsActivity.this);
    }
    public void onStop() {
        super.onStop();
        Appboy.getInstance(CreditsActivity.this).closeSession(CreditsActivity.this);
    }
    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }
    void HitAPI() {
        APIService apiservice = retrofit.create(APIService.class);
        Call<InAppPurchases> APICall = apiservice.GetCreditsDetail();
        APICall.enqueue(new Callback<InAppPurchases>() {
            @Override
            public void onResponse(Call<InAppPurchases> call, Response<InAppPurchases> response) {

                if (response.body() != null) {
                    if (response.body().getResult() == 1) {

                        inapps = response.body().getMessage();
                        creditScreenAdapter.SetUserList(inapps);

                    } else {
                        Toast.makeText(getApplicationContext(), "invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<InAppPurchases> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
        Log.e("CreditScreen","billing initialized");
      //  SkuDetails skuDetails =  bp.getPurchaseListingDetails("com.liquidsoftwaremobile.mytarotadvisor.credit1");
      //  Log.e("CreditScreen",skuDetails.title);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */
        Log.e("CreditScreen","Product Purchased");
        if(productId.contains("com.liquidsoftwaremobile.mytarotadvisor.credit1"))
        {
            int temp =  Integer.parseInt(new UserSession(CreditsActivity.this).getUserCredits());
            temp += 450;
            new UserSession(CreditsActivity.this).setUserCredits(temp+"");

            MyCredits.setText(new UserSession(this).getUserCredits().toString());
            UpdateCredits(new UserSession(CreditsActivity.this).getUserId(),temp+"");
            bp.consumePurchase(productId);
        }
        else if(productId.contains("com.liquidsoftwaremobile.mytarotadvisor.credit2"))
        {
            int temp =  Integer.parseInt(new UserSession(CreditsActivity.this).getUserCredits());
            temp += 1000;
            new UserSession(CreditsActivity.this).setUserCredits(temp+"");

            MyCredits.setText(new UserSession(this).getUserCredits().toString());
            UpdateCredits(new UserSession(CreditsActivity.this).getUserId(),temp+"");
            bp.consumePurchase(productId);
        }else if(productId.contains("com.liquidsoftwaremobile.mytarotadvisor.credit3"))
        {
            int temp =  Integer.parseInt(new UserSession(CreditsActivity.this).getUserCredits());
            temp += 1950;
            new UserSession(CreditsActivity.this).setUserCredits(temp+"");

            MyCredits.setText(new UserSession(this).getUserCredits().toString());
            UpdateCredits(new UserSession(CreditsActivity.this).getUserId(),temp+"");
            bp.consumePurchase(productId);
        }else if(productId.contains("com.liquidsoftwaremobile.mytarotadvisor.credit"))
        {
            int temp =  Integer.parseInt(new UserSession(CreditsActivity.this).getUserCredits());
            temp += 3300;
            new UserSession(CreditsActivity.this).setUserCredits(temp+"");

            MyCredits.setText(new UserSession(this).getUserCredits().toString());
            UpdateCredits(new UserSession(CreditsActivity.this).getUserId(),temp+"");
            bp.consumePurchase(productId);
        }
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         *
         * Note - this includes handling the case where the user canceled the buy dialog:
         * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
         */
        Log.e("CreditScreen","onBillingError Called");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
        Log.e("CreditScreen","onPurchaseHistoryRestored Called");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    void UpdateCredits(final String id, final String credits) {

        APIService apiservice = retrofit1.create(APIService.class);
        Call<UpdateCreditResponse> APICall = apiservice.updateCredits(id, credits);
        APICall.enqueue(new Callback<UpdateCreditResponse>() {
            @Override
            public void onResponse(Call<UpdateCreditResponse> call, Response<UpdateCreditResponse> response) {
                if (response.body() != null) {

                    if (response.body().getResult() == 1) {

                        Log.e("CreditScreen","Credits Updated");
                       // Intent intent = new Intent(CreditsActivity.this,MainActivity.class);
                        //CreditsActivity.this.startActivity(intent);

                    } else {
                        Log.e("CreditScreen" ,"Credits Updated error");
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateCreditResponse> call, Throwable t) {
                Log.e("CreditScreen" ,"Credits Updated filure");
            }
        });
    }
}
