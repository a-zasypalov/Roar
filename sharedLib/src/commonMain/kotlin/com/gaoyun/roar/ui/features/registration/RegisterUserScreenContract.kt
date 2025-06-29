package com.gaoyun.roar.ui.features.registration

import com.gaoyun.roar.ui.navigation.NavigationSideEffect

class RegisterUserScreenContract {
    sealed class Effect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            object ToPetAdding : Navigation()
        }
    }

}