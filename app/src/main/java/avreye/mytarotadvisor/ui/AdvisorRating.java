package avreye.mytarotadvisor.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.Object.AdvisorInfo;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Retrofit;

public class AdvisorRating extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView headerTitle;
    Context context;
    Retrofit retrofit;
    Switch aSwitch;
    TextView textView_status;
    private View rootView;
    AppController appController;
    private UserSession mUserSession;
    private ProgressDialog progressDialog;
    LinearLayout reviewLayout;
    TextView textView_total_rating;
    TextView textView_average_rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_rating);
        mUserSession = new UserSession(this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }


        reviewLayout = (LinearLayout) findViewById(R.id.ratings);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        appController = (AppController) getApplicationContext();
        appController.SetActivity(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        headerTitle = (TextView) toolbar.findViewById(R.id.title_text);
        TextView MyCredits = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.credit_bg);

        String sourceString = "<i>MY</i>" + "<b>" + "RATING" + "</b> ";
        headerTitle.setGravity(Gravity.CENTER);
        headerTitle.setText(Html.fromHtml(sourceString));
        if (mUserSession.getUserType().contains("advisor")) {
            MyCredits.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView_credits.setVisibility(View.INVISIBLE);


        }

        textView_total_rating = (TextView) findViewById(R.id.total_rating);
        textView_average_rating = (TextView) findViewById(R.id.average_rating);
        progressDialog = new ProgressDialog(this);
        getAdviserList(mUserSession.getUserId());
        showDialog();
    }

    private void getAdviserList(final String id) {
        progressDialog.setMessage("Please wait ...");
        showDialog();
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
                    hideDialog();
                    textView_total_rating.setText(userInfo.getReviews().size()+"");
                    textView_average_rating.setText(userInfo.getRating()+"");
                    for (int i = 0; i < userInfo.getReviews().size(); i++) {
                        LayoutInflater layoutInflater = LayoutInflater.from(AdvisorRating.this);
                        View ratingView = layoutInflater.inflate(R.layout.rating_item, null, false);
                        TextView reviewerName = (TextView) ratingView.findViewById(R.id.rating_item_name);
                        TextView reviewerComment = (TextView) ratingView.findViewById(R.id.rating_item_comment);
                        TextView date = (TextView) ratingView.findViewById(R.id.rating_item_date);
                        com.iarcuschin.simpleratingbar.SimpleRatingBar reviewerRating = (com.iarcuschin.simpleratingbar.SimpleRatingBar) ratingView.findViewById(R.id.rating_item_ratingBar);
                        reviewerRating.setOnTouchListener(new View.OnTouchListener() {
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        reviewerName.setText(userInfo.getReviews().get(i).getClientUsername());
                        reviewerComment.setText(userInfo.getReviews().get(i).getComment());
                        String[] strs = userInfo.getReviews().get(i).getCreatedAt().split(" ");
                        date.setText(strs[0]);
                        reviewerRating.setRating(Float.parseFloat(userInfo.getReviews().get(i).getReview()));

                        reviewLayout.addView(ratingView);
                    }
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();


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
        AppController.getInstance().addToRequestQueue(strReq, "adviser");
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            AdvisorRating.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
