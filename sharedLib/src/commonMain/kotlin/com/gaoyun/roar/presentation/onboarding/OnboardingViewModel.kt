package com.gaoyun.roar.presentation.onboarding

import com.gaoyun.roar.domain.onboarding.CompleteOnboardingUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnboardingViewModel : BaseViewModel<OnboardingScreenContract.Event, OnboardingScreenContract.State, OnboardingScreenContract.Effect>(), KoinComponent {

    private val completeOnboardingUseCase: CompleteOnboardingUseCase by inject()

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