package avreye.mytarotadvisor.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appboy.Appboy;

import avreye.mytarotadvisor.R;

public class FaqTosPp extends AppCompatActivity {


    TextView textView_title;
    private TextView headerTitle;
    private TextView MyCredits;
    private Toolbar toolbar;
    ImageButton imageButton_back;
    Activity activity;
    private ProgressDialog progDailog;
    WebView webView_faq;

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


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        headerTitle = (TextView) toolbar.findViewById(R.id.title_text);
        MyCredits = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        imageButton_back = (ImageButton) toolbar.findViewById(R.id.tool_bar_backButton);
        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.credit_bg);


        MyCredits.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        textView_credits.setVisibility(View.INVISIBLE);
        webView_faq = (WebView) findViewById(R.id.faq_webview);
        imageButton_back.setVisibility(View.VISIBLE);
        headerTitle.setText(getIntent().getStringExtra("screenname"));


        activity = this;

        progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);


        webView_faq.getSettings().setJavaScriptEnabled(true);
        webView_faq.getSettings().setLoadWithOverviewMode(true);
        webView_faq.getSettings().setUseWideViewPort(true);
        webView_faq.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView_faq.getSettings().setBuiltInZoomControls(true);
        webView_faq.getSettings().setSupportZoom(true);

        webView_faq.setWebViewClient(new WebViewClient() {

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


        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqTosPp.this, MainActivity.class);
                startActivity(intent);
                FaqTosPp.this.finish();
            }
        });

    }

}
