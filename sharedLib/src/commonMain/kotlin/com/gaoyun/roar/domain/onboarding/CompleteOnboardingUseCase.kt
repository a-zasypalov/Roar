package com.gaoyun.roar.domain.onboarding

import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CompleteOnboardingUseCase : KoinComponent {

    private val preferences: Preferences by inject()

    fun completeOnboarding() {
        preferences.setBoolean(PreferencesKeys.ONBOARDING_COMPLETE, true)
    }

}