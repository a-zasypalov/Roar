package com.gaoyun.roar.presentation

import com.gaoyun.roar.util.EmailSender
import kotlinx.coroutines.flow.MutableStateFlow

class AboutScreenViewModel(
    private val emailSender: EmailSender,
) : BaseViewModel() {
    override val viewState = MutableStateFlow(Unit)

    fun sendEmail(to: String, subject: String) {
        emailSender.sendSupportEmail(to, subject)
    }
}