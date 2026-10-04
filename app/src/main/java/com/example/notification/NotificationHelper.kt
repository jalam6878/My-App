package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_REMINDERS = "vitalstep_reminders"
        const val CHANNEL_GOALS = "vitalstep_goals"
        const val CHANNEL_WELLNESS = "vitalstep_wellness"

        const val NOTIF_ID_STEP_GOAL = 1001
        const val NOTIF_ID_ACHIEVEMENT = 1002
        const val NOTIF_ID_WATER = 1003
        const val NOTIF_ID_WALK = 1004
        const val NOTIF_ID_MORNING = 1005
        const val NOTIF_ID_EVENING = 1006
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

            val reminderChannel = NotificationChannel(
                CHANNEL_REMINDERS,
                "Health Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily hydration and walking reminders"
            }

            val goalChannel = NotificationChannel(
                CHANNEL_GOALS,
                "Goal & Milestone Celebrations",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Congratulations for reaching step targets and milestones"
            }

            val wellnessChannel = NotificationChannel(
                CHANNEL_WELLNESS,
                "Daily Wellness Tips",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Morning and evening health recommendations"
            }

            notificationManager?.createNotificationChannels(listOf(reminderChannel, goalChannel, wellnessChannel))
        }
    }

    fun showGoalReachedNotification(steps: Int, goal: Int, isBengali: Boolean) {
        val title = if (isBengali) "অভিনন্দন! লক্ষ্য অর্জিত হয়েছে!" else "Goal Crushed! Congratulations!"
        val message = if (isBengali)
            "আপনি সফলভাবে আপনার দৈনিক $goal পদক্ষেপের লক্ষ্য অতিক্রম করেছেন ($steps পদক্ষেপ)!"
        else
            "You have achieved and exceeded your daily goal of $goal steps with $steps steps!"

        sendNotification(NOTIF_ID_STEP_GOAL, CHANNEL_GOALS, title, message)
    }

    fun showAchievementUnlockedNotification(badgeTitle: String, isBengali: Boolean) {
        val title = if (isBengali) "নতুন ব্যাজ আনলক হয়েছে!" else "New Achievement Unlocked!"
        val message = if (isBengali)
            "আপনি $badgeTitle অর্জন করেছেন! আপনার অগ্রগতি অসাধারণ!"
        else
            "You've unlocked the $badgeTitle badge! Outstanding progress!"

        sendNotification(NOTIF_ID_ACHIEVEMENT, CHANNEL_GOALS, title, message)
    }

    fun showWaterReminderNotification(isBengali: Boolean) {
        val title = if (isBengali) "পানি পানের সময় হয়েছে" else "Hydration Reminder"
        val message = if (isBengali)
            "এক গ্লাস পানি পান করে শরীরকে সতেজ ও সচল রাখুন।"
        else
            "Drink a glass of water to keep your body energized and hydrated."

        sendNotification(NOTIF_ID_WATER, CHANNEL_REMINDERS, title, message)
    }

    fun showWalkingReminderNotification(isBengali: Boolean) {
        val title = if (isBengali) "পদক্ষেপ নেওয়ার সময়" else "Time to Step It Up"
        val message = if (isBengali)
            "কিছুক্ষণ হেঁটে আসুন এবং আপনার দৈনিক লক্ষ্যের দিকে এগিয়ে যান।"
        else
            "Take a short brisk stroll to advance towards your daily step goal."

        sendNotification(NOTIF_ID_WALK, CHANNEL_REMINDERS, title, message)
    }

    fun showMorningTipNotification(tipTitle: String, isBengali: Boolean) {
        val title = if (isBengali) "শুভ সকাল - আজকের স্বাস্থ্য টিপ" else "Good Morning - Today's Health Tip"
        sendNotification(NOTIF_ID_MORNING, CHANNEL_WELLNESS, title, tipTitle)
    }

    private fun sendNotification(id: Int, channelId: String, title: String, message: String) {
        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val manager = NotificationManagerCompat.from(context)
            manager.notify(id, builder.build())
        } catch (_: SecurityException) {
            // Notification permission might be denied by user
        }
    }
}
