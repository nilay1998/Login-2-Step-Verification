package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.login.Retrofit.NetworkClient;
import com.example.login.Retrofit.Profile;
import com.example.login.Retrofit.RequestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    private EditText username;
    private EditText phone;
    private ImageView send_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=username.getText().toString();
                String number=phone.getText().toString();

               if(number.length()==10)
               {
                   Retrofit retrofit = NetworkClient.getRetrofitClient();
                   final RequestService requestService=retrofit.create(RequestService.class);
                   Call<Profile> call=requestService.createUser(name,number);
                   call.enqueue(new Callback<Profile>() {
                       @Override
                       public void onResponse(Call<Profile> call, Response<Profile> response) {
                           Toast.makeText(getApplicationContext(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                           if(response.body().getStatus().equals("1"))
                           {
                               Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                               startActivity(intent);
                               finish();
                           }
                       }

                       @Override
                       public void onFailure(Call<Profile> call, Throwable t) {
                           Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                       }
                   });
               }
               else
               {
                   Toast.makeText(getApplicationContext(),"Enter a 10 digit number",Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    private void initViews()
    {
        username=findViewById(R.id.username);
        phone=findViewById(R.id.phone);
        send_button=findViewById(R.id.send_signup);
    }


}
