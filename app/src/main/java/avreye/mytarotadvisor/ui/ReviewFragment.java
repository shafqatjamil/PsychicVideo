package avreye.mytarotadvisor.ui;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import avreye.mytarotadvisor.Object.AdvisorInfo;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.AdvisorListAdapter;
import avreye.mytarotadvisor.helper.UserSession;
/**
 * Created by null on 4/26/2016.
 */
public class ReviewFragment extends Fragment {
    private ProgressDialog progressDialog;
    ArrayList<AdvisorInfo> UserList;
    AdvisorListAdapter userListAdapter;
    private SwipeRefreshLayout swipeContainer;
    private View rootView;
    UserSession userSession;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rating_popup, null, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        UserList = new ArrayList<>();
        userListAdapter = new AdvisorListAdapter(getActivity(), UserList);
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