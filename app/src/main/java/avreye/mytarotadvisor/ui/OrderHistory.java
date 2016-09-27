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
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.Object.OrderHistoryItemObject;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.MessageCenterAdapter;
import avreye.mytarotadvisor.adapter.OrderHistoryAdapter;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.helper.UserSession;

public class OrderHistory extends AppCompatActivity {
    TextView textView_title;
    private TextView headerTitle;
    private TextView MyCredits;
    private Toolbar toolbar;
    ImageButton imageButton_back;
    Activity activity;
    private ProgressDialog progDailog;
    UserSession userSession;
    ArrayList<OrderHistoryItemObject> MessageList;

    OrderHistoryAdapter orderHistoryAdapter;
    ListView listView_orders;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        userSession = new UserSession(this);
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
        imageButton_back.setVisibility(View.VISIBLE);
        headerTitle.setText("OrderHistory");


        activity = this;

       // progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        //progDailog.setCancelable(false);

        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderHistory.this, MainActivity.class);
                startActivity(intent);
                OrderHistory.this.finish();
            }
        });

        listView_orders = (ListView) findViewById(R.id.orderhistory_listview);databaseHelper = new DatabaseHelper(this);
        orderHistoryAdapter = new OrderHistoryAdapter(this, new ArrayList<OrderHistoryItemObject>());
        assert listView_orders != null;
        listView_orders.setAdapter(orderHistoryAdapter);


    }
    @Override
    public void onResume() {
        super.onResume();

        MessageList = databaseHelper.GetOrderHistory(userSession.getUserId());
            orderHistoryAdapter.SetUserList(MessageList);
    }
}
