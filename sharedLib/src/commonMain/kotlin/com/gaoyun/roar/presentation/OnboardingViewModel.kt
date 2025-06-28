package com.gaoyun.roar.presentation

import com.gaoyun.roar.domain.onboarding.CompleteOnboardingUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class OnboardingViewModel(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
) : BaseViewModel() {
    override val viewState = MutableStateFlow(Unit)

    fun completeOnboarding() {
        completeOnboardingUseCase.completeOnboarding()
    }
}