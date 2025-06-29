package com.gaoyun.roar.ui.features.onboarding

import com.gaoyun.roar.domain.onboarding.CompleteOnboardingUseCase
import com.gaoyun.roar.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class OnboardingViewModel(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
) : BaseViewModel() {
    override val viewState = MutableStateFlow(Unit)

    fun completeOnboarding() {
        completeOnboardingUseCase.completeOnboarding()
    }
}