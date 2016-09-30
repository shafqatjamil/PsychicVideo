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

import com.appboy.Appboy;

import java.util.ArrayList;
import java.util.List;

import Billing.IabHelper;
import Billing.IabResult;
import Billing.Inventory;
import avreye.mytarotadvisor.Object.InAppPurchases;
import avreye.mytarotadvisor.Object.LoginResponse;
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

public class CreditsActivity extends AppCompatActivity {

    private static final String TAG = "CreditsScreen";
    CreditScreenAdapter creditScreenAdapter;
    ListView inapplist;
    ArrayList<InAppPurchases.Message> inapps;
    TextView textView_my_credits;
    TextView textView_message;
    Retrofit retrofit;
    TextView textView_title;
    private TextView headerTitle;
    private TextView MyCredits;
    private Toolbar toolbar;
    ImageButton imageButton_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

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





        IabHelper.QueryInventoryFinishedListener mGotInventoryListener
                = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {

                if (result.isFailure()) {
                    Log.e(TAG, "fail setting up In-app Billing: " + result);
                }
                else {
                    // does the user have the premium upgrade?
                   // mIsPremium = inventory.hasPurchase(SKU_PREMIUM);
                    // update UI accordingly
                    Log.e(TAG, "Successful setting up In-app Billing: " + result + inventory.getSkuDetails("com.liquidsoftwaremobile.mytarotadvisor.credit1"));
                }
            }
        };
        SplashActivity.mHelper.queryInventoryAsync(mGotInventoryListener);
        /////////////////////////////////////////////////////

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.PURCHASE_CREDITS_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        inapps = new ArrayList<InAppPurchases.Message>();
        inapplist = (ListView) findViewById(R.id.credits_list_view);
        creditScreenAdapter = new CreditScreenAdapter(this, inapps);
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
}
