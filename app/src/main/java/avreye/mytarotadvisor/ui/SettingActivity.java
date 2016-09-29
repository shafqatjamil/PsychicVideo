package avreye.mytarotadvisor.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appboy.Appboy;
import com.appboy.enums.Month;

import java.util.Calendar;

import avreye.mytarotadvisor.Object.RegistrationResponse;
import avreye.mytarotadvisor.Object.UpdateClientResponse;
import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.helper.APIService;
import avreye.mytarotadvisor.helper.UserSession;
import avreye.mytarotadvisor.ui.credentail.RegisterationActivity;
import avreye.mytarotadvisor.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingActivity extends AppCompatActivity {
    TextView textView_title;
    private TextView headerTitle;
    private TextView MyCredits;
    private Toolbar toolbar;
    ImageButton imageButton_back;
    EditText editText_name;
    EditText  editText_dob;
    EditText  editText_email;
    EditText  editText_password;
    Button button_register;
    ProgressDialog progressDialog;
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        headerTitle = (TextView) toolbar.findViewById(R.id.title_text);
        MyCredits = (TextView) toolbar.findViewById(R.id.toolbar_credits);
        imageButton_back = (ImageButton) toolbar.findViewById(R.id.tool_bar_backButton);
        imageButton_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        TextView textView_credits = (TextView) toolbar.findViewById(R.id.Credit_textview);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.credit_bg);
        headerTitle.setText("Setting");
        MyCredits.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        textView_credits.setVisibility(View.INVISIBLE);
        imageButton_back.setVisibility(View.VISIBLE);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                SettingActivity.this.finish();
            }
        });


        editText_name = (EditText)findViewById(R.id.et_name);
        editText_dob = (EditText)findViewById(R.id.et_dob);
        editText_email = (EditText)findViewById(R.id.et_email);
        editText_password = (EditText)findViewById(R.id.et_password);
        button_register = (Button)findViewById(R.id.button_register);
        editText_dob.setText(new UserSession(this).getUserDOB());
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

                progressDialog = new ProgressDialog(SettingActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating...");
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
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CLIENT_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



    void HitAPI() {

        APIService apiservice = retrofit.create(APIService.class);
        Call<UpdateClientResponse> APICall = apiservice.updateClient(new UserSession(SettingActivity.this).getUserId(),
                editText_name.getText().toString()
                ,editText_password.getText().toString()
                ,new UserSession(SettingActivity.this).getUserEmail()
                ,editText_email.getText().toString()
                ,editText_dob.getText().toString());

        APICall.enqueue(new Callback<UpdateClientResponse>() {
            @Override
            public void onResponse(Call<UpdateClientResponse> call, Response<UpdateClientResponse> response) {


                if (response.body() != null) {
                    if(response.body().getResult() == 1)
                    {

                        UserSession mUserSession = new UserSession(SettingActivity.this);
                        mUserSession.setIsLoggedIn(true);
                        mUserSession.setUserEmail(editText_email.getText().toString());
                        mUserSession.setUserDOB(editText_dob.getText().toString());
                        mUserSession.setUserName(editText_name.getText().toString());

                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
            public void onFailure(Call<UpdateClientResponse> call, Throwable t) {
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
            return new DatePickerDialog(SettingActivity.this, myDateListener, year, month, day);
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
        if (userDob.isEmpty() || userDob.length() < 4 || userDob.length() > 50) {
            editText_dob.setError("enter valid user name");
            valid = false;
        } else {
            editText_dob.setError(null);
        }
        return valid;
    }
}
