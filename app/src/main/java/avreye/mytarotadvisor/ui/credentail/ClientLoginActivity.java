package avreye.mytarotadvisor.ui.credentail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appboy.Appboy;
import com.pubnub.api.Pubnub;

import java.io.IOException;
import java.util.ArrayList;

import avreye.mytarotadvisor.Object.GetAdvisorStatusReponse;
import avreye.mytarotadvisor.Object.LoginResponse;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.DatabaseHelper;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.MainActivity;
import avreye.mytarotadvisor.ui.SplashActivity;
import avreye.mytarotadvisor.utils.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by null on 4/22/2016.
 */
public class ClientLoginActivity extends AppCompatActivity {
    Pubnub pubnub;
    EditText editText_name;
    EditText editText_password;
    Button button_login;
    TextView _signupLink;
    String tag = "psychic video";
    Retrofit retrofit;
    private UserSession mUserSession;
    String _URL;
    ProgressDialog progressDialog;
    TextView textView_forgot_password;
    Retrofit retrofitMessage;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.client_login);
        mUserSession = new UserSession(this);
        editText_name = (EditText) findViewById(R.id.login_et_name);
        editText_password = (EditText) findViewById(R.id.login_et_password);
        _signupLink = (TextView) findViewById(R.id.link_signup);
        textView_forgot_password = (TextView) findViewById(R.id.link_forgot_password);

        button_login = (Button) findViewById(R.id.login_button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ClientLoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                if (validate()) {
                    HitAPI();
                } else {
                    progressDialog.dismiss();
                }
            }
        });


        retrofitMessage = new Retrofit.Builder()
                .baseUrl(Constants.MESSAGE_API_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        ////API call Code///////////////////////


//        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(
//                        new Interceptor() {
//                            @Override
//                            public okhttp3.Response intercept(Chain chain) throws IOException {
//                                Request request = chain.request().newBuilder()
//                                        .addHeader("Authorization", "Basic username:password" ).build();
//                                return chain.proceed(request);
//                            }
//                        }).build();


        _URL = Constants.CLIENT_API_BASE_URL;
        retrofit = new Retrofit.Builder()
                .baseUrl(_URL)
                //  .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ////////////////////API Call Code Ended here////////////


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterationActivity.class);
                startActivity(intent);
            }
        });
        textView_forgot_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /////////////



                AlertDialog.Builder alert = new AlertDialog.Builder(ClientLoginActivity.this);

                final EditText edittext = new EditText(ClientLoginActivity.this);
                alert.setMessage("Please enter the email adress you used to register with the application.");
                alert.setTitle("Forgot Password");

                alert.setView(edittext);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        //OR
                        String YouEditTextValue = edittext.getText().toString();
                        if (!YouEditTextValue.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(YouEditTextValue).matches()) {
                            {
                                progressDialog = new ProgressDialog(ClientLoginActivity.this,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Sending Email...");
                                progressDialog.show();
                                ForgotPassword(YouEditTextValue);
                            }

                        }
                        else
                        {

                            Toast.makeText(getApplicationContext(), "invalid email", Toast.LENGTH_LONG).show();
                        }


                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });
                alert.show();





                //////////////////////////








            }
        });


    }


    void ForgotPassword(String email) {
        APIService apiservice = retrofit.create(APIService.class);
        Call<GetAdvisorStatusReponse> APICall = apiservice.ForgotPassword(email);
        APICall.enqueue(new Callback<GetAdvisorStatusReponse>() {
            @Override
            public void onResponse(Call<GetAdvisorStatusReponse> call, Response<GetAdvisorStatusReponse> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    if (response.body().getResult() == 1) {

                        Toast.makeText(getApplicationContext(), "email has been sent", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "invalid email", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAdvisorStatusReponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void HitAPI() {
        APIService apiservice = retrofit.create(APIService.class);
        Call<LoginResponse> APICall = apiservice.LoginUser(editText_name.getText().toString()
                , editText_password.getText().toString());
        Log.e("AVR", editText_name.getText().toString() + " " + editText_password.getText().toString());
        APICall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body() != null) {
                    // LoginResponse userInfo = new Gson().fromJson(String.valueOf(response.body()), LoginResponse.class);

                    progressDialog.dismiss();
                    if (response.body().getResult() == 1) {

                        mUserSession.setIsLoggedIn(true);
                        mUserSession.setUserId(response.body().getUser().getId());
                        mUserSession.setUserEmail(response.body().getUser().getEmail());
                        mUserSession.setUserDOB(response.body().getUser().getDob());
                        mUserSession.setUserName(response.body().getUser().getUsername());
                        mUserSession.setUserCredits(response.body().getUser().getTotalCredit());
                        mUserSession.setUserOrders(response.body().getUser().getTotalOrders());
                        mUserSession.setUserType(response.body().getType());

                        if (response.body().getType().contains("client")) {

                            Appboy.getInstance(ClientLoginActivity.this).changeUser(""+response.body().getUser().getId());

                            mUserSession.setUserChannelName("Channel_User_" + response.body().getUser().getId());

                            HitMessageAPI(response.body().getUser().getId(),response.body().getType());

                        } else {
                            mUserSession.setUserChannelName("Channel_Advisor_" + response.body().getUser().getId());
                            mUserSession.setADVISOR_STATUS(response.body().getUser().getStatus());

                            HitMessageAPI(response.body().getUser().getId(),response.body().getType());
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String email = editText_name.getText().toString();
        String password = editText_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText_name.setError("enter a valid email address");
            valid = false;
        } else {
            editText_name.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            editText_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            editText_password.setError(null);
        }

        return valid;
    }

    void HitMessageAPI(final String id, final String type) {
        APIService apiservice = retrofitMessage.create(APIService.class);
        Log.e("test" , id + "  " + type);
        Call<MessageHistoryResponse> APICall = apiservice.getHistory(id, type);
        Log.e("test" , id + "  " + type);
        APICall.enqueue(new Callback<MessageHistoryResponse>() {
            @Override
            public void onResponse(Call<MessageHistoryResponse> call, Response<MessageHistoryResponse> response) {

                if (response.body() != null) {

                    Log.e("test" , id + "  " + type);

                    if (response.body().getResult() == 1) {

                        Log.e("test" , id + "  " + type);
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
                                    ""+( Integer.parseInt(Hmessages.get(i).getReceiverId()) + Integer.parseInt(Hmessages.get(i).getSenderId()))
                            )));


                        }
                    } else {
                        Log.e("message history" ,"error");
                    }

                //    ArrayList<Message> test  = databaseHelper.getAllMessages(new UserSession(ClientLoginActivity.this).getUserId());

                    Intent serviceIntent = new Intent(ClientLoginActivity.this, PubnubService.class);
                    startService(serviceIntent);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    ClientLoginActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<MessageHistoryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
