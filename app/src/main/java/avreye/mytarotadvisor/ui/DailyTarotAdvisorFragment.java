package avreye.mytarotadvisor.ui;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.appboy.Appboy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import avreye.mytarotadvisor.Object.GetTarotResponse;
import avreye.mytarotadvisor.R;

import avreye.mytarotadvisor.AppController;
import avreye.mytarotadvisor.helper.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyTarotAdvisorFragment extends Fragment {

    TextView textView_date;
    AppController appController;
    ImageView imageView_thumbnail;
    VideoView videoView_video;
    ImageView button_playButton;
    Retrofit retrofit;
    String VideoURL = "";

    private View rootView;
    public DailyTarotAdvisorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_daily_tarot_advisor, container, false);
        return  rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        videoView_video = (VideoView)rootView.findViewById(R.id.video_view) ;
        imageView_thumbnail = (ImageView) rootView.findViewById(R.id.daily_tarot_video_thumbnail) ;
        button_playButton = (ImageView) rootView.findViewById(R.id.daily_tarot_video_play_button) ;
        textView_date = (TextView) rootView.findViewById(R.id.dailytarot_date);
        String date = new SimpleDateFormat("MMM dd, yyyy").format(new Date());
        textView_date.setText(date);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://mytarotadvisorapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HitAPI();
        button_playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VideoURL.isEmpty())
                    return;
                button_playButton.setVisibility(View.GONE);
                imageView_thumbnail.setVisibility(View.GONE);
                Uri uri=Uri.parse(VideoURL);

                VideoView video=(VideoView)rootView.findViewById(R.id.video_view);
                video.setVideoURI(uri);
                video.start();

                Appboy.getInstance(getActivity()).logCustomEvent("TarotOfDay_Viewed");
            }
        });



    }
    void HitAPI() {
        APIService apiservice = retrofit.create(APIService.class);
        Call<GetTarotResponse> APICall = apiservice.getTarot();
        APICall.enqueue(new Callback<GetTarotResponse>() {

            @Override
            public void onResponse(Call<GetTarotResponse> call, Response<GetTarotResponse> response) {

                if (response.body().getError() == false) {

                    if(response.isSuccessful()) {

                        Picasso.with(getActivity()).load(response.body().getPoster()).into(imageView_thumbnail);
                        VideoURL = response.body().getVideoUrl();

                    }
                    else
                    {
                        Toast.makeText(getActivity(), "invalid username or password",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetTarotResponse> call, Throwable t) {

            }

        });
    }
}
