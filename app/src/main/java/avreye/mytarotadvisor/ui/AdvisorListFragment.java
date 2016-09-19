package avreye.mytarotadvisor.ui;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.Object.AdvisorInfo;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.AdvisorListAdapter;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.utils.Constants;
import avreye.mytarotadvisor.utils.ReadWriteJsonFileUtils;

/**
 * Created by null on 4/26/2016.
 */
public class AdvisorListFragment extends Fragment {

    private ProgressDialog progressDialog;
    ArrayList<AdvisorInfo> UserList;
    AdvisorListAdapter userListAdapter;
    private SwipeRefreshLayout swipeContainer;
    private View rootView;
    UserSession userSession;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.client_home_activity, null, false);



        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userSession = new UserSession(getActivity());
        GridView gridView = (GridView) rootView.findViewById(R.id.userlist);
        progressDialog = new ProgressDialog(getActivity());
        UserList = new ArrayList<>();
        userListAdapter = new AdvisorListAdapter(getActivity(), UserList);
        assert gridView != null;
        gridView.setAdapter(userListAdapter);

     //   getAdvisorListOffline();
        getAdviserList(false);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getAdviserList(true);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    void getAdvisorListOffline()
    {


        String response = new ReadWriteJsonFileUtils(getActivity()).readJsonFileData("advisorlist");

        if(response == null)
            return;
        try {
            UserList.clear();
            JSONObject jObj = new JSONObject(response);
            jObj = jObj.getJSONObject("message");
            JSONArray jsonArray = jObj.names();
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jObj.getJSONObject(jsonArray.getString(index));
                AdvisorInfo userInfo = new Gson().fromJson(jsonObject.toString(), AdvisorInfo.class);
                if (userInfo.getReviews().size() > 0) {
                    int totalReview = 0;
                    for (int i = 0; i < userInfo.getReviews().size(); i++) {
                        totalReview += Integer.parseInt(userInfo.getReviews().get(i).getReview());
                    }
                    userInfo.setRating(totalReview / userInfo.getReviews().size());
                } else {
                    userInfo.setRating(0.0f);
                }

                Log.e("USerProfile",userInfo.getId() + Constants.Advisor_IMAGE_URL + userInfo.getDisplayPicture() );
                UserList.add(userInfo);
                userSession.setUserProfilePictureUrl(userInfo.getId(),Constants.Advisor_IMAGE_URL + userInfo.getDisplayPicture());
                Log.e("USerProfile",userInfo.getId() + Constants.Advisor_IMAGE_URL + userInfo.getDisplayPicture() );
            }

            userListAdapter.SetUserList(UserList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    private void getAdviserList(final boolean pultorefresh) {

        progressDialog.setMessage("Please wait ...");
        if (!pultorefresh) {
            showDialog();
        }
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.Advisor_API_BASE_URL + "getAdvisor", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    UserList.clear();
                    JSONObject jObj = new JSONObject(response);
                    jObj = jObj.getJSONObject("message");
                    JSONArray jsonArray = jObj.names();
                    for (int index =  0; index < jsonArray.length(); index++) {
                        JSONObject jsonObject = jObj.getJSONObject(jsonArray.getString(index));
                        AdvisorInfo userInfo = new Gson().fromJson(jsonObject.toString(), AdvisorInfo.class);
                        if (userInfo.getReviews().size() > 0) {
                            int totalReview = 0;
                            for (int i = 0; i < userInfo.getReviews().size(); i++) {
                                totalReview += Integer.parseInt(userInfo.getReviews().get(i).getReview());
                            }
                            userInfo.setRating(totalReview / userInfo.getReviews().size());
                        } else {
                            userInfo.setRating(0.0f);
                        }
                        if(userInfo.getDeactivated().contains("0"))
                            UserList.add(userInfo);

                        userSession.setUserProfilePictureUrl(userInfo.getId(),Constants.Advisor_IMAGE_URL + userInfo.getDisplayPicture());

                    }



                    ArrayList<AdvisorInfo> ActiveUsers = new ArrayList<AdvisorInfo>();
                    ArrayList<AdvisorInfo> InactiveUsers = new ArrayList<AdvisorInfo>();
                    for(int i = 0; i < UserList.size(); i++)
                    {
                        if(UserList.get(i).getStatus().contains("0"))
                        {
                           InactiveUsers.add(UserList.get(i));
                        }
                        else
                        {
                            ActiveUsers.add(UserList.get(i));
                        }
                    }

                    Collections.sort(ActiveUsers, new Comparator<AdvisorInfo>() {
                        @Override
                        public int compare(AdvisorInfo z1, AdvisorInfo z2) {
                            if (Integer.parseInt(z1.getDisplayorder()) > Integer.parseInt(z2.getDisplayorder()))
                                return 1;
                            if (Integer.parseInt(z1.getDisplayorder()) < Integer.parseInt(z2.getDisplayorder()))
                                return -1;
                            return 0;
                        }
                    });
                    Collections.sort(InactiveUsers, new Comparator<AdvisorInfo>() {
                        @Override
                        public int compare(AdvisorInfo z1, AdvisorInfo z2) {
                            if (Integer.parseInt(z1.getDisplayorder()) > Integer.parseInt(z2.getDisplayorder()))
                                return 1;
                            if (Integer.parseInt(z1.getDisplayorder()) < Integer.parseInt(z2.getDisplayorder()))
                                return -1;
                            return 0;
                        }
                    });



                    UserList.clear();
                    UserList = ActiveUsers;
                    for(int i = 0; i < InactiveUsers.size(); i++)
                        UserList.add(InactiveUsers.get(i));



                    if (pultorefresh) {
                        swipeContainer.setRefreshing(false);
                    } else {
                        hideDialog();
                    }
                    userListAdapter.SetUserList(UserList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                try {
//                    new ReadWriteJsonFileUtils(getActivity()).deleteFile("advisorlist");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                    new ReadWriteJsonFileUtils(getActivity()).createJsonFileData("advisorlist", response);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (pultorefresh) {
                    swipeContainer.setRefreshing(false);
                } else {
                    hideDialog();
                }


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();

//             params.put("tag", "login");
                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, "adviser");
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
