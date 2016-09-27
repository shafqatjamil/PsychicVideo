package avreye.mytarotadvisor.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.Object.GetAdvisorStatusReponse;
import avreye.mytarotadvisor.Object.UpdateStatusResponse;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdvisorAvailabilityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView headerTitle;
    Context context;
    Retrofit retrofit;
    Switch aSwitch;
    TextView textView_status;
    private View rootView;
    ProgressDialog progressDialog;
    AppController appController;
    private UserSession mUserSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_availability);
        mUserSession = new UserSession(this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        appController = (AppController) getApplicationContext();
        appController.SetActivity(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        headerTitle = (TextView) toolbar.findViewById(R.id.title_text);
        TextView MyCredits = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.credit_bg);

        String sourceString = "<i>MY</i>" + "<b>" + "STATUS" + "</b> ";
        headerTitle.setGravity(Gravity.CENTER);
        headerTitle.setText(Html.fromHtml(sourceString));

//        ImageButton imageButton = (ImageButton) toolbar.findViewById(R.id.tool_bar_backButton);
//        imageButton.setVisibility(View.VISIBLE);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AdvisorAvailabilityActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
        if(mUserSession.getUserType().contains("advisor"))
        {
            MyCredits.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView_credits.setVisibility(View.INVISIBLE);


        }




        textView_status = (TextView) findViewById(R.id.status_text);
        context = this;
        aSwitch = (Switch) findViewById(R.id.switch1);
        if (new UserSession(context).getADVISOR_STATUS() == "1") {

            textView_status.setText("ACTIVE");
            textView_status.setTextColor(Color.parseColor("#FF00EEFF"));
            aSwitch.setChecked(true);
        } else {

            textView_status.setTextColor(Color.parseColor("#FFABA7A7"));
            textView_status.setText("INACTIVE");
            aSwitch.setChecked(false);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView_status.setText("ACTIVE");
                    textView_status.setTextColor(Color.parseColor("#FF00EEFF"));
                    new UserSession(context).setADVISOR_STATUS("1");
                    HitAPI("1");
                } else {
                    textView_status.setTextColor(Color.parseColor("#FFABA7A7"));
                    textView_status.setText("INACTIVE");
                    new UserSession(context).setADVISOR_STATUS("0");
                    HitAPI("0");
                }
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Advisor_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HitAPI_getStatus();
    }
    boolean HitAPI_getStatus() {

        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("wait...");
        progressDialog.show();
        APIService apiservice = retrofit.create(APIService.class);
        Call<GetAdvisorStatusReponse> APICall = apiservice.getStatus(new UserSession(context).getUserId());
        APICall.enqueue(new Callback<GetAdvisorStatusReponse>() {
            @Override
            public void onResponse(Call<GetAdvisorStatusReponse> call, Response<GetAdvisorStatusReponse> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().contains("1")) {

                        textView_status.setText("ACTIVE");
                        textView_status.setTextColor(Color.parseColor("#FF00EEFF"));
                        aSwitch.setChecked(true);
                    } else {
                        textView_status.setTextColor(Color.parseColor("#FFABA7A7"));
                        textView_status.setText("INACTIVE");
                        aSwitch.setChecked(false);
                    }

                    progressDialog.dismiss();

                } else {


                    progressDialog.dismiss();
                    Toast.makeText(AdvisorAvailabilityActivity.this,"Error occourd while getting status",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetAdvisorStatusReponse> call, Throwable t) {
                progressDialog.dismiss();

            }


        });
        return true;
    }

    void HitAPI(String status) {
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("wait...");
        progressDialog.show();
        APIService apiservice = retrofit.create(APIService.class);
        Call<UpdateStatusResponse> APICall = apiservice.UpdateStatus(new UserSession(context).getUserId(), status);
        APICall.enqueue(new Callback<UpdateStatusResponse>() {
            @Override
            public void onResponse(Call<UpdateStatusResponse> call, Response<UpdateStatusResponse> response) {

                if (response.body() != null) {


                } else {
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateStatusResponse> call, Throwable t) {

            }


        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            AdvisorAvailabilityActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
