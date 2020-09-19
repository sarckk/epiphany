package com.example.addictionapp.api;

import com.example.addictionapp.models.AccelerometerResult
import com.example.addictionapp.models.IdResult;
import com.example.addictionapp.models.LocationResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface BackendService {

    @GET("id")
    fun getId(): Call<IdResult>

    @POST("location")
    fun sendLocation(@Body body : HashMap<String, String>): Call<LocationResult>

    @POST("accelerometer")
    fun sendAccelerometer(@Body body : HashMap<String, String>): Call<AccelerometerResult>


    companion object {
        fun create(): BackendService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl("test")
                    .build()

            return retrofit.create(BackendService::class.java)
        }
    }
}
