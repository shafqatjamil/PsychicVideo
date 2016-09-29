package avreye.mytarotadvisor.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.appboy.Appboy;
import com.appboy.enums.CardCategory;
import com.appboy.ui.AppboyFeedFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.squareup.picasso.Picasso;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.Object.AdvisorProfilePictureResponse;
import avreye.mytarotadvisor.Object.GetMyCreditsResponse;
import avreye.mytarotadvisor.Object.PromoCodeReponse;
import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.ClickableImageView;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.credentail.ClientLoginActivity;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TextView headerTitle;
    private TextView MyCredits;
    private BottomBar mBottomBar;
    AppController appController;
    MessageCenter mMessageCenter;
    UserSession mUserSession;
    private Retrofit retrofit;
    private Retrofit retrofit1;
    private Retrofit retrofit_Coupon;
    boolean flag;
    ClickableImageView imageView1;
    ClickableImageView imageView2;
    ClickableImageView imageView3;
    ClickableImageView imageView4;
    com.joooonho.SelectableRoundedImageView selectableRoundedImageView;
    Menu menu;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserSession = new UserSession(this);
        flag = false;
        Log.e("MainActivity", "Registering for PushNotifications");
        sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        if (mUserSession.getUserType().equals("client")) {
            imageView1 = (ClickableImageView) findViewById(R.id.tabbar_home);
            imageView2 = (ClickableImageView) findViewById(R.id.tabbar_message);
            imageView3 = (ClickableImageView) findViewById(R.id.tabbar_news);
            imageView4 = (ClickableImageView) findViewById(R.id.tabbar_dailytarot);
            imageView1.SetActivated();
            imageView2.SetDeActivated();
            imageView3.SetDeActivated();
            imageView4.SetDeActivated();
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView1.SetActivated();
                    imageView2.SetDeActivated();
                    imageView3.SetDeActivated();
                    imageView4.SetDeActivated();



                    String sourceString = "OUR" + "<b>" + "ADVISORS" + "</b> ";
                    headerTitle.setGravity(Gravity.CENTER);
                    headerTitle.setText(Html.fromHtml(sourceString));

                    Fragment newFragment = new AdvisorListFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
                    transaction.replace(R.id.container, newFragment);
                    transaction.commit();

                    mMessageCenter = null;
                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView1.SetDeActivated();
                    imageView2.SetActivated();
                    imageView3.SetDeActivated();
                    imageView4.SetDeActivated();
                    String sourceString = "<b>" + "MESSAGE" + "</b> " + "CENTER";
                    headerTitle.setGravity(Gravity.CENTER);
                    headerTitle.setText(Html.fromHtml(sourceString));

                    Fragment messageFragment = new MessageCenter();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
                    transaction.replace(R.id.container, messageFragment);
                    transaction.commit();
                    mMessageCenter = (MessageCenter) messageFragment;
                }
            });
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView1.SetDeActivated();
                    imageView2.SetDeActivated();
                    imageView3.SetActivated();
                    imageView4.SetDeActivated();

                    String sourceString = "NEWSFEED";
                    headerTitle.setGravity(Gravity.CENTER);
                    headerTitle.setText(Html.fromHtml(sourceString));
                    AppboyFeedFragment messageFragment = new AppboyFeedFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
                    transaction.replace(R.id.container, messageFragment);
                    transaction.commit();
                }
            });
            imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView1.SetDeActivated();
                    imageView2.SetDeActivated();
                    imageView3.SetDeActivated();
                    imageView4.SetActivated();
                    String sourceString = "DAILYTAROT";
                    headerTitle.setGravity(Gravity.CENTER);
                    headerTitle.setText(Html.fromHtml(sourceString));
                    mMessageCenter = null;
                    Fragment userListFragment = new DailyTarotAdvisorFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
                    transaction.replace(R.id.container, userListFragment);
                    transaction.commit();
                }
            });
        }
        else
        {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.tabbar);
            linearLayout.setVisibility(View.GONE);
        }







        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        appController = (AppController) getApplicationContext();
        appController.SetActivity(this);
        headerTitle = (TextView) toolbar.findViewById(R.id.title_text);
        MyCredits = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        TextView textView_credits1 = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        textView_credits.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits1.setTypeface(Typeface.create("MyriadPro-Cond", Typeface.NORMAL));
        textView_credits.setTextSize(12f);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.credit_bg);

        if(mUserSession.getUserType().contains("advisor"))
        {
            MyCredits.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView_credits.setVisibility(View.INVISIBLE);


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        //  ImageView imageView = (ImageView) headerView.findViewById(R.id.nav_header_user_image);
        TextView textView_name = (TextView) headerView.findViewById(R.id.nav_header_user_name);
        TextView textView_email = (TextView) headerView.findViewById(R.id.nav_header_user_email);
        TextView textView_version = (TextView) headerView.findViewById(R.id.nav_header_user_version);
        selectableRoundedImageView = (com.joooonho.SelectableRoundedImageView) headerView.findViewById(R.id.nav_header_user_image);
        selectableRoundedImageView.setVisibility(View.GONE);
        textView_name.setText(mUserSession.getUserName());
        textView_email.setText(mUserSession.getUserDOB());
        if(mUserSession.getUserType().contains("advisor"))
            textView_email.setVisibility(View.GONE);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int verCode = pInfo.versionCode;
        textView_version.setText("Version: " + verCode);


        menu = navigationView.getMenu();


        ////Deafault activity/////
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CLIENT_API_BASE_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit1 = new Retrofit.Builder()
                .baseUrl(Constants.Advisor_API_BASE_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit_Coupon = new Retrofit.Builder()
                .baseUrl(Constants.Promocode_API_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (mUserSession.getUserType().equals("client")) {

            String sourceString = "OUR" + "<b>" + "ADVISORS" + "</b> ";
            headerTitle.setGravity(Gravity.CENTER);
            headerTitle.setText(Html.fromHtml(sourceString));
            MyCredits.setText(mUserSession.getUserCredits());



            menu.getItem(3).setVisible(false);
            menu.getItem(9).setVisible(false);
            menu.getItem(10).setVisible(false);

//            if(getIntent().getStringExtra("flag") != null && getIntent().getStringExtra("flag") != "")
//            {
//                flag = true;
//                Log.e("mainActivity","Review");
//                Fragment userListFragment = new ReviewFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container, userListFragment);
//                transaction.commit();
//
//            }
//            else
//            {
                Fragment userListFragment = new AdvisorListFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, userListFragment);
                transaction.commit();
           // }

            mMessageCenter = null;
        } else {
            String sourceString = "<i>MY</i>" + "<b>" + "ORDERS" + "</b> ";
            headerTitle.setGravity(Gravity.CENTER);
            headerTitle.setText(Html.fromHtml(sourceString));
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(false);
            menu.getItem(6).setVisible(false);
            menu.getItem(7).setVisible(false);
            menu.getItem(8).setVisible(false);


            Fragment messageFragment = new MessageCenter();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, messageFragment);
            transaction.commit();
            mMessageCenter = (MessageCenter) messageFragment;

            selectableRoundedImageView.setVisibility(View.VISIBLE);
            GetProfilePicture(mUserSession.getUserId());

        }
        ////////////bottom bar//////////////////
//        if (mUserSession.getUserType().equals("client")) {
//            mBottomBar = BottomBar.attach(this, savedInstanceState);
//            mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
//                @Override
//                public void onMenuTabSelected(@IdRes int menuItemId) {
//                    if (menuItemId == R.id.bottomBarItemOne) {
//                        getSupportActionBar().show();
//
//
//                        String sourceString = "<i>OUR</i>" + "<b>" + "ADVISORS" + "</b> ";
//                        headerTitle.setGravity(Gravity.CENTER);
//                        headerTitle.setText(Html.fromHtml(sourceString));
//
//                        Fragment newFragment = new AdvisorListFragment();
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
//                        transaction.replace(R.id.container, newFragment);
//                        transaction.commit();
//
//                        mMessageCenter = null;
//                        // The user selected item number one.
//                    } else if (menuItemId == R.id.bottomBarItemTwo) {
//                        // The user selected item number one.
//                        String sourceString =  "<b>" + "MESSAGE" + "</b> " + "<i>CENTER </i>" ;
//                        headerTitle.setGravity(Gravity.CENTER);
//                        headerTitle.setText(Html.fromHtml(sourceString));
//
//                        getSupportActionBar().show();
//                        Fragment messageFragment = new MessageCenter();
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
//                        transaction.replace(R.id.container, messageFragment);
//                        transaction.commit();
//                        mMessageCenter = (MessageCenter) messageFragment;
//                    }else if (menuItemId == R.id.bottomBarItemThree) {
//                        // The user selected item number one.
//                        String sourceString =  "NEWSFEED" ;
//                        headerTitle.setGravity(Gravity.CENTER);
//                        headerTitle.setText(Html.fromHtml(sourceString));
//                        getSupportActionBar().show();
//                        AppboyFeedFragment messageFragment = new AppboyFeedFragment();
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
//                        transaction.replace(R.id.container, messageFragment);
//                        transaction.commit();
//                       // mMessageCenter = (MessageCenter) messageFragment;
//                    } else if (menuItemId == R.id.bottomBarItemFour) {
//
//                        getSupportActionBar().hide();
//                        mMessageCenter = null;
//                        Fragment userListFragment = new DailyTarotAdvisorFragment();
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
//                        transaction.replace(R.id.container, userListFragment);
//                        transaction.commit();
//
//
//                    }
//                }
//
//                @Override
//                public void onMenuTabReSelected(@IdRes int menuItemId) {
//                    if (menuItemId == R.id.bottomBarItemOne) {
//                        // The user reselected item number one, scroll your content to top.
//                    }
//                    if (menuItemId == R.id.bottomBarItemTwo) {
//                        // The user reselected item number one, scroll your content to top.
//                    }
//                }
//
//            });
//
//            // Setting colors for different tabs when there's more than three of them.
//            // You can set colors for tabs in three different ways as shown below.
//            //  mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
//
//            mBottomBar.mapColorForTab(0, "#612fd4");
//            mBottomBar.mapColorForTab(1, "#612fd4");
//            mBottomBar.mapColorForTab(2, "#612fd4");
//            mBottomBar.mapColorForTab(3, "#612fd4");
//            // mBottomBar.mapColorForTab(4, "#FF9800");
//        }
        //////////////////////////////////////////////////////////////////////////////////////
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_faq) {
            mMessageCenter = null;
            Intent intent = new Intent(MainActivity.this, FaqTosPp.class);
            intent.putExtra("screenname","Faq");
            intent.putExtra("url","https://storage.googleapis.com/www.psychictxt.com/FAQ.htm");
            startActivity(intent);
        } else if (id == R.id.nav_tutorial) {
            mMessageCenter = null;
            Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchase_credits) {
            mMessageCenter = null;
            Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
            intent.putExtra("message", "no message");
            startActivity(intent);
        } else if (id == R.id.promo_code) {
            mMessageCenter = null;
            Intent intent = new Intent(MainActivity.this, PromoCodeActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_order_history) {
            mMessageCenter = null;
            Intent intent = new Intent(MainActivity.this, OrderHistory.class);
            startActivity(intent);

        } else if (id == R.id.nav_suppport) {


            mMessageCenter = null;
            Intent intent = new Intent(MainActivity.this, SupportActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_privace_policy) {
            mMessageCenter = null;
            Intent intent = new Intent(MainActivity.this, FaqTosPp.class);
            intent.putExtra("screenname","Privacy Policy");
            intent.putExtra("url","https://storage.googleapis.com/www.psychictxt.com/Mytarotadvisor_PRIVACYPOLICY_web.htm");
            startActivity(intent);
        } else if (id == R.id.nav_term_of_services) {

            mMessageCenter = null;
            Intent intent = new Intent(MainActivity.this, FaqTosPp.class);
            intent.putExtra("screenname","Term of Services");
            intent.putExtra("url","https://storage.googleapis.com/www.psychictxt.com/mytarotadvisorTOS_web.htm");
            startActivity(intent);
        }  else if (id == R.id.logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setMessage("Are you sure you want to logout.")
                    .setTitle("Logout")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new UserSession(MainActivity.this).destroyUserSession();
                            startActivity(new Intent(MainActivity.this, ClientLoginActivity.class));
                            dialog.dismiss();
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else if (id == R.id.nav_availability) {

            Intent intent = new Intent(MainActivity.this, AdvisorAvailabilityActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_settings) {

            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_ratting) {

            Intent intent = new Intent(MainActivity.this, AdvisorRating.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /////////////////////////////////////////////////////////////////////////////

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        if (mUserSession.getUserType().equals("client"))
//            mBottomBar.onSaveInstanceState(outState);
//    }
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onPause() {
        super.onPause();
        appController.SetActivity(null);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mMessageReceiver);

/*
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        appController.SetActivity(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("message_receiver"));


       // registerReceiver();
        if(mUserSession.getUserType().equals("client")) {
            GetMyCredits(mUserSession.getUserId());
            isCouponAvailable();
        }
    }
    void GetMyCredits(final String id) {
        APIService apiservice = retrofit.create(APIService.class);
        Call<GetMyCreditsResponse> APICall = apiservice.getMyCredits(id);
        APICall.enqueue(new Callback<GetMyCreditsResponse>() {
            @Override
            public void onResponse(Call<GetMyCreditsResponse> call, Response<GetMyCreditsResponse> response) {

                if (response.body() != null) {

                    if (response.body().getResult() == 1) {

                        mUserSession.setUserCredits(response.body().getMessage());
                        ((TextView)toolbar.findViewById(R.id.toolbar_credits)).setText(mUserSession.getUserCredits());

                    } else {
                        Log.e("message history" ,"error");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMyCreditsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    void GetProfilePicture(final String id) {
        APIService apiservice = retrofit1.create(APIService.class);
        Call<AdvisorProfilePictureResponse> APICall = apiservice.getAdvisorPicture(id);
        APICall.enqueue(new Callback<AdvisorProfilePictureResponse>() {
            @Override
            public void onResponse(Call<AdvisorProfilePictureResponse> call, Response<AdvisorProfilePictureResponse> response) {

                if (response.body() != null) {

                    if (response.body().getResult() == 1) {


                        Picasso.with(MainActivity.this).load(Constants.Advisor_IMAGE_URL  + response.body().getMessage()).into(selectableRoundedImageView);

                    } else {
                        Log.e("message history" ,"error");
                    }
                }
            }

            @Override
            public void onFailure(Call<AdvisorProfilePictureResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (mMessageCenter != null) {
                mMessageCenter.receiveMessageFromMainScreen();
            }

        }
    };



    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        Pubnub pubnub = new Pubnub(Constants.PUBNUB_PUBLISH_KEY, Constants.PUBNUB_SUBSCRIBE_KEY);
        pubnub.enablePushNotificationsOnChannel(new UserSession(getApplicationContext()).getUserChannelName(), token, new com.pubnub.api.Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                Log.e("sendRServerCalled", message.toString());
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
                Log.e("sendRServerCalledE", error.toString());
            }
        });
    }

    void isCouponAvailable()
    {
        APIService apiservice = retrofit_Coupon.create(APIService.class);
        Call<GetMyCreditsResponse> APICall = apiservice.isCoupunAvailable();
        APICall.enqueue(new Callback<GetMyCreditsResponse>() {
            @Override
            public void onResponse(Call<GetMyCreditsResponse> call, Response<GetMyCreditsResponse> response) {

                if (response.body() != null) {

                    if (response.body().getResult() == 1) {

                        menu.getItem(3).setVisible(true);

                    } else {
                        Log.e("message history" ,"error");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMyCreditsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onStart() {
        super.onStart();
        Appboy.getInstance(MainActivity.this).openSession(MainActivity.this);
    }
    public void onStop() {
        super.onStop();
        Appboy.getInstance(MainActivity.this).closeSession(MainActivity.this);
    }

}
