package com.example.fizjotherapy.control

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.fizjotherapy.R.drawable
import com.example.fizjotherapy.boundry.NotificationRepository
import com.example.fizjotherapy.dto.Notification
import com.example.fizjotherapy.dto.Rola
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.dto.Wizyta
import com.example.fizjotherapy.prompt.PromptService
import java.lang.StringBuilder

class NotificationService(private val activity: Activity) : BroadcastReceiver() {

    private val promptService = PromptService(activity)
    private val notificationRepository = NotificationRepository(activity)

    private val notificationId = 1
    val notificationChannelId = "channel1"
    val notificationTitle = "notificationTitle"
    val notificationMessage = "notificationMess"

    fun sendNotification(user: User) {
        openNotificationChannel(activity)
        val notifications = findNotifications(user)
        val isUserRole = user.rola.vName == Rola.USER.vName
        if (notifications.isNotEmpty()) {
            val intent = Intent(activity, NotificationService::class.java)
            val title = if (!isUserRole) "Nowa wizyta" else "Wizyta zosała anulowana"
            intent.putExtra(notificationTitle, title)
            intent.putExtra(notificationMessage, buildMessage(isUserRole, notifications))
            onReceive(activity, intent)
            Log.d("NotificationService", "Saving that user has received notification")
            notificationRepository.update(user)
        }
    }

    private fun buildMessage(isUserRole: Boolean, notifications: List<Notification>): String {
        val dateBuilder = StringBuilder()
        val firstFragment = if (notifications.size == 1) "Wizyta na termin %s została %s" else "Wizyty termin %s zostały %s"
        notifications.forEach { notification ->
            val dataWizyty = notification.appointment.dataWizyty
            dateBuilder.append(
                String.format(
                    "%s-%s-%s, ",
                    dataWizyty.year,
                    "%02d".format(dataWizyty.month.value),
                    "%02d".format(dataWizyty.dayOfMonth),
                )
            )
        }
        val thirdFragment = if (!isUserRole) "umówiona" else "anulowana"
        return String.format(firstFragment, dateBuilder.toString(), thirdFragment)
    }

    private fun findNotifications(user: User): List<Notification> {
        return notificationRepository.findByUser(user)
    }

    private fun openNotificationChannel(context: Context) {
        val notifyName = "Notify channel"
        val notifyImportance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel =
            NotificationChannel(notificationChannelId, notifyName, notifyImportance)
        notificationChannel.description = "My notify channel"
        val manager = context.getSystemService(
            NotificationManager::class.java
        )
        manager.createNotificationChannel(notificationChannel)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = NotificationCompat.Builder(activity, notificationChannelId)
            .setSmallIcon(drawable.email_icon)
            .setContentTitle(intent?.getStringExtra(notificationTitle) ?: "Null")
            .setContentText(intent?.getStringExtra(notificationMessage) ?: "Null")
            .build()

        val managerCompat = NotificationManagerCompat.from(context!!)
        checkPermission()
        managerCompat.notify(notificationId, notification)
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            promptService.buildAlertDialog("Czy chcesz włączyć powiadomienia", {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                activity.startActivity(intent)
            }, {})
            return
        }
    }

    fun create(appointment: Wizyta) {
        notificationRepository.create(appointment)
    }

    fun create(appointments: Set<Wizyta>) {
        notificationRepository.create(appointments)
    }
}