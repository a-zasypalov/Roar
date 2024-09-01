package com.gaoyun.roar.android.notifications

import com.gaoyun.roar.notifications.NotificationBadgeHandler

class NotificationBadgeHandlerImpl : NotificationBadgeHandler {
    override fun setShowBadge(count: Int) {
        //Noop because Android doesn't support badge handling
    }
}