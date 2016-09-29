package avreye.mytarotadvisor.ui;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.appboy.Appboy;

import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.UserSession;

public class VideoPlayerActivity extends AppCompatActivity {

    Toolbar toolbar;

    UserSession mUserSession;
    private TextView MyCredits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mUserSession = new UserSession(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) toolbar.findViewById(R.id.title_text);
        String sourceString = "Back";//getIntent().getStringExtra("advisor_name");
        textView.setText(Html.fromHtml(sourceString));

        toolbar.findViewById(R.id.title_text).setVisibility(View.GONE);
        ((TextView) toolbar.findViewById(R.id.toolbar_credits)).setText(new UserSession(this).getUserCredits());
        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        TextView textView_credits1 = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        textView_credits.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits1.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits.setTextSize(12f);

        final VideoView videoView =
                (VideoView) findViewById(R.id.mp_video_view);

        if(getIntent().getStringExtra("video_url") != null)
        {

        }
        videoView.setVideoPath( getIntent().getStringExtra("advisor_video"));
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });

        videoView.start();




        MyCredits = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.credit_bg);
        if(mUserSession.getUserType().contains("advisor"))
        {
            MyCredits.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView_credits.setVisibility(View.INVISIBLE);


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            VideoPlayerActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
}