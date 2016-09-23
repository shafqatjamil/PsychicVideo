package avreye.mytarotadvisor.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.ui.AdvisorDetailActivity;
import avreye.mytarotadvisor.Object.AdvisorInfo;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.utils.Constants;
public class AdvisorListAdapter extends BaseAdapter {

    ArrayList<AdvisorInfo> UserList;
    Context mContext;

    public AdvisorListAdapter(Context mContext, ArrayList<AdvisorInfo> userlist) {
        this.mContext = mContext;
        this.UserList = userlist;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.video_item, null);
            viewHolder = new ViewHolder();
            viewHolder.selectableRoundedImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.advisor_image);
            viewHolder.ratingBar = (com.iarcuschin.simpleratingbar.SimpleRatingBar) convertView.findViewById(R.id.ratingbar);
            viewHolder.textViewDescription = (TextView) convertView.findViewById(R.id.tvoutertext);
            viewHolder.linearLayout_offlineoverlay = (ImageView) convertView.findViewById(R.id.oflline_overlay);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.tvusername);
            viewHolder.userStatusImg = (ImageView) convertView.findViewById(R.id.vi_verified_status);

            viewHolder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            viewHolder.userName.setText(UserList.get(position).getUsername());
           // viewHolder.userName.setTypeface(AppController.getInstance().GetMyriad());
            viewHolder.ratingBar.setRating(3.6f);//UserList.get(position).getRating());

        if (Integer.parseInt(UserList.get(position).getStatus()) == 1){
            viewHolder.userStatusImg.setImageResource(R.drawable.blue);
            viewHolder.linearLayout_offlineoverlay.setVisibility(View.GONE);
        }else{
            viewHolder.userStatusImg.setImageResource(R.drawable.orange);
            viewHolder.linearLayout_offlineoverlay.setVisibility(View.VISIBLE);
        }


        String str = UserList.get(position).getShortDescription().replace("\\\\" , "\\");
        viewHolder.textViewDescription.setText(Html.fromHtml(unescapeJavaString(str)));
        String imageUri = Constants.Advisor_IMAGE_URL +UserList.get(position).getDisplayPicture();
        Picasso.with(mContext).load(imageUri).into(viewHolder.selectableRoundedImageView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, UserList.get(position).getFirstName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, AdvisorDetailActivity.class);
                intent.putExtra("advisor_id", UserList.get(position).getId());
                intent.putExtra("advisor_name", UserList.get(position).getUsername());
                intent.putExtra("advisor_status", UserList.get(position).getStatus());
                intent.putExtra("advisor_instruction", UserList.get(position).getInstructions());
                intent.putExtra("advisor_description", UserList.get(position).getShortDescription());
                intent.putExtra("advisor_picture", UserList.get(position).getDisplayPicture());

                mContext.startActivity(intent);
            }
        });
        viewHolder.selectableRoundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, UserList.get(position).getFirstName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, AdvisorDetailActivity.class);
                intent.putExtra("advisor_id", UserList.get(position).getId());
                intent.putExtra("advisor_name", UserList.get(position).getUsername());
                intent.putExtra("advisor_status", UserList.get(position).getStatus());
                intent.putExtra("advisor_instruction", UserList.get(position).getInstructions());
                intent.putExtra("advisor_description", UserList.get(position).getShortDescription());
                intent.putExtra("advisor_picture", UserList.get(position).getDisplayPicture());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder {
        SelectableRoundedImageView selectableRoundedImageView;
        TextView textViewDescription;
        TextView userName;
        com.iarcuschin.simpleratingbar.SimpleRatingBar ratingBar;
        ImageView userStatusImg;
        ImageView linearLayout_offlineoverlay;
    }
    public void SetUserList(ArrayList<AdvisorInfo> userList) {
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


}
