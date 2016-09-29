package avreye.mytarotadvisor.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    ArrayList<Message> MessageList;

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

        listView_orders = (ListView) findViewById(R.id.orderhistory_listview);
        databaseHelper = new DatabaseHelper(this);
        orderHistoryAdapter = new OrderHistoryAdapter(this, new ArrayList<OrderHistoryItemObject>());
        assert listView_orders != null;
        listView_orders.setAdapter(orderHistoryAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();

        MessageList = databaseHelper.GetOrderHistory(userSession.getUserId());
        ArrayList<OrderHistoryItemObject> Orders = new ArrayList<>();
        ArrayList<Message> messages = MessageList;
        //write here logic for extracting messages

        for (int i = 0; i < MessageList.size(); i++) {
            if (MessageList.get(i).getSender_id().contains(userSession.getUserId())) {

                Date date1 = new Date();
                Date date2 = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date1 = sdf.parse(MessageList.get(i).getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                OrderHistoryItemObject orderHistoryItemObject = new OrderHistoryItemObject();
                orderHistoryItemObject.setOrderDate(MessageList.get(i).getDate());
                orderHistoryItemObject.setAdvisorName(MessageList.get(i).getReciever_display_name());
                orderHistoryItemObject.setDeliverd("Pending");
                String advid = MessageList.get(i).getReciever_id();

                for (int j = messages.size()-1; j > 0; j--) {
                    try {
                        date2 = sdf.parse(messages.get(j).getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (messages.get(j).getSender_id().contains(advid) && date1.compareTo(date2) < 0) {
                        orderHistoryItemObject.setDeliverd(messages.get(j).getDate());
                    //    messages.remove(j);
                        break;
                    }
                }
                Orders.add(orderHistoryItemObject);
            }
        }
        Collections.reverse(Orders);
           orderHistoryAdapter.SetUserList(Orders);
    }
}
