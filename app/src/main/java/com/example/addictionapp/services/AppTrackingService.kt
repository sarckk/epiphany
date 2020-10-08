package com.example.addictionapp.services

import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.addictionapp.R
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.utils.Awareness
import com.example.addictionapp.utils.LocationBroadcastReceiver
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.location.ActivityIdentification
import org.koin.android.ext.android.inject


class AppTrackingService : Service() {
    private val CHANNEL_ID = "Epiphany"
    private val repo: BlocklistRepository by inject()

    class AppTracker(repo: BlocklistRepository, context: Context): Thread() {
        val context = context
        val awareness = Awareness()
        val applicationsPackageNames = repo.getAllBlacklistedApps().map {
            it.packageName
        }

        var isBlocklistedAppRunning = false

        override fun run() {
            while (true) {
                trackUsedApps()
                getTime()
                trackActivities()
                sleep(10000)
            }
        }

        fun trackUsedApps() {
            val usageEvents = usageStatsManager.queryEvents(System.currentTimeMillis() - 10000, System.currentTimeMillis())

            var blocklistedAppInUsageEvents = false
            while (usageEvents.hasNextEvent()) {
                var event: UsageEvents.Event = UsageEvents.Event()
                val success = usageEvents.getNextEvent(event)
                if (success) {
                    if (applicationsPackageNames.contains(event.packageName)) {
                        blocklistedAppInUsageEvents = true
                        isBlocklistedAppRunning = true
                    }
                }
            }

            if (isBlocklistedAppRunning && !blocklistedAppInUsageEvents) {
                isBlocklistedAppRunning = false

                //launch reflection intent
                NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.wellbeingStateFragment)
                    .createPendingIntent()
            }
        }

        private fun getTime() {
            awareness.getTimeCategory(context as Activity)
        }

        private fun getPendingIntent(): PendingIntent? {
            // The LocationBroadcastReceiver class is a customized static broadcast class. For details about the implementation, please refer to the sample code.
            val intent = Intent(context, LocationBroadcastReceiver::class.java)
            intent.action = LocationBroadcastReceiver.ACTION_PROCESS_LOCATION
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun trackActivities() {
            val activityIdentificationService = ActivityIdentification.getService(context)
            val pendingIntent = getPendingIntent();

            activityIdentificationService.createActivityIdentificationUpdates(5000, pendingIntent)
                .addOnSuccessListener(OnSuccessListener<Void?> {
                    Log.i(
                        "EpiphanyHMS",
                        "createActivityIdentificationUpdates onSuccess"
                    )
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Log.e(
                        "EpiphanyHMS",
                        "createActivityIdentificationUpdates onFailure:" + e.message
                    )
                })
        }
    }

    companion object {
        lateinit var context: Context
        lateinit var usageStatsManager: UsageStatsManager;

        fun startService(context: Context) {
            usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val startIntent = Intent(context, AppTrackingService::class.java)
            this.context = context
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, AppTrackingService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createNotificationChannel()
        val pendingIntent: PendingIntent =
            Intent(this, AppTrackingService::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }
        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.front_notif_name))
            .setContentText(getText(R.string.front_notif_description))
            .setSmallIcon(R.drawable.epiphany_logo)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.ticker_text))
            .build()

        val tracker = AppTracker(repo, context)
        tracker.start()

        startForeground(1, notification)



        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Epiphany service",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}