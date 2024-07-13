package com.gaoyun.roar.presentation.onboarding

import com.gaoyun.roar.domain.onboarding.CompleteOnboardingUseCase
import com.gaoyun.roar.presentation.MultiplatformBaseViewModel

class OnboardingViewModel(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
) : MultiplatformBaseViewModel<OnboardingScreenContract.Event, OnboardingScreenContract.State, OnboardingScreenContract.Effect>() {

    override fun setInitialState() = OnboardingScreenContract.State

    override fun handleEvents(event: OnboardingScreenContract.Event) {
        when (event) {
            is OnboardingScreenContract.Event.OnboardingComplete -> completeOnboarding()
        }
    }

    fun completeOnboarding() {
        completeOnboardingUseCase.completeOnboarding()
    }

}