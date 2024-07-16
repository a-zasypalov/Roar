package com.gaoyun.roar.util

interface EmailSender {
    fun sendSupportEmail(to: String, subject: String)
}