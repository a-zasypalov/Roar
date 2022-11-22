package com.gaoyun.feature_create_reminder.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gaoyun.feature_create_reminder.notification.ReminderBroadcastArguments.REMINDER_ID_EXTRA
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.reminder.GetReminder
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class ReminderBroadcastReceiver : BroadcastReceiver() {
    private val helper: ReminderBroadcastReceiverHelper by lazy { ReminderBroadcastReceiverHelper() }

    override fun onReceive(context: Context, intent: Intent) {
        helper.onReceive(context, intent)
    }
}

fun Intent.putReminderBroadcastReceiverArguments(reminderId: String): Intent {
    putExtra(REMINDER_ID_EXTRA, reminderId)
    return this
}

internal object ReminderBroadcastArguments {
    const val REMINDER_ID_EXTRA = "reminderIdReminderBroadcastExtra"
}

class ReminderBroadcastReceiverHelper : KoinComponent {

    private val getInteraction: GetInteraction by inject()
    private val getReminder: GetReminder by inject()

    fun onReceive(context: Context, intent: Intent) = runBlocking {
        getReminder.getReminder(intent.getStringExtra(REMINDER_ID_EXTRA) ?: "")
            .filterNotNull()
            .filter { !it.isCompleted }
            .collect { reminder ->
                getInteraction.getInteraction(reminder.interactionId)
                    .filterNotNull()
                    .filter { it.isActive }
                    .collect { interaction ->
                        val title = interaction.name
                        val message = interaction.notes

                        val notification = NotificationCompat.Builder(context, "com.gaoyun.roar.RemindersChannel")
                            .setSmallIcon(com.gaoyun.common.R.drawable.ic_tab_home)
                            .setContentText(message)
                            .setContentTitle(title)
                            .build()

                        val manager = context.getSystemService(NotificationManager::class.java)
                        manager.notify(Random.nextInt(), notification)
                    }
            }
    }
}