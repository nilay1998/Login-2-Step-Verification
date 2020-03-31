package com.example.login.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestService {

    @POST("register")
    @FormUrlEncoded
    Call<Profile> createUser(@Field("name") String name,
                             @Field("phone") String phone );

    @POST("login")
    @FormUrlEncoded
    Call<Profile> loginUser(@Field("phone") String phone);

    @GET("API/V1/8ec4e2ff-7366-11ea-9fa5-0200cd936042/SMS/{phone_number}/AUTOGEN")
    Call<OtpResponse> getOtp(@Path("phone_number") String phone_number);

    @GET("API/V1/8ec4e2ff-7366-11ea-9fa5-0200cd936042/SMS/VERIFY/{session_id}/{otp_input}")
    Call<OtpResponse> verifyOtp(@Path("session_id") String session_id, @Path("otp_input") String otp_input);
}
