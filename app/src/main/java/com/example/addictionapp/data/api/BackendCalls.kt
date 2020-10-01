package com.example.addictionapp.data.api

import android.util.Log
import com.example.addictionapp.data.models.*
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
                    Log.i("test", result)
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
        data.put("ul_user_id", id)
        data.put("latitude", lat)
        data.put("longitude", lng)

        Log.i("test", data.toString())

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

        Log.i("test", data.toString())

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

    fun getSuggestion(id: String) : Call<SuggestionResult> {
        var data = HashMap<String, String>()
        data.put("suggestion_user_id", id)

        var call = backend.getSuggestion(data)
        return call
    }

    companion object {
        var test = ""
    }

    fun checkForShowNotification(id: String) : String {
        var data = HashMap<String, String>()
        data.put("user_id_notification", id)

        var result = ""
        var call = backend.checkForShowNotification(data)

        call.enqueue(object : Callback<NotificationResult> {
            override fun onResponse(call: Call<NotificationResult>, response: Response<NotificationResult>) {
                if (response.isSuccessful){
                    result = response.body()!!.status
                }
            }
            override fun onFailure(call: Call<NotificationResult>, t: Throwable) {
                Log.i("test", "Error in checkForShowNotification")
            }
        })

        return result
    }
}