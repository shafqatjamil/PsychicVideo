package avreye.mytarotadvisor.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.MessageCenterAdapter;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.helper.UserSession;


public class MessageCenter extends Fragment {

    MessageCenterAdapter messageCenterAdapter;
    ListView messageList;
    private View rootView;
    DatabaseHelper databaseHelper;
    UserSession userSession;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message_center, null, false);

        return rootView;

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userSession = new UserSession(getActivity());
        EditText searchView = (EditText) rootView.findViewById(R.id.message_center_searchview);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
       /// TextView textView = (TextView) searchView.findViewById(id);
      //  textView.setTextColor(Color.BLUE);






        messageList = (ListView) rootView.findViewById(R.id.message_center_list);


        databaseHelper = new DatabaseHelper(getActivity());
        messageCenterAdapter = new MessageCenterAdapter(getActivity(), new ArrayList<Message>());
        assert messageList != null;
        messageList.setAdapter(messageCenterAdapter);

        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                messageCenterAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (userSession.getUserType().contains("client"))
            messageCenterAdapter.SetUserList(databaseHelper.GetChatList(Integer.parseInt(userSession.getUserId())));
        else
            messageCenterAdapter.SetUserList(databaseHelper.GetChatListforAdvisor(Integer.parseInt(userSession.getUserId())));
    }

    public void receiveMessageFromMainScreen() {
        if (userSession.getUserType().contains("client"))
            messageCenterAdapter.SetUserList(databaseHelper.GetChatList(Integer.parseInt(userSession.getUserId())));
        else
            messageCenterAdapter.SetUserList(databaseHelper.GetChatListforAdvisor(Integer.parseInt(userSession.getUserId())));
    }

}
