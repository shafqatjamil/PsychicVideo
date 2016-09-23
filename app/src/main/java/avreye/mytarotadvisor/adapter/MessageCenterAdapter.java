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

import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.ChatActivityforAdvisor;
import avreye.mytarotadvisor.ui.ChatActivityforUser;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by null on 4/21/2016.
 */
public class MessageCenterAdapter extends BaseAdapter {
    ArrayList<Message> UserList;
    Context mContext;
    UserSession userSession;
    Retrofit retrofit;
    private ArrayList<Message> mOriginalValues; // Original Values
    private ArrayList<Message> mDisplayedValues;
    public MessageCenterAdapter(Context mContext, ArrayList<Message> userlist) {
        this.mOriginalValues = userlist;
        this.mDisplayedValues = userlist;
        this.mContext = mContext;
        this.UserList = userlist;
        userSession = new UserSession(mContext);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Advisor_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.message_center_item, null);
            viewHolder = new ViewHolder();


            viewHolder.selectableRoundedImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.message_center_item_image);
            viewHolder.textView_last_texter_name = (TextView) convertView.findViewById(R.id.message_center_item_last_texter_name);
            viewHolder.textView_last_text_time = (com.github.curioustechizen.ago.RelativeTimeTextView) convertView.findViewById(R.id.message_center_item_last_text_time);
            viewHolder.message_center_item_date = (com.github.curioustechizen.ago.RelativeTimeTextView) convertView.findViewById(R.id.message_center_item_date);
            viewHolder.textView_last_text = (TextView) convertView.findViewById(R.id.message_center_item_last_text);
            viewHolder.relativeLayout_Clients = (RelativeLayout)convertView.findViewById(R.id.message_center_item_inner);



            viewHolder.linearLayout_Advisor = (LinearLayout) convertView.findViewById(R.id.message_center_advisor_item);
            viewHolder.textView_date = (com.github.curioustechizen.ago.RelativeTimeTextView) convertView.findViewById(R.id.message_center_item_date);
            viewHolder.textView_complete = (TextView) convertView.findViewById(R.id.message_center_item_is_complete);
            viewHolder.textView_from = (TextView) convertView.findViewById(R.id.message_center_item_from);
            viewHolder.textView_dob = (TextView) convertView.findViewById(R.id.message_center_item_is_dob);
            viewHolder.textView_message = (TextView) convertView.findViewById(R.id.message_center_item_messgage);
            viewHolder.textView_videoorder = (TextView) convertView.findViewById(R.id.message_center_item_video_order);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView_videoorder.setText(UserList.get(position).getType());
        if(userSession.getUserType().contains("advisor"))
        {
            viewHolder.textView_dob.setText(UserList.get(position).getDob());
            Log.e("DOB",UserList.get(position).getDob());
            if(UserList.get(position).getSender_id().contains(userSession.getUserId()))
            {
                viewHolder.textView_complete.setText("COMPLETE");
                viewHolder.textView_complete.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary) );
            }
            else
            {
                viewHolder.textView_complete.setText("PENDING");
                viewHolder.textView_complete.setTextColor(ContextCompat.getColor(mContext,R.color.text_orange) );
            }
        }

        if(new UserSession(mContext).getUserType().contains("client"))
        {
            viewHolder.linearLayout_Advisor.setVisibility(View.GONE);
            viewHolder.relativeLayout_Clients.setVisibility(View.VISIBLE);
            String OpponentName = "";
            String Advisorid = "";
            if(UserList.get(position).getSender_id().contains(userSession.getUserId()))
            {
                OpponentName = UserList.get(position).getReciever_display_name();
                Advisorid = UserList.get(position).getReciever_id();
            }
            else
            {
                OpponentName = UserList.get(position).getSender_display_name();
                Advisorid = UserList.get(position).getSender_id();
            }

            final String advisorname = OpponentName;
            final String advisorid = Advisorid;
            viewHolder.textView_last_texter_name.setText(OpponentName);///change it


            if(UserList.get(position).getText().toString().contains("/~*/")) {
                int index = UserList.get(position).getText().toString().indexOf("/~*/");
                String question = UserList.get(position).getText().toString().substring(0, index);
                viewHolder.textView_last_text.setText(question);
            }
            else
            {
                viewHolder.textView_last_text.setText(UserList.get(position).getText().toString());
            }



            String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z" ;
            final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            long timeStamp= 0;
            try {
                timeStamp = sdf.parse(UserList.get(position).getDate()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(timeStamp));
            cal.setTimeZone(TimeZone.getDefault());
            System.out.println(sdf.format(cal.getTime()));

            String steTime = sdf.format(cal.getTime());
            try {
                long et = toEpoch(steTime.substring(0,steTime.length()-5));
                viewHolder.textView_last_text_time.setReferenceTime(et);
            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.textView_last_text_time.setText(steTime.substring(0,steTime.length()-5));


            Picasso.with(mContext).load(userSession.getUserProfilePictureUrl(advisorid)).into(viewHolder.selectableRoundedImageView);






            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ChatActivityforUser.class);
                    intent.putExtra("advisor_id", advisorid);
                    intent.putExtra("advisor_name", advisorname);
                    mContext.startActivity(intent);



                }
            });
            viewHolder.selectableRoundedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ChatActivityforUser.class);
                    intent.putExtra("advisor_id", advisorid);
                    intent.putExtra("advisor_name", advisorname);
                    mContext.startActivity(intent);
                }
            });
        }
        else
        {
            viewHolder.relativeLayout_Clients.setVisibility(View.GONE);
            viewHolder.linearLayout_Advisor.setVisibility(View.VISIBLE);
            String OpponentName = "";
            String Advisorid = "";
            if(UserList.get(position).getSender_id().contains(userSession.getUserId()))
            {
                OpponentName = UserList.get(position).getReciever_display_name();
                Advisorid = UserList.get(position).getReciever_id();
            }
            else
            {
                OpponentName = UserList.get(position).getSender_display_name();
                Advisorid = UserList.get(position).getSender_id();
            }

            final String advisorname = OpponentName;
            final String advisorid = Advisorid;
            viewHolder.textView_from.setText(OpponentName);///change it


            if(UserList.get(position).getText().toString().contains("/~*/")) {
                int index = UserList.get(position).getText().toString().indexOf("/~*/");
                String question = UserList.get(position).getText().toString().substring(0, index);
                viewHolder.textView_message.setText(question);
            }
            else
            {
                viewHolder.textView_message.setText(UserList.get(position).getText().toString());
            }


            String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z" ;
            final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            long timeStamp= 0;
            try {
                timeStamp = sdf.parse(UserList.get(position).getDate()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(timeStamp));
            cal.setTimeZone(TimeZone.getDefault());
            System.out.println(sdf.format(cal.getTime()));

            String steTime = sdf.format(cal.getTime());
            try {
                long et = toEpoch(steTime.substring(0,steTime.length()-5));
                viewHolder.textView_last_text_time.setReferenceTime(et);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String[] strs = steTime.substring(0,steTime.length()-5).split(" ");
            viewHolder.message_center_item_date.setText(strs[0]);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(userSession.getUserType().contains("client")) {
                        Intent intent = new Intent(mContext, ChatActivityforUser.class);
                        intent.putExtra("advisor_id", advisorid);
                        intent.putExtra("advisor_name", advisorname);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, ChatActivityforAdvisor.class);
                        intent.putExtra("advisor_id", advisorid);
                        intent.putExtra("advisor_name", advisorname);
                        mContext.startActivity(intent);
                    }
                }
            });

        }
        return convertView;
    }
    class ViewHolder {
        SelectableRoundedImageView selectableRoundedImageView;
        TextView textView_last_texter_name;
        TextView textView_last_text;
        com.github.curioustechizen.ago.RelativeTimeTextView textView_last_text_time;
        com.github.curioustechizen.ago.RelativeTimeTextView message_center_item_date;
        RelativeLayout relativeLayout_Clients;

        LinearLayout linearLayout_Advisor;
        com.github.curioustechizen.ago.RelativeTimeTextView textView_date;
        TextView textView_complete;
        TextView textView_from;
        TextView textView_dob;
        TextView textView_message;
        TextView textView_videoorder;



    }
    public void SetUserList(ArrayList<Message> userList) {
        this.UserList = userList;
        notifyDataSetChanged();
    }
    public  long toEpoch(String str) throws Exception
    {
        str = str.trim();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(str);
        long epoch = date.getTime();
        System.out.println(epoch); // 1055545912454
        return epoch;
    }
    ///////////////////////////
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<Message>) results.values; // has the filtered values
                SetUserList(mDisplayedValues);  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Message> FilteredArrList = new ArrayList<Message>();

                if (mOriginalValues == null || mOriginalValues.size() == 0) {
                    mOriginalValues = UserList; // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getText();
                        if (data.toLowerCase().contains(constraint.toString()) || mOriginalValues.get(i).getSender_display_name().contains(constraint.toString())) {

                            FilteredArrList.add(mOriginalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }

                return results;
            }
        };
        return filter;
    }
    ////////////////////////////
}
