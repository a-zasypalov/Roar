package com.gaoyun.common.navigation

sealed class NavigationAction {
    class NavigateTo(val path: String) : NavigationAction()
    object NavigateBack : NavigationAction()
    class PopTo(val path: String, val inclusive: Boolean) : NavigationAction()

}