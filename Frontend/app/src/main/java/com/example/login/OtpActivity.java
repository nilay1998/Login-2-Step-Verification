package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.Retrofit.NetworkClient;
import com.example.login.Retrofit.OtpClient;
import com.example.login.Retrofit.OtpResponse;
import com.example.login.Retrofit.RequestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OtpActivity extends AppCompatActivity {

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private Button button;
    private TextView textView;
    private String number;
    private String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Intent intent=getIntent();
        number=intent.getStringExtra("number");
        initViews();

        textView.setText("We have sent an SMS on "+number+" with a 6 digit verification code");

        Retrofit retrofit = OtpClient.getRetrofitClient();
        final RequestService requestService=retrofit.create(RequestService.class);
        Call<OtpResponse> call=requestService.getOtp(number);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if(response.body().getStatus().equals("Success"))
                {
                    details=response.body().getDetails();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error in sending OTP",Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=et1.getText().toString()+et2.getText().toString()+et3.getText().toString()+et4.getText().toString()+et5.getText().toString()+et6.getText().toString();

                if(otp.length()!=6)
                {
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = OtpClient.getRetrofitClient();
                    final RequestService requestService=retrofit.create(RequestService.class);
                    Call<OtpResponse> call=requestService.verifyOtp(details,otp);
                    call.enqueue(new Callback<OtpResponse>() {
                        @Override
                        public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                            if(response.body()!=null)
                            {
                                if(response.body().getStatus().equals("Success"))
                                {
                                    Toast.makeText(getApplicationContext(),"OTP Verified",Toast.LENGTH_SHORT).show();
                                }
                                else if(response.body().getStatus().equals("Error"))
                                {
                                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<OtpResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Error in sending OTP",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initViews()
    {
        textView=findViewById(R.id.phoneText);
        button=findViewById(R.id.otpbutton);
        et1=findViewById(R.id.editText1);
        et2=findViewById(R.id.editText2);
        et3=findViewById(R.id.editText3);
        et4=findViewById(R.id.editText4);
        et5=findViewById(R.id.editText5);
        et6=findViewById(R.id.editText6);

        et1.addTextChangedListener(new GenericTextWatcher(et1));
        et2.addTextChangedListener(new GenericTextWatcher(et2));
        et3.addTextChangedListener(new GenericTextWatcher(et3));
        et4.addTextChangedListener(new GenericTextWatcher(et4));
        et5.addTextChangedListener(new GenericTextWatcher(et5));
        et6.addTextChangedListener(new GenericTextWatcher(et6));
    }

    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {
                case R.id.editText1:
                    if(text.length()==1)
                        et2.requestFocus();
                    break;
                case R.id.editText2:
                    if(text.length()==1)
                        et3.requestFocus();
                    else if(text.length()==0)
                        et1.requestFocus();
                    break;
                case R.id.editText3:
                    if(text.length()==1)
                        et4.requestFocus();
                    else if(text.length()==0)
                        et2.requestFocus();
                    break;
                case R.id.editText4:
                    if(text.length()==1)
                        et5.requestFocus();
                    if(text.length()==0)
                        et3.requestFocus();
                    break;
                case R.id.editText5:
                    if(text.length()==1)
                        et6.requestFocus();
                    if(text.length()==0)
                        et4.requestFocus();
                    break;
                case R.id.editText6:
                    if(text.length()==0)
                        et5.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}
