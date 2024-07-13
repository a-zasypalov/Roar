package com.gaoyun.roar.ui.navigation

sealed class NavigationAction {
    class NavigateTo(val path: String) : NavigationAction()
    data object NavigateBack : NavigationAction()
    class PopTo(val path: String, val inclusive: Boolean) : NavigationAction()

}