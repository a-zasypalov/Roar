package com.gaoyun.roar.presentation.about_screen

import com.gaoyun.roar.presentation.MultiplatformBaseViewModel
import com.gaoyun.roar.util.EmailSender

class AboutScreenViewModel(
    private val emailSender: EmailSender,
) : MultiplatformBaseViewModel<AboutScreenContract.Event, AboutScreenContract.State, AboutScreenContract.Effect>() {

    override fun setInitialState() = AboutScreenContract.State()

    override fun handleEvents(event: AboutScreenContract.Event) {
        when (event) {
            is AboutScreenContract.Event.NavigateBack -> {
                setEffect { AboutScreenContract.Effect.NavigateBack }
            }
        }
    }

    fun sendEmail(to: String, subject: String) {
        emailSender.sendSupportEmail(to, subject)
    }
}