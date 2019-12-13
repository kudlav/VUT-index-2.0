package prvnimilion.vutindex

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.google.firebase.analytics.FirebaseAnalytics
import prvnimilion.vutindex.home.view.HomeActivity
import prvnimilion.vutindex.ui_common.enums.DifferenceType
import prvnimilion.vutindex.ui_common.util.Difference

class VutIndexNotificationManager {

    fun sendNotification(difference: Difference?) {
        if (difference == null) return
        val context = BaseApplication.applicationContext()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)

        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, difference.differenceType.toString())
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Notification showed!")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "notification")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle)

        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK and FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        notificationBuilder
            .setAutoCancel(true)
            .setColor(Color.RED)
            .setWhen(System.currentTimeMillis())
            .setPriority(Notification.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.notification_icon)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )

        when (difference.differenceType) {
            DifferenceType.PASSED -> {
                val notifString = if (difference.passed!!) {
                    "Předmět ${difference.subject} úspěšně absolvován"
                } else {
                    "Předmět ${difference.subject} neabsolvován"
                }

                notificationBuilder
                    .setContentTitle("Výsledek zkoušky:")
                    .setContentText(notifString)

            }
            DifferenceType.CREDIT -> {
                val notifString = if (difference.creditGiven!!) {
                    "Zápočet z ${difference.subject} udělen! "
                } else {
                    "Zápočet z ${difference.subject} neudělen! "
                }

                notificationBuilder
                    .setContentTitle("Zápočet:")
                    .setContentText(notifString)

            }
            DifferenceType.POINTS -> {
                notificationBuilder
                    .setContentTitle("Nové body:")
                    .setContentText(
                        String.format("${difference.pointsGiven} bodů z ${difference.subject} na ISu!")
                    )

            }
            DifferenceType.MESSAGE -> {
                notificationBuilder
                    .setContentText("Nová VUTzpráva")
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationChannel.vibrationPattern = longArrayOf(100, 200)

            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, notificationBuilder.build())

        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(300)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "12"
        const val NOTIFICATION_CHANNEL_NAME = "VUT_INDEX_CHANNEL"
    }
}