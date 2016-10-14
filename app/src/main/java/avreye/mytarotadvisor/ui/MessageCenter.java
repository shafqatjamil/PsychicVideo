package avreye.mytarotadvisor.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.MessageCenterAdapter;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MessageCenter extends Fragment {

    MessageCenterAdapter messageCenterAdapter;
    ListView messageList;
    private View rootView;
    DatabaseHelper databaseHelper;
    UserSession userSession;
    private Retrofit retrofit;

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

        databaseHelper = new DatabaseHelper(getActivity());
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MESSAGE_API_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getLatestMessages(new UserSession(getActivity()).getUserId(),new UserSession(getActivity()).getUserType(),databaseHelper.getLatestMessageId());



        messageList = (ListView) rootView.findViewById(R.id.message_center_list);


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
    void getLatestMessages(final String id, final String type, final String msg_id) {
        Log.e("test" , id + "  " + type);
        APIService apiservice = retrofit.create(APIService.class);
        Call<MessageHistoryResponse> APICall = apiservice.getHistoryFromid(id, type, msg_id);
        APICall.enqueue(new Callback<MessageHistoryResponse>() {
            @Override
            public void onResponse(Call<MessageHistoryResponse> call, Response<MessageHistoryResponse> response) {

                if (response.body() != null) {

                    if (response.body().getResult() == 1) {
                        ArrayList<MessageHistoryResponse.Message> Hmessages = response.body().getMessage();
                        databaseHelper.FlushDatabase();
                        for(int i = 0; i < Hmessages.size(); i++)
                        {
                            databaseHelper.insertMessageToDB((new Message(
                                    Integer.parseInt(Hmessages.get(i).getId()),
                                    Hmessages.get(i).getSenderId(),
                                    Hmessages.get(i).getSenderDisplayname(),
                                    Hmessages.get(i).getReceiverId(),
                                    Hmessages.get(i).getReceiverDisplayname(),
                                    Hmessages.get(i).getMsgText(),
                                    Hmessages.get(i).getMsgDate(),
                                    Integer.parseInt(Hmessages.get(i).getStatus()),
                                    Hmessages.get(i).getVideoUrl(),
                                    Hmessages.get(i).getMessageType(),
                                    Hmessages.get(i).getSenderType(),
                                    Hmessages.get(i).getReceiverType(),
                                    Hmessages.get(i).getReviewStatus(),
                                    Hmessages.get(i).getMessageReviewId(),
                                    ""+( Integer.parseInt(Hmessages.get(i).getReceiverId()) + Integer.parseInt(Hmessages.get(i).getSenderId())),
                                    Hmessages.get(i).getClientDob()
                            )));


                        }
                        if (userSession.getUserType().contains("client"))
                            messageCenterAdapter.SetUserList(databaseHelper.GetChatList(Integer.parseInt(userSession.getUserId())));
                        else
                            messageCenterAdapter.SetUserList(databaseHelper.GetChatListforAdvisor(Integer.parseInt(userSession.getUserId())));

                    } else {
                        Log.e("message history" ,"error");
                    }

                }
            }

            @Override
            public void onFailure(Call<MessageHistoryResponse> call, Throwable t) {
            }
        });
    }

}
