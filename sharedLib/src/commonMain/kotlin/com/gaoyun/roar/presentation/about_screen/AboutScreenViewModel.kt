package com.gaoyun.roar.presentation.about_screen

import com.gaoyun.roar.presentation.BaseViewModel
import org.koin.core.component.KoinComponent

class AboutScreenViewModel : BaseViewModel<AboutScreenContract.Event, AboutScreenContract.State, AboutScreenContract.Effect>(),
    KoinComponent {

    override fun setInitialState() = AboutScreenContract.State()

    override fun handleEvents(event: AboutScreenContract.Event) {
        when (event) {
            is AboutScreenContract.Event.NavigateBack -> {
                setEffect { AboutScreenContract.Effect.NavigateBack }
            }
        }
    }

}