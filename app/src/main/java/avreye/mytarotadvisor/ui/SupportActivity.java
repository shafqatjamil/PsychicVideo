package avreye.mytarotadvisor.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.http.HttpClient;
import com.appboy.Appboy;
import com.appboy.ui.AppboyFeedbackFragment;

import avreye.mytarotadvisor.R;

public class SupportActivity extends AppCompatActivity {
    TextView textView_title;
    private TextView headerTitle;
    private TextView MyCredits;
    private Toolbar toolbar;
    ImageButton imageButton_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorPrimary));
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
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupportActivity.this, MainActivity.class);
                startActivity(intent);
                SupportActivity.this.finish();
            }
        });
        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.credit_bg);


        MyCredits.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        textView_credits.setVisibility(View.INVISIBLE);
        imageButton_back.setVisibility(View.VISIBLE);
        headerTitle.setText("Support");

        AppboyFeedbackFragment appboyFeedbackFragment = new AppboyFeedbackFragment();
        appboyFeedbackFragment.setFeedbackFinishedListener(new AppboyFeedbackFragment.FeedbackFinishedListener() {

            @Override
            public void onFeedbackFinished(AppboyFeedbackFragment.FeedbackResult feedbackResult) {
                // Here you can take action on the feedbackResult
                Toast.makeText(SupportActivity.this,"Sent",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SupportActivity.this, MainActivity.class);
                SupportActivity.this.startActivity(intent);
                SupportActivity.this.finish();
            }

            @Override
            public String beforeFeedbackSubmitted(String feedbackString) {
                return feedbackString;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_support, appboyFeedbackFragment);
        transaction.commit();



    }
    public void onStart() {
        super.onStart();
        Appboy.getInstance(SupportActivity.this).openSession(SupportActivity.this);
    }
    public void onStop() {
        super.onStop();
        Appboy.getInstance(SupportActivity.this).closeSession(SupportActivity.this);
    }
}
