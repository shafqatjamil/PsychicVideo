package avreye.mytarotadvisor.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.appboy.Appboy;

import avreye.mytarotadvisor.R;

public class VideoOrderConfirmationScreen extends AppCompatActivity {

    Button buttonHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_order_confirmation_screen);
        buttonHome = (Button) findViewById(R.id.video_order_confirmayion_screen_home_button);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoOrderConfirmationScreen.this.finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }
    public void onStart() {
        super.onStart();
        Appboy.getInstance(VideoOrderConfirmationScreen.this).openSession(VideoOrderConfirmationScreen.this);
    }
    public void onStop() {
        super.onStop();
        Appboy.getInstance(VideoOrderConfirmationScreen.this).closeSession(VideoOrderConfirmationScreen.this);
    }
}
