package com.gaoyun.roar.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.ui.common.ColorsProvider
import com.gaoyun.roar.ui.navigation.AppNavigator
import com.gaoyun.roar.ui.navigation.NavigationAction
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val colorsProvider: ColorsProvider,
    private val preferences: Preferences,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _event: MutableSharedFlow<NavigationSideEffect> = MutableSharedFlow()
    private val _effect: Channel<NavigationAction> = Channel()
    val navigationEffect = _effect.receiveAsFlow()
    
    init {
        viewModelScope.launch {
            _event.collect {
                handleNavigation(it)
            }
        }
    }

    @Composable
    fun getColorScheme() = colorsProvider.getCurrentScheme()

    fun isOnboardingComplete() = preferences.getBoolean(PreferencesKeys.ONBOARDING_COMPLETE, false)

    fun navigate(event: NavigationSideEffect) {
        viewModelScope.launch { _event.emit(event) }
    }

    private fun handleNavigation(event: NavigationSideEffect) {
        when (event) {
            is BackNavigationEffect -> setEffect { NavigationAction.NavigateBack }
            else -> appNavigator.navigate(event)?.let { setEffect { it } }
        }
    }

    private fun setEffect(builder: () -> NavigationAction) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}