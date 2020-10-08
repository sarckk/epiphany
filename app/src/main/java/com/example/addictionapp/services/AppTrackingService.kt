package com.example.addictionapp.services

import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.addictionapp.R
import com.example.addictionapp.data.reflections.ReflectionRepository
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.data.models.suggestion.ActivitySuggestion
import com.example.addictionapp.ui.MainActivity
import com.example.addictionapp.utils.Awareness
import com.example.addictionapp.utils.LocationBroadcastReceiver
import com.example.addictionapp.utils.Notifier
import com.huawei.hms.location.ActivityIdentification
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.SimpleFormatter


class AppTrackingService : Service() {
    private val CHANNEL_ID = "Epiphany"
    private val repo: BlocklistRepository by inject()
    private val reflectionRepo: ReflectionRepository by inject()

    class AppTracker(repo: BlocklistRepository, reflectionRepo: ReflectionRepository, context: Context): Thread() {
        val context = context
        val awareness = Awareness()
        val CHANNEL_ID = "EpiphanyImportant"
        val notifier = Notifier()
        val activities = ActivitySuggestion()
        val reflectionRepo = reflectionRepo
        val applicationsPackageNames = repo.getAllBlacklistedApps().map {
            it.packageName
        }

        val MILLIS_IN_DAY = 86400000
        val MILLIS_IN_HOUR = 60 * 60 * 1000

        var startTimer = System.currentTimeMillis()
        var lastNotifTimer = System.currentTimeMillis() - 60 * 60 * 1000 * 3
        var blacklistedAppRunning = false

        override fun run() {
            val timePreviousDay = usageStatsManager.queryAndAggregateUsageStats(System.currentTimeMillis() - MILLIS_IN_DAY, System.currentTimeMillis())
                .map { usageData -> usageData.value }
                .sumByLong { usageData: UsageStats -> (usageData.totalTimeInForeground / 1000 / 60 / 60) as Long }
            while (true) {
                trackUsedApps()
                if (!blacklistedAppRunning && System.currentTimeMillis() - lastNotifTimer > 4 * MILLIS_IN_HOUR) {
                    shouldShowNotification(timePreviousDay, (System.currentTimeMillis() - startTimer) / 1000)
                }
                //getTime()
                //trackActivities()
                sleep(1000)
            }
        }

        fun trackUsedApps() = GlobalScope.launch {
            val usageEvents = usageStatsManager.queryEvents(System.currentTimeMillis() - 1000, System.currentTimeMillis())

            while (usageEvents.hasNextEvent()) {
                var event: UsageEvents.Event = UsageEvents.Event()
                val success = usageEvents.getNextEvent(event)
                if (success) {
                    if (applicationsPackageNames.contains(event.packageName) && event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                        startTimer = System.currentTimeMillis()
                        blacklistedAppRunning = true
                    }
                    if (applicationsPackageNames.contains(event.packageName) && event.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                        Log.d("testable", "Blacklisted app stopped")
                        blacklistedAppRunning = false


                        val today = LocalDateTime.now()
                        // today's date in SQLite friendly format
                        val todayDateFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(today)

                        // check if there hasnt been a reflection today
                        val reflection = reflectionRepo.getReflection(todayDateFormatted)
                        if(reflection == null){
                            val pendingIntent: PendingIntent =
                                NavDeepLinkBuilder(context)
                                    .setComponentName(MainActivity::class.java)
                                    .setGraph(R.navigation.nav_graph)
                                    .setDestination(R.id.wellbeingStateFragment)
                                    .createPendingIntent()

                            val notification = Notification.Builder(context, CHANNEL_ID)
                                .setContentTitle("Fill an reflection!")
                                .setContentText("Fill out an reflection")
                                .setSmallIcon(R.drawable.epiphany_logo)
                                .setContentIntent(pendingIntent)
                                .setTicker("todo")
                                .build()

                            with(NotificationManagerCompat.from(context)) {
                                // notificationId is a unique int for each notification that you must define
                                notify(1338, notification)
                            }
                        }
                    }
                }
            }
        }

        private fun getTime() {
            awareness.getTimeCategory(context as Activity)
        }

        private fun shouldShowNotification(timePreviousDay: Long, currentTimeRunning: Long) {

            if (notifier.shouldNotify(timePreviousDay, currentTimeRunning)) {

                lastNotifTimer = System.currentTimeMillis()
                val pendingIntent: PendingIntent =
                    Intent(context, AppTrackingService::class.java).let { notificationIntent ->
                        PendingIntent.getActivity(context, 0, notificationIntent, 0)
                    }
                val notification = Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle("Stay strong!")
                    .setContentText("We detected that you're likely to use social media! Do " + activities.getActivity("morning", "walking") + " instead")
                    .setSmallIcon(R.drawable.epiphany_logo)
                    .setContentIntent(pendingIntent)
                    .setTicker("todo")
                    .build()

                with(NotificationManagerCompat.from(context)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(1337, notification)
                }
            }
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
                .addOnSuccessListener {
                    Log.i(
                        "EpiphanyHMS",
                        "createActivityIdentificationUpdates onSuccess"
                    )
                }
                .addOnFailureListener { e ->
                    Log.e(
                        "EpiphanyHMS",
                        "createActivityIdentificationUpdates onFailure:" + e.message
                    )
                }
        }

        inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
            var sum = 0L
            for (element in this) {
                sum += selector(element)
            }
            return sum
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

        createNotificationChannel(CHANNEL_ID)
        createNotificationChannel("EpiphanyImportant")
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

        val tracker = AppTracker(repo, reflectionRepo, context)
        tracker.start()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun createNotificationChannel(CHANNEL_ID: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Epiphany service",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}