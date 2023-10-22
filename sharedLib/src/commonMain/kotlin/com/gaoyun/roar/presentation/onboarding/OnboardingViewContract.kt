package com.gaoyun.roar.presentation.onboarding

import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class OnboardingScreenContract {

    sealed class Event : ViewEvent {
        object OnboardingComplete : Event()
    }

    object State : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
        }
    }
}