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
import org.koin.android.ext.android.inject

class AppTrackingService : Service() {
    private val CHANNEL_ID = "Epiphany"
    private val repo: BlocklistRepository by inject()

    class AppTracker(repo: BlocklistRepository, context: Context): Thread() {
        val context = context
        val applicationsPackageNames = repo.getAllBlacklistedApps().map {
            it.packageName
        }

        var isBlocklistedAppRunning = false

        override fun run() {

            while (true) {
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



                sleep(10000)
            }


        }
    }

    companion object {
        lateinit var usageStatsManager: UsageStatsManager;

        fun startService(context: Context) {
            usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val startIntent = Intent(context, AppTrackingService::class.java)
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

        val tracker = AppTracker(repo, this)
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