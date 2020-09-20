package com.example.addictionapp.api;

import com.example.addictionapp.models.*
import com.google.gson.GsonBuilder

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface BackendService {

    @POST("get_new_user_id")
    fun getId(): Call<IdResult>

    @POST("location")
    fun sendLocation(@Body body : HashMap<String, String>): Call<LocationResult>

    @POST("accelerometer")
    fun sendAccelerometer(@Body body : HashMap<String, String>): Call<AccelerometerResult>

    @POST("suggestion")
    fun getSuggestion(@Body body : HashMap<String, String>): Call<SuggestionResult>

    @POST("notification")
    fun checkForShowNotification(@Body body : HashMap<String, String>): Call<NotificationResult>

    companion object {
        fun create(): BackendService {

            val BASE_URL = "https://a8dc5ab7dae5.ngrok.io"

            var gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            return retrofit.create(BackendService::class.java)
        }
    }
}
