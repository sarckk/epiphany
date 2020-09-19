package com.example.addictionapp

import android.Manifest
import android.app.usage.UsageEvents
import android.app.usage.UsageEvents.Event.ACTIVITY_PAUSED
import android.app.usage.UsageEvents.Event.ACTIVITY_RESUMED
import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var usm: UsageStatsManager;
    private var lastCheck = 0L;
    private var currentlyOnFacebook = false;
    private var onFacebookSince = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main);

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, 1)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        usm = getSystemService(UsageStatsManager::class.java)

        lastCheck = System.currentTimeMillis(); //5 seconds ago

        for(i in 1..100) {
            Handler().postDelayed(this::pollingLoop, (1000L*i))
        }
    }

    private fun pollingLoop() {
        if(currentlyOnFacebook) {
            var duration = (System.currentTimeMillis() - onFacebookSince) / 1000;
            Log.i("test", "On facebook currently for ${duration}s")
            if(duration > 2) {
                Accessibility.getInstance()?.launchSplitScreen();
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
        while (evts.hasNextEvent()) {
            evts.getNextEvent(evt);
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

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch(exc: Exception) {
                Log.e(CAMERA_TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val CAMERA_TAG = "Camera"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
