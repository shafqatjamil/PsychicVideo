package avreye.mytarotadvisor.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import avreye.mytarotadvisor.Object.GetAdvisorStatusReponse;
import avreye.mytarotadvisor.Object.LoginResponse;
import avreye.mytarotadvisor.Object.UpdateStatusResponse;
import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.AdvisorListAdapter;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.credentail.ClientLoginActivity;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AdvisorAvailibilityFragment extends Fragment {

    Context context;
    Retrofit retrofit;
    Switch aSwitch;
    TextView textView_status;
    private View rootView;
    ProgressDialog progressDialog;

    public AdvisorAvailibilityFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_advisor_availibility, container, false);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView_status = (TextView) rootView.findViewById(R.id.status_text);
        context = getActivity();
        aSwitch = (Switch) rootView.findViewById(R.id.switch1);
        if (new UserSession(getActivity()).getADVISOR_STATUS() == "1") {

            textView_status.setText("ACTIVE");
            textView_status.setTextColor(Color.parseColor("#FF00EEFF"));
            aSwitch.setChecked(true);
        } else {

            textView_status.setTextColor(Color.parseColor("#FFABA7A7"));
            textView_status.setText("INACTIVE");
            aSwitch.setChecked(false);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView_status.setText("ACTIVE");
                    textView_status.setTextColor(Color.parseColor("#FF00EEFF"));
                    new UserSession(getActivity()).setADVISOR_STATUS("1");
                    HitAPI("1");
                } else {
                    textView_status.setTextColor(Color.parseColor("#FFABA7A7"));
                    textView_status.setText("INACTIVE");
                    new UserSession(getActivity()).setADVISOR_STATUS("0");
                    HitAPI("0");
                }
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Advisor_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HitAPI_getStatus();

    }

    boolean HitAPI_getStatus() {

        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("wait...");
        progressDialog.show();
        APIService apiservice = retrofit.create(APIService.class);
        Call<GetAdvisorStatusReponse> APICall = apiservice.getStatus(new UserSession(context).getUserId());
        APICall.enqueue(new Callback<GetAdvisorStatusReponse>() {
            @Override
            public void onResponse(Call<GetAdvisorStatusReponse> call, Response<GetAdvisorStatusReponse> response) {

                if (response.body() != null) {
                    if (response.body().getResult() == 1) {

                        textView_status.setText("ACTIVE");
                        textView_status.setTextColor(Color.parseColor("#FF00EEFF"));
                        aSwitch.setChecked(true);
                    } else {
                        textView_status.setTextColor(Color.parseColor("#FFABA7A7"));
                        textView_status.setText("INACTIVE");
                        aSwitch.setChecked(false);
                    }

                    progressDialog.dismiss();

                } else {
                }
            }
            @Override
            public void onFailure(Call<GetAdvisorStatusReponse> call, Throwable t) {

            }


        });
        return true;
    }

    void HitAPI(String status) {
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("wait...");
        progressDialog.show();
        APIService apiservice = retrofit.create(APIService.class);
        Call<UpdateStatusResponse> APICall = apiservice.UpdateStatus(new UserSession(context).getUserId(), status);
        APICall.enqueue(new Callback<UpdateStatusResponse>() {
            @Override
            public void onResponse(Call<UpdateStatusResponse> call, Response<UpdateStatusResponse> response) {

                if (response.body() != null) {


                } else {
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateStatusResponse> call, Throwable t) {

            }


        });
    }
}
