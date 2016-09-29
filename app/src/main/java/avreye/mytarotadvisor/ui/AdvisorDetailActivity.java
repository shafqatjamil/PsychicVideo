package avreye.mytarotadvisor.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appboy.Appboy;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.Object.AdvisorInfo;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.MlRoundedImageView;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.credentail.RegisterationActivity;
import avreye.mytarotadvisor.utils.Constants;


public class AdvisorDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    android.widget.ImageView imageViewVideo;
    MlRoundedImageView mlRoundedImageViewProfilePic;
    ImageView buttonVideoPlay;
    Button buttonVideoReading;
    TextView textViewAdvisorName;
    TextView textViewTitle;
    TextView textViewDescription;
    TextView textViewTotalReviews;
    com.iarcuschin.simpleratingbar.SimpleRatingBar ratingBar;
    LinearLayout reviewLayout;

    private ProgressDialog progressDialog;
    String adviserVideoName = null;

    UserSession mUserSession;
    boolean advisor_status;

    Animation animFadein;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_page);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        Appboy.getInstance(AdvisorDetailActivity.this).logCustomEvent(getIntent().getStringExtra("advisor_name")+"_PsychicProfileViewed");

        mUserSession = new UserSession(this);
        imageViewVideo = (ImageView) findViewById(R.id.advisor_page_video_view);
        mlRoundedImageViewProfilePic = (MlRoundedImageView) findViewById(R.id.ap_profile_pic);
        buttonVideoPlay = (ImageView) findViewById(R.id.ap_video_play);
        buttonVideoReading = (Button) findViewById(R.id.ap_button_video_reading);
        textViewAdvisorName = (TextView) findViewById(R.id.ap_advisor_name);
        textViewTitle = (TextView) findViewById(R.id.ap_advisor_title);
        textViewDescription = (TextView) findViewById(R.id.ap_advisor_description);
        textViewTotalReviews = (TextView) findViewById(R.id.ap_textview_totalreviews);
        ratingBar = (com.iarcuschin.simpleratingbar.SimpleRatingBar) findViewById(R.id.ap_ratingbar);
        reviewLayout = (LinearLayout) findViewById(R.id.ap_review_layout);


        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        progressDialog = new ProgressDialog(this);
        buttonVideoPlay.setVisibility(View.GONE);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.button_animation);
        advisor_status = false;
        getAdviserList(getIntent().getStringExtra("advisor_id"));
        if(!getIntent().getStringExtra("advisor_status").contains("1"))
        {
            buttonVideoReading.setEnabled(false);
            buttonVideoReading.setBackgroundColor(getResources().getColor(R.color.aluminum));
            buttonVideoReading.setText("I'M CURRENTLY UNAVAILABLE PLEASE CHECK BACK LATER");
            buttonVideoReading.setTextSize(10f);
        }




        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.findViewById(R.id.title_text).setVisibility(View.GONE);
        ((TextView) toolbar.findViewById(R.id.toolbar_credits)).setText(new UserSession(this).getUserCredits());
        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        TextView textView_credits1 = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        textView_credits.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits1.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits.setTextSize(12f);



        buttonVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adviserVideoName != null) {
                    Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                    intent.putExtra("advisor_video", Constants.Advisor_Video_URL + adviserVideoName);
                    intent.putExtra("advisor_name", getIntent().getStringExtra("advisor_name"));
                    startActivity(intent);
                }
            }
        });

        buttonVideoReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  buttonVideoReading.startAnimation(animFadein);
                Intent intent = new Intent(getApplicationContext(),ClientSendMessage.class);
                intent.putExtra("advisor_id", getIntent().getStringExtra("advisor_id"));
                intent.putExtra("advisor_name", getIntent().getStringExtra("advisor_name"));
                intent.putExtra("advisor_description", getIntent().getStringExtra("advisor_description"));
                intent.putExtra("advisor_instruction", getIntent().getStringExtra("advisor_instruction"));
                intent.putExtra("advisor_picture", getIntent().getStringExtra("advisor_picture"));
                startActivity(intent);
            }
        });
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
                    if (userInfo != null) {
                        if(userInfo.getStatus().contains("0") || userInfo.getDeactivated().contains("1")) {
                            buttonVideoReading.setEnabled(false);
                            buttonVideoReading.setBackgroundColor(getResources().getColor(R.color.aluminum));
                            buttonVideoReading.setText("I'M CURRENTLY UNAVAILABLE PLEASE CHECK BACK LATER");
                            buttonVideoReading.setTextSize(10f);
                        }
                        Picasso.with(AdvisorDetailActivity.this).load(Constants.Advisor_Video_Thumbnail_URL + userInfo.getVideoThumb()).into(imageViewVideo);
                        Picasso.with(AdvisorDetailActivity.this).load(Constants.Advisor_IMAGE_URL + userInfo.getDisplayPicture()).into(mlRoundedImageViewProfilePic);
                        textViewAdvisorName.setText(userInfo.getFirstName());
                        textViewTitle.setText(Html.fromHtml(userInfo.getShortDescription()));
                        textViewDescription.setText(Html.fromHtml(userInfo.getBioData()));
                        adviserVideoName = userInfo.getIntroVideo();
                        ratingBar.setRating(userInfo.getRating());

                        if (userInfo.getIntroVideo() != null && userInfo.getIntroVideo() != "") {
                            buttonVideoPlay.setVisibility(View.VISIBLE);
                        }
                        textViewTotalReviews.setText("Reviews(" + userInfo.getReviews().size() + ")");
                        for (int i = 0; i < userInfo.getReviews().size(); i++) {
                            LayoutInflater layoutInflater = LayoutInflater.from(AdvisorDetailActivity.this);
                            View ratingView = layoutInflater.inflate(R.layout.review_item, null, false);
                            TextView reviewerName = (TextView) ratingView.findViewById(R.id.review_item_reviewer_name);
                            TextView reviewerComment = (TextView) ratingView.findViewById(R.id.review_item_reviewer_comment);
                            com.iarcuschin.simpleratingbar.SimpleRatingBar reviewerRating = (com.iarcuschin.simpleratingbar.SimpleRatingBar) ratingView.findViewById(R.id.review_rating);
                            reviewerRating.setOnTouchListener(new View.OnTouchListener() {
                                public boolean onTouch(View v, MotionEvent event) {
                                    return true;
                                }
                            });
                            reviewerName.setText(userInfo.getReviews().get(i).getClientUsername());
                            reviewerComment.setText(userInfo.getReviews().get(i).getComment());
                            reviewerRating.setRating(Float.parseFloat(userInfo.getReviews().get(i).getReview()));

                            reviewLayout.addView(ratingView);
                        }

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

        // Adding request to  queue
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
            AdvisorDetailActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
    public void onStart() {
        super.onStart();
        Appboy.getInstance(AdvisorDetailActivity.this).openSession(AdvisorDetailActivity.this);
    }
    public void onStop() {
        super.onStop();
        Appboy.getInstance(AdvisorDetailActivity.this).closeSession(AdvisorDetailActivity.this);
    }
}
