package com.gaoyun.roar.domain.onboarding

import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys

class CompleteOnboardingUseCase(private val prefs: Preferences) {

    fun completeOnboarding() {
        prefs.setBoolean(PreferencesKeys.ONBOARDING_COMPLETE, true)
    }

    fun onboardingIsComplete() = prefs.getBoolean(PreferencesKeys.ONBOARDING_COMPLETE, false)

}