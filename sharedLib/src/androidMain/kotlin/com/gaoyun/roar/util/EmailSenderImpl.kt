package com.gaoyun.roar.util

import android.content.Intent

class EmailSenderImpl(private val activityProvider: ActivityProvider) : EmailSender {

    override fun sendSupportEmail(to: String, subject: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        activityProvider.activeActivity?.startActivity(intent)
    }
}