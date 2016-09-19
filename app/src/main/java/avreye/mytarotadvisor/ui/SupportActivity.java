package avreye.mytarotadvisor.ui;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.amazonaws.http.HttpClient;
import com.appboy.Appboy;

import avreye.mytarotadvisor.R;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

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
