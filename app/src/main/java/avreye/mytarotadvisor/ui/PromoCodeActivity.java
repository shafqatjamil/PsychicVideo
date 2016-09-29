package avreye.mytarotadvisor.ui;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.Object.PromoCodeReponse;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromoCodeActivity extends AppCompatActivity {
    TextView textView_title;
    private TextView headerTitle;
    private TextView MyCredits;
    private Toolbar toolbar;
    ImageButton imageButton_back;
    private ProgressDialog progDailog;
    UserSession userSession;
    private Retrofit retrofit_Coupon;
    private Retrofit retrofit;
    private ProgressDialog progressDialog;
    Button button;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);
        userSession = new UserSession(this);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        headerTitle = (TextView) toolbar.findViewById(R.id.title_text);
        MyCredits = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        imageButton_back = (ImageButton) toolbar.findViewById(R.id.tool_bar_backButton);
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

        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.credit_bg);
        ((TextView)toolbar.findViewById(R.id.toolbar_credits)).setText(userSession.getUserCredits());
        imageButton_back.setVisibility(View.VISIBLE);
        headerTitle.setText("Promo Code");
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromoCodeActivity.this, MainActivity.class);
                startActivity(intent);
                PromoCodeActivity.this.finish();
            }
        });
        retrofit_Coupon = new Retrofit.Builder()
                .baseUrl(Constants.Promocode_API_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CLIENT_API_BASE_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        progressDialog = new ProgressDialog(PromoCodeActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait...");
        button = (Button) findViewById(R.id.button_apply_promocode);
        editText = (EditText) findViewById(R.id.editText_promocode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyPromocode(userSession.getUserId(),editText.getText().toString());
            }
        });
    }


    void applyPromocode(String id, String promocode)
    {

        progressDialog.show();
        APIService apiservice = retrofit_Coupon.create(APIService.class);
        Call<PromoCodeReponse> APICall = apiservice.applyPromocode(id, promocode);
        APICall.enqueue(new Callback<PromoCodeReponse>() {
            @Override
            public void onResponse(Call<PromoCodeReponse> call, Response<PromoCodeReponse> response) {

                if (response.body() != null) {

                    if (response.body().getStatus() == 1) {

                        String temp = userSession.getUserCredits();
                        UpdateCredits(userSession.getUserId(),
                                Integer.parseInt(temp) + Integer.parseInt(response.body().getCredit()) + "");

                        userSession.setUserCredits(Integer.parseInt(temp) + Integer.parseInt(response.body().getCredit()) + "");

                        progressDialog.hide();
                    } else {
                        Toast.makeText(PromoCodeActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        progressDialog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<PromoCodeReponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                progressDialog.hide();
            }
        });
    }
    void UpdateCredits(final String id, final String credits) {

        progressDialog.show();
        APIService apiservice = retrofit.create(APIService.class);
        Call<MessageHistoryResponse> APICall = apiservice.updateCredits(id, credits);
        APICall.enqueue(new Callback<MessageHistoryResponse>() {
            @Override
            public void onResponse(Call<MessageHistoryResponse> call, Response<MessageHistoryResponse> response) {
                if (response.body() != null) {

                    if (response.body().getResult() == 1) {

                        ((TextView)toolbar.findViewById(R.id.toolbar_credits)).setText(userSession.getUserCredits());
                        Intent intent = new Intent(PromoCodeActivity.this,MainActivity.class);
                        PromoCodeActivity.this.startActivity(intent);

                    } else {
                        Log.e("message history" ,"error");
                    }

                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<MessageHistoryResponse> call, Throwable t) {

                progressDialog.hide();
            }
        });
    }
}
