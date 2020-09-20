package com.example.addictionapp.api

import android.util.Log
import com.example.addictionapp.models.AccelerometerResult
import com.example.addictionapp.models.IdResult
import com.example.addictionapp.models.LocationResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class BackendCalls() {

    var backend: BackendService = BackendSingleton().getInstance()

    fun getId(): String {
        var result = ""
        var call = backend.getId()

        call.enqueue(object : Callback<IdResult> {
            override fun onResponse(call: Call<IdResult>, response: Response<IdResult>) {
                if (response.isSuccessful){
                    result = response.body()!!.id
                }
            }
            override fun onFailure(call: Call<IdResult>, t: Throwable) {
               Log.i("test", "Error in getid")
            }
        })

        return result
    }

    fun sendLocation(id: String, lat: String, lng: String) : Boolean {

        var data = HashMap<String, String>()
        data.put("id", id)
        data.put("lat", lat)
        data.put("lng", lng)

        var result = false
        var call = backend.sendLocation(data)

        call.enqueue(object : Callback<LocationResult> {
            override fun onResponse(call: Call<LocationResult>, response: Response<LocationResult>) {
                if (response.isSuccessful){
                    result = response.body()!!.success
                }
            }
            override fun onFailure(call: Call<LocationResult>, t: Throwable) {
                Log.i("test", "Error in sendLocation")
            }
        })

        return result
    }

    fun sendAccelerometer(id: String, state: String) : Boolean {
        var data = HashMap<String, String>()
        data.put("ua_user_id", id)
        data.put("accel", state)

        var result = false
        var call = backend.sendAccelerometer(data)

        call.enqueue(object : Callback<AccelerometerResult> {
            override fun onResponse(call: Call<AccelerometerResult>, response: Response<AccelerometerResult>) {
                if (response.isSuccessful){
                    result = response.body()!!.success
                }
            }
            override fun onFailure(call: Call<AccelerometerResult>, t: Throwable) {
                Log.i("test", "Error in sendAccelerometer")
            }
        })

        return result
    }
}