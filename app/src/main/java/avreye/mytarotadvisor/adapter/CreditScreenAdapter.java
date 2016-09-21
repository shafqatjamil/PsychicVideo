package avreye.mytarotadvisor.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import avreye.mytarotadvisor.Object.InAppPurchases;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.UserSession;


/**
 * Created by null on 4/21/2016.
 */
public class CreditScreenAdapter extends BaseAdapter {

    ArrayList<InAppPurchases.Message> inapps;
    Context mContext;
    UserSession userSession;

    public CreditScreenAdapter(Context mContext, ArrayList<InAppPurchases.Message> inapps) {
        this.mContext = mContext;
        this.inapps = inapps;
        userSession = new UserSession(mContext);
    }

    @Override
    public int getCount() {
        return inapps.size();
    }

    @Override
    public Object getItem(int position) {
        return inapps.get(position);
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

            convertView = mInflater.inflate(R.layout.credit_item, null);
            viewHolder = new ViewHolder();

            viewHolder.textView_credits = (TextView) convertView.findViewById(R.id.credits);
            viewHolder.textView_description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.button_purchase = (Button) convertView.findViewById(R.id.purchase);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

            viewHolder.textView_credits.setText(inapps.get(position).getCredits());///change it

            viewHolder.textView_description.setText(Html.fromHtml(unescapeJavaString(inapps.get(position).getDescription())));
            viewHolder.button_purchase.setText("$"+inapps.get(position).getPrice());

            viewHolder.button_purchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*
                    Intent intent = new Intent(mContext, ChatActivityforUser.class);
                    intent.putExtra("advisor_id", advisorid);
                    intent.putExtra("advisor_name", advisorname);
                    mContext.startActivity(intent);
*/



                }
            });






        return convertView;
    }

    class ViewHolder {
        TextView textView_credits;
        TextView textView_description;
        Button button_purchase;


    }

    public void SetUserList(ArrayList<InAppPurchases.Message> inapplist) {
        this.inapps = inapplist;
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
