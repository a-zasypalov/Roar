package com.gaoyun.roar.android.notifications

import com.gaoyun.roar.notifications.AppReminderInfoHandler

class AppReminderInfoHandlerImpl : AppReminderInfoHandler {
    override fun setShowBadge(count: Int) {
        //Noop because Android doesn't support badge handling
    }
}