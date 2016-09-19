package avreye.mytarotadvisor.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.appboy.Appboy;

import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.UserSession;

public class VideoPlayerActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

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