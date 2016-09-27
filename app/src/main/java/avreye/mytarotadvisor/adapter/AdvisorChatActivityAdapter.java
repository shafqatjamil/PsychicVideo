package avreye.mytarotadvisor.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.shader.BubbleShader;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import avreye.mytarotadvisor.Object.GetMyCreditsResponse;
import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.Object.UpdateMessageStatusReponse;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.helper.ImageLoadinginList;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.MainActivity;
import avreye.mytarotadvisor.ui.VideoPlayerActivity;
import avreye.mytarotadvisor.ui.credentail.ClientLoginActivity;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdvisorChatActivityAdapter extends BaseAdapter {
    ImageLoadinginList imageLoadinginList;
    private ArrayList<Message> UserList;
    private Context mContext;
    Retrofit retrofit;
    DatabaseHelper databaseHelper;
    String myId;
    public AdvisorChatActivityAdapter(Context mContext, ArrayList<Message> userlist, String Advisor_id, String Client_id) {
        this.mContext = mContext;
        this.UserList = userlist;
        imageLoadinginList = new ImageLoadinginList(mContext);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MESSAGE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        databaseHelper = new DatabaseHelper(mContext);
        myId = Advisor_id;

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


            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.chatbubbleadvisor, null);
            viewHolder.textView_text = (TextView) convertView.findViewById(R.id.message_text);
            viewHolder.textView_text_outer = (TextView) convertView.findViewById(R.id.message_text_outer);
            viewHolder.message_text_time = (TextView) convertView.findViewById(R.id.message_text_time);
            viewHolder.textView_order_status = (TextView) convertView.findViewById(R.id.order_status);
            viewHolder.textView_markascompleted = (TextView) convertView.findViewById(R.id.order_status_markascompleted);
            viewHolder.imageView_video = (com.github.siyamed.shapeimageview.mask.PorterShapeImageView) convertView.findViewById(R.id.video_thumbnail);
            viewHolder.parent_layout = (LinearLayout) convertView.findViewById(R.id.bubble_layout_parent);
            viewHolder.user_image_right = (ImageView) convertView.findViewById(R.id.user_image_right);
            viewHolder.user_image_left = (ImageView) convertView.findViewById(R.id.user_image_left);
            viewHolder.video_thumbnail_framlayout = (FrameLayout) convertView.findViewById(R.id.video_thumbnail_framlayout);
            viewHolder.video_thumbnail_play_button = (ImageButton) convertView.findViewById(R.id.video_thumbnail_play_button);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView_markascompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = false;
                for(int i = position+1; i < UserList.size();i++)
                {
                    if(UserList.get(i).getSender_id().contains(myId))
                    {
                        flag = true;
                    }
                }

                if(flag)
                {
                    Log.e("Status" ,UserList.get(position).getMessage_review_id()+"");
                    Log.e("Status" ,UserList.get(position).getSender_id()+"");
                    new AlertDialog.Builder(mContext)
                            .setCancelable(false)
                            .setMessage("Are you sure to mark this order as completed.")
                            .setTitle("Mark Order")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ////////////////////////////

                                    APIService apiservice = retrofit.create(APIService.class);
                                    Call<UpdateMessageStatusReponse> APICall = apiservice.updateMessageStatus(UserList.get(position).getMessage_review_id(), UserList.get(position).getSender_id());
                                    APICall.enqueue(new retrofit2.Callback<UpdateMessageStatusReponse>() {
                                        @Override
                                        public void onResponse(Call<UpdateMessageStatusReponse> call, Response<UpdateMessageStatusReponse> response) {
                                            if (response.body() != null) {

                                                if (response.body().getResult() == 1) {
                                                    Log.e("Status" ,"Changed");
                                                    databaseHelper.UpdateMessageStatus(UserList.get(position).getId());
                                                    viewHolder.textView_markascompleted.setVisibility(View.GONE);
                                                    viewHolder.textView_order_status.setVisibility(View.GONE);
                                                    viewHolder.textView_text.setBackgroundResource(R.drawable.bubble_out);
                                                    UserList.get(position).setStatus(1);

                                                } else {
                                                    Log.e("message history" ,"error not 1");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UpdateMessageStatusReponse> call, Throwable t) {
                                            Log.e("message history" ,"errorfailure");
                                            Log.e("message history" ,t.getMessage());
                                        }
                                    });
                                    /////////////////////////////
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                else
                {
                    Toast.makeText(mContext,"Please complete order first",Toast.LENGTH_SHORT).show();
                }
            }
        });




        if (position % 2 == 0) {

            try {

                viewHolder.message_text_time.setVisibility(View.VISIBLE);
                long et = toEpoch(UserList.get(position).getDate().substring(0, UserList.get(position).getDate().length() - 5));

                SimpleDateFormat mFormatter = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(et));
                viewHolder.message_text_time.setText(mFormatter.format(calendar.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        else
        {

            viewHolder.message_text_time.setVisibility(View.GONE);
        }
        if (UserList.get(position).getSender_id().contains(new UserSession(mContext).getUserId())) {
            //my message

            viewHolder.textView_markascompleted.setVisibility(View.GONE);
            viewHolder.textView_order_status.setVisibility(View.GONE);

            if (UserList.get(position).getType().toString().contains("video")) {

                viewHolder.video_thumbnail_framlayout.setVisibility(View.VISIBLE);
                viewHolder.textView_text_outer.setGravity(Gravity.RIGHT);
                viewHolder.textView_text_outer.setVisibility(View.GONE);
                viewHolder.textView_text.setVisibility(View.GONE);

                Log.e("video name", UserList.get(position).getUrl());
                String str1 = UserList.get(position).getUrl().replace("https://s3.amazonaws.com/studiosbucket/", "");
                String str2 = str1.replace(".mp4", "");
                Picasso.with(mContext)
                        .load(UserSession.getInstance(mContext).getThumbnailUri(str2 + ".png"))
                        .into(viewHolder.imageView_video, new com.squareup.picasso.Callback() {


                            @Override
                            public void onSuccess() {
                                Log.e("imagte loading", "success");
                            }

                            @Override
                            public void onError() {
                                Log.e("imagte loading", "eroor");
                                Picasso.with(mContext).load(R.drawable.videoplaceholder).into(viewHolder.imageView_video);
                                imageLoadinginList.SetThumbnail(viewHolder.imageView_video, UserList.get(position).getUrl());
                            }
                        });
                //  viewHolder.imageView_video.setArrowPosition(BubbleShader.ArrowPosition.RIGHT);
                viewHolder.video_thumbnail_play_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                        intent.putExtra("advisor_video", UserList.get(position).getUrl());
                        mContext.startActivity(intent);
                    }
                });

            }
            else
            {
                viewHolder.textView_text.setText(Html.fromHtml(unescapeJavaString(UserList.get(position).getText())));
                viewHolder.video_thumbnail_framlayout.setVisibility(View.GONE);
                viewHolder.textView_text_outer.setVisibility(View.GONE);
                viewHolder.textView_text.setVisibility(View.VISIBLE);
            }
            //////////////////////////////////////////////////////////////////////////////////////////

            viewHolder.parent_layout.setGravity(Gravity.RIGHT);
            viewHolder.textView_text_outer.setGravity(Gravity.RIGHT);
            viewHolder.textView_text.setGravity(Gravity.RIGHT);
            viewHolder.textView_text.setBackgroundResource(R.drawable.bubble_in);
            viewHolder.user_image_left.setVisibility(View.GONE);
            viewHolder.user_image_right.setVisibility(View.VISIBLE);





        } else {
            //others message

            viewHolder.parent_layout.setGravity(Gravity.LEFT);
            viewHolder.user_image_left.setVisibility(View.VISIBLE);
            viewHolder.user_image_right.setVisibility(View.GONE);




            if (UserList.get(position).getText().toString().contains("/~*/")) {




                viewHolder.textView_order_status.setText("Pending: Requires Video Reply");
                int index = UserList.get(position).getText().toString().indexOf("/~*/");
                String question = UserList.get(position).getText().toString().substring(0, index);
                String situation = UserList.get(position).getText().toString().substring(index + 4);
                String str = question.replace("\\\\", "\\");
/////////
                if (UserList.get(position).getUrl().length() < 5) {
                    viewHolder.video_thumbnail_framlayout.setVisibility(View.GONE);
                    viewHolder.textView_text.setText("Question: " + Html.fromHtml(unescapeJavaString(str)) + "\n" + "Situation: " + Html.fromHtml(unescapeJavaString(situation.replace("\\\\", "\\"))));
                    viewHolder.textView_text.setBackgroundResource(R.drawable.bubble_out);
                    viewHolder.textView_text.setGravity(Gravity.LEFT);
                    viewHolder.textView_text_outer.setVisibility(View.GONE);
                    viewHolder.video_thumbnail_framlayout.setVisibility(View.GONE);
                    viewHolder.textView_text.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.video_thumbnail_framlayout.setVisibility(View.VISIBLE);
                    viewHolder.textView_text_outer.setText("Question: " + Html.fromHtml(unescapeJavaString(str)) + "\n" + "Situation: " + Html.fromHtml(unescapeJavaString(situation.replace("\\\\", "\\"))));
                    viewHolder.textView_text_outer.setGravity(Gravity.LEFT);
                    viewHolder.textView_text_outer.setVisibility(View.VISIBLE);
                    viewHolder.textView_text.setVisibility(View.GONE);


                    String str1 = UserList.get(position).getUrl().replace("https://s3.amazonaws.com/studiosbucket/", "");
                    String str2 = str1.replace(".mp4", "");
                    Picasso.with(mContext)
                            .load(UserSession.getInstance(mContext).getThumbnailUri(str2 + ".png"))
                            .into(viewHolder.imageView_video, new com.squareup.picasso.Callback() {


                                @Override
                                public void onSuccess() {
                                    Log.e("imagte loading", "success");
                                }

                                @Override
                                public void onError() {
                                    Log.e("imagte loading", "eroor");
                                    Picasso.with(mContext).load(R.drawable.videoplaceholder).into(viewHolder.imageView_video);
                                    imageLoadinginList.SetThumbnail(viewHolder.imageView_video, UserList.get(position).getUrl());
                                }
                            });
                    //  viewHolder.imageView_video.setArrowPosition(BubbleShader.ArrowPosition.RIGHT);
                    viewHolder.video_thumbnail_play_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                            intent.putExtra("advisor_video", UserList.get(position).getUrl());
                            mContext.startActivity(intent);
                        }
                    });
                }
                //////



            } else {

                viewHolder.textView_order_status.setText("Pending: Requires Text Reply");
                viewHolder.textView_text.setText(Html.fromHtml(unescapeJavaString(UserList.get(position).getText())));
                viewHolder.video_thumbnail_framlayout.setVisibility(View.GONE);
                viewHolder.textView_text_outer.setVisibility(View.GONE);
                viewHolder.textView_text.setGravity(Gravity.LEFT);
                viewHolder.textView_text.setVisibility(View.VISIBLE);
                viewHolder.textView_text.setBackgroundResource(R.drawable.bubble_out);


            }
            if(UserList.get(position).getStatus() == 0)
            {
                viewHolder.textView_markascompleted.setVisibility(View.VISIBLE);
                viewHolder.textView_order_status.setVisibility(View.VISIBLE);
                viewHolder.textView_text.setBackgroundResource(R.drawable.bubble_out_red);
            }
            else
            {
                viewHolder.textView_markascompleted.setVisibility(View.GONE);
                viewHolder.textView_order_status.setVisibility(View.GONE);
                viewHolder.textView_text.setBackgroundResource(R.drawable.bubble_out);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView textView_text;
        TextView textView_text_outer;
        TextView textView_order_status;
        TextView textView_markascompleted;
        TextView message_text_time;
        com.github.siyamed.shapeimageview.mask.PorterShapeImageView imageView_video;
        LinearLayout parent_layout;
        ImageView user_image_right, user_image_left;
        FrameLayout video_thumbnail_framlayout;
        ImageButton video_thumbnail_play_button;

    }

    public void SetMessagesArrayList(ArrayList<Message> userList) {
        this.UserList = userList;
        notifyDataSetChanged();
    }

    public String unescapeJavaString(String st) {

        StringBuilder sb = new StringBuilder(st.length());

        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == st.length() - 1) ? '\\' : st
                        .charAt(i + 1);
                // Octal escape?
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code += st.charAt(i + 1);
                        i++;
                        if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code += st.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code, 8));
                    continue;
                }
                switch (nextChar) {
                    case '\\':
                        ch = '\\';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    // Hex Unicode: u????
                    case 'u':
                        if (i >= st.length() - 5) {
                            ch = 'u';
                            break;
                        }
                        int code = Integer.parseInt(
                                "" + st.charAt(i + 2) + st.charAt(i + 3)
                                        + st.charAt(i + 4) + st.charAt(i + 5), 16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    public long toEpoch(String str) throws Exception {
        str = str.trim();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(str);
        long epoch = date.getTime();
        System.out.println(epoch); // 1055545912454
        return epoch;
    }


}