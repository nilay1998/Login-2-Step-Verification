package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.Retrofit.NetworkClient;
import com.example.login.Retrofit.Profile;
import com.example.login.Retrofit.RequestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private EditText phone_number;
    private ImageView send_button;
    private TextView signup_text;
    private ImageView welcome;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                relativeLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                welcome.setVisibility(GONE);
            }
        }.start();

        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number=phone_number.getText().toString().trim();
                if(number.length()==10)
                {
                    Retrofit retrofit = NetworkClient.getRetrofitClient();
                    final RequestService requestService=retrofit.create(RequestService.class);
                    Call<Profile> call=requestService.loginUser(number);
                    call.enqueue(new Callback<Profile>() {
                        @Override
                        public void onResponse(Call<Profile> call, Response<Profile> response) {
                            Toast.makeText(getApplicationContext(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            if(response.body().getStatus().equals("1"))
                            {
                                Intent intent=new Intent(MainActivity.this,OtpActivity.class);
                                intent.putExtra("number",number);
                                startActivity(intent);
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
                    Toast.makeText(getApplicationContext(),"Enter 10 digit phone number",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews()
    {
        progressBar=findViewById(R.id.loadingProgressBar);
        relativeLayout=findViewById(R.id.rl);
        welcome=findViewById(R.id.welcome);
        phone_number=findViewById(R.id.edittext);
        send_button=findViewById(R.id.send_login);
        signup_text=findViewById(R.id.signup);
        ImageView mimageView=findViewById(R.id.imageview);
        Bitmap mbitmap=((BitmapDrawable) getResources().getDrawable(R.drawable.login_image)).getBitmap();
        Bitmap imageRounded=Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas=new Canvas(imageRounded);
        Paint mpaint=new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 400, 400, mpaint); // Round Image Corner 100 100 100 100
        mimageView.setImageBitmap(imageRounded);
    }
}
