package avreye.mytarotadvisor.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.appboy.Appboy;

import avreye.mytarotadvisor.R;

public class FaqTosPp extends AppCompatActivity {


    TextView textView_title;
    WebView webView_faq;

    Activity activity ;
    private ProgressDialog progDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_tos_pp);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        textView_title = (TextView) findViewById(R.id.faq_title);
        webView_faq = (WebView) findViewById(R.id.faq_webview);

        textView_title.setText(getIntent().getStringExtra("screenname"));



        activity = this;

        progDailog = ProgressDialog.show(activity, "Loading","Please wait...", true);
        progDailog.setCancelable(false);




        webView_faq.getSettings().setJavaScriptEnabled(true);
        webView_faq.getSettings().setLoadWithOverviewMode(true);
        webView_faq.getSettings().setUseWideViewPort(true);
        webView_faq.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView_faq.getSettings().setBuiltInZoomControls(true);
        webView_faq.getSettings().setSupportZoom(true);

        webView_faq.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });

        webView_faq.loadUrl(getIntent().getStringExtra("url"));
        //webView.loadUrl("file:///android_asset/index.html");


    }
    public void onStart() {
        super.onStart();
        Appboy.getInstance(FaqTosPp.this).openSession(FaqTosPp.this);
    }
    public void onStop() {
        super.onStop();
        Appboy.getInstance(FaqTosPp.this).closeSession(FaqTosPp.this);
    }
}
