package com.example.addictionapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.usage.UsageEvents
import android.app.usage.UsageEvents.Event.ACTIVITY_PAUSED
import android.app.usage.UsageEvents.Event.ACTIVITY_RESUMED
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.addictionapp.api.BackendCalls
import com.example.addictionapp.models.SuggestionResult
import com.example.addictionapp.utils.LocationBroadcastReceiver
import com.example.addictionapp.utils.MessageEvent
import com.huawei.hms.location.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var usm: UsageStatsManager;
    private var id = "71053"
    private var lastCheck = 0L;
    private var currentlyOnFacebook = false;
    private var activatedAction = false;
    private var notificationShown = false;
    private var onFacebookSince = 0L;
    private var backend = BackendCalls()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        //initialiseId()
        createNotificationChannel()

        val activityIdentificationService = ActivityIdentification.getService(this)
        val pendingIntent = getPendingIntent()

        activityIdentificationService.createActivityIdentificationUpdates(5000, pendingIntent)
            .addOnSuccessListener {
                Log.i(
                    "test",
                    "createActivityIdentificationUpdates onSuccess"
                )
            }
            .addOnFailureListener { e ->
                Log.e(
                    "test",
                    "createActivityIdentificationUpdates onFailure:" + e.message
                )
            }

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val mLocationCallback: LocationCallback
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                backend.sendLocation("71053", locationResult.lastLocation.latitude.toString(), locationResult.lastLocation.longitude.toString())
            }
        }

        fusedLocationProviderClient
            .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
            .addOnSuccessListener {
                Log.i("test", "success")
            }
            .addOnFailureListener {
                Log.i("test", "failure")
            }


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this,
                REQUIRED_PERMISSIONS, 1)
        }

        usm = getSystemService(UsageStatsManager::class.java)

        lastCheck = System.currentTimeMillis(); //5 seconds ago

        for(i in 1..100) {
            Handler().postDelayed(this::pollingLoop, (1000L*i))
        }
    }

    private fun initialiseId() {
        if (id == "") {
            id = backend.getId()
        }
    }

    private fun getPendingIntent(): PendingIntent? {
        // The LocationBroadcastReceiver class is a customized static broadcast class. For details about the implementation, please refer to the sample code.
        val intent = Intent(this, LocationBroadcastReceiver::class.java)
        intent.action = LocationBroadcastReceiver.ACTION_PROCESS_LOCATION
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun pollingLoop() {
        if(currentlyOnFacebook) {
            var duration = (System.currentTimeMillis() - onFacebookSince) / 1000;
            Log.i("test", "On facebook currently for ${duration}s")
            if(duration > 3 && !notificationShown){
                notificationShown = true
                var call = backend.getSuggestion("71053")
                call.enqueue(object : Callback<SuggestionResult> {
                    override fun onResponse(call: Call<SuggestionResult>, response: Response<SuggestionResult>) {
                        if (response.isSuccessful){
                            pushNotification(response.body()!!.best_activity)
                        }
                    }
                    override fun onFailure(call: Call<SuggestionResult>, t: Throwable) {
                        Log.i("test", "Error in getSuggestion")
                    }
                })
            }

            if(duration > 30 && !activatedAction) {
                activatedAction = true
                EventBus.getDefault().post(
                    MessageEvent(
                        false
                    )
                );
            }
        }

        queryNewEvents()
    }

    private fun queryNewEvents() {
        Log.i("test", "Querying for events...")
        var currentCheck = System.currentTimeMillis();
        val evts = usm.queryEvents(lastCheck, currentCheck) //One day
        lastCheck = currentCheck

        val evt = UsageEvents.Event();

        var lastResumeTime = System.currentTimeMillis();
        Log.i("poll", evts.hasNextEvent().toString())
        while (evts.hasNextEvent()) {
            evts.getNextEvent(evt);
            Log.i("poll", "packageName:" + evt.packageName)
            Log.i("poll", "eventType:" + evt.eventType)
            if(evt.packageName == "com.facebook.katana" && (evt.eventType == ACTIVITY_PAUSED || evt.eventType == ACTIVITY_RESUMED)) {
                if (evt.eventType == ACTIVITY_RESUMED) {
                    currentlyOnFacebook = true;
                    onFacebookSince = evt.timeStamp
                }
                if (evt.eventType == ACTIVITY_PAUSED) {
                    currentlyOnFacebook = false;
                    var duration = (evt.timeStamp - onFacebookSince) / 1000
                    if (duration > 1) {
                        Log.i("test", "Closed facebook after ${duration}s")
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        val name: CharSequence = "Social media usage notification"
        val descriptionText = "Notification channel for reporting social media usage"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("socialmedia", name, importance).apply{
            description = descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun pushNotification(msg: String) {
        /*
        <-- If you want to add intent -->
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
         */
        val builder = NotificationCompat.Builder(this, "socialmedia")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("You started using your social media apps...")
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)){
            notify(1, builder.build())
        }
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        @RequiresApi(Build.VERSION_CODES.Q)
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }
}
