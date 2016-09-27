package avreye.mytarotadvisor.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.Object.OrderHistoryItemObject;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.ChatActivityforAdvisor;
import avreye.mytarotadvisor.ui.ChatActivityforUser;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shafqat on 9/27/16.
 */

public class OrderHistoryAdapter extends BaseAdapter {
    ArrayList<OrderHistoryItemObject> UserList;
    Context mContext;
    UserSession userSession;
    public OrderHistoryAdapter(Context mContext, ArrayList<OrderHistoryItemObject> userlist) {
        this.mContext = mContext;
        this.UserList = userlist;
        userSession = new UserSession(mContext);

    }
    @Override
    public int getCount() {
        return UserList.size();
    }

    @Override
    public Object getItem(int position) {
        return UserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final OrderHistoryAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.item_orderhistory, null);
            viewHolder = new ViewHolder();

            viewHolder.textView_orderdate = (TextView) convertView.findViewById(R.id.item_orderhistory_date);
            viewHolder.textView_advisorname = (TextView) convertView.findViewById(R.id.item_orderhistory_advisor);
            viewHolder.textView_deliverd = (TextView) convertView.findViewById(R.id.item_orderhistory_deliverd);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderHistoryAdapter.ViewHolder) convertView.getTag();
        }

      ////////////write here
        viewHolder.textView_orderdate.setText(UserList.get(position).getOrderDate());
        viewHolder.textView_advisorname.setText(UserList.get(position).getAdvisorName());
        viewHolder.textView_deliverd.setText(UserList.get(position).getDeliverd());


        return convertView;
    }
    class ViewHolder {
        TextView textView_orderdate;
        TextView textView_advisorname;
        TextView textView_deliverd;



    }
    public void SetUserList(ArrayList<OrderHistoryItemObject> userList) {
        this.UserList = userList;
        notifyDataSetChanged();
    }


}
