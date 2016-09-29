package avreye.mytarotadvisor.ui.credentail;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appboy.Appboy;
import com.appboy.enums.Month;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import avreye.mytarotadvisor.Object.RegistrationResponse;
import avreye.mytarotadvisor.Object.SignUpBonusResponse;
import avreye.mytarotadvisor.PubnubService;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.CustomLayout;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.MainActivity;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterationActivity extends AppCompatActivity {

    EditText  editText_name;
    EditText  editText_dob;
    EditText  editText_email;
    EditText  editText_password;
    TextView textView_login;
    Button button_register;
    String tag = "psychic video";
    Retrofit retrofit;
    Retrofit retrofit1;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registeration);

        CustomLayout mCustomLayout = (CustomLayout)findViewById(R.id.registration_layout);
        Picasso.with(this).load(R.drawable.bg).into(mCustomLayout);
        editText_name = (EditText)findViewById(R.id.et_name);
        editText_dob = (EditText)findViewById(R.id.et_dob);
        editText_email = (EditText)findViewById(R.id.et_email);
        editText_password = (EditText)findViewById(R.id.et_password);
        textView_login = (TextView)findViewById(R.id.regidtration_tv_signin);
        button_register = (Button)findViewById(R.id.button_register);
        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),ClientLoginActivity.class);
                startActivity(intent);
            }
        });
        editText_dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    setDate(editText_dob);
                }
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(RegisterationActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                if(validate()) {
                    HitAPI();
                }
                else
                {
                    progressDialog.dismiss();
                }
            }
        });
        ////API call Code///////////////////////
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CLIENT_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit1 = new Retrofit.Builder()
                .baseUrl(Constants.SIGNUPBONUSURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ////////////////////API Call Code Ended here////////////
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    void HitAPI() {

        String tarot_id = Settings.Secure.getString(RegisterationActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        APIService apiservice = retrofit.create(APIService.class);
        Call<RegistrationResponse> APICall = apiservice.RegisterUser(editText_name.getText().toString()
                                                        ,editText_password.getText().toString()
                                                        ,editText_email.getText().toString()
                                                        ,"101", Constants.DEFAULT_TOTAL_CREDITS,Constants.TOTAL_ORDERS,tarot_id,editText_dob.getText().toString());// set it

        APICall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {


                if (response.body() != null) {
                    if(response.body().getResult() == 1)
                    {

                        UserSession mUserSession = new UserSession(RegisterationActivity.this);
                        mUserSession.setIsLoggedIn(true);
                        mUserSession.setUserId(""+response.body().getId());
                        mUserSession.setUserEmail(editText_email.getText().toString());
                        mUserSession.setUserDOB(editText_dob.getText().toString());
                        mUserSession.setUserName(editText_name.getText().toString());
                        mUserSession.setUserOrders(Constants.TOTAL_ORDERS);
                        mUserSession.setUserType("client");
                        mUserSession.setUserChannelName("Channel_User_" + response.body().getId());
                        getSignUpBonus();

                        String[] dobb = editText_dob.getText().toString().split("-");

                        Log.e("UserID",response.body().getId().toString());

                        Appboy.getInstance(RegisterationActivity.this).changeUser(""+response.body().getId());
                        Appboy.getInstance(RegisterationActivity.this).getCurrentUser().setDateOfBirth(Integer.parseInt(dobb[0]), Month.APRIL,Integer.parseInt(dobb[2]));
                        Appboy.getInstance(RegisterationActivity.this).getCurrentUser().setFirstName(editText_name.getText().toString());
                        Appboy.getInstance(RegisterationActivity.this).getCurrentUser().setEmail(editText_email.getText().toString());
                        Appboy.getInstance(RegisterationActivity.this).getCurrentUser().setCustomUserAttribute("username",editText_name.getText().toString());
                        Appboy.getInstance(RegisterationActivity.this).logCustomEvent("Registration");



                    }
                    else if(response.body().getResult() == 0)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    else if(response.body().getResult() == -1)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"There is already an account registered with this email address, please sign in using your password",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
             //   Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();
            }
        });
    }


    void getSignUpBonus()
    {
        APIService apiservice = retrofit1.create(APIService.class);
        Call<SignUpBonusResponse> APICall = apiservice.getSignUpBonus();// set it

        APICall.enqueue(new Callback<SignUpBonusResponse>() {
            @Override
            public void onResponse(Call<SignUpBonusResponse> call, Response<SignUpBonusResponse> response) {

                progressDialog.dismiss();
                if (response.body() != null) {
                    if(response.body().getResult() == 1)
                    {


                        int temp = 0;
                        for(int i = 0; i < response.body().getMessage().size(); i++)
                        {
                            temp += Integer.parseInt(response.body().getMessage().get(i).getBonus());
                        }
                        new UserSession(RegisterationActivity.this).setUserCredits(temp+"");

                        Intent serviceIntent = new Intent(RegisterationActivity.this, PubnubService.class);
                        startService(serviceIntent);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        RegisterationActivity.this.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpBonusResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                //   Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();
            }
        });
    }



    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(RegisterationActivity.this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };
    private void showDate(int year, int month, int day) {



        String imageName = "%02d";
        editText_dob.setText(new StringBuilder().append(year).append("-")
                .append(String.format( imageName, month)).append("-").append(String.format( imageName, day)));


    }

    public boolean validate() {
        boolean valid = true;

        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();
        String userName = editText_name.getText().toString();
        String userDob = editText_dob.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText_email.setError("enter a valid email address");
            valid = false;
        } else {
            editText_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            editText_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            editText_password.setError(null);
        }

        if (userName.isEmpty() || userName.length() < 4 || userName.length() > 50) {
            editText_name.setError("enter valid user name");
            valid = false;
        } else {
            editText_name.setError(null);
        }
        String[] strs = userDob.split("-");
        if (getAge(Integer.parseInt(strs[0]),Integer.parseInt(strs[1]),Integer.parseInt(strs[2])) < 18) {
            editText_dob.setError("Adults Only.Restricted to age 18 and above.");
            valid = false;
        } else {
            editText_dob.setError(null);
        }
        return valid;
    }
    public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }

        return a;
    }
}
