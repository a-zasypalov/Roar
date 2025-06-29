package com.gaoyun.roar.ui.navigation

sealed class NavigationAction {
    class NavigateToPath(val path: String) : NavigationAction()
    class NavigateTo<T : Any>(val args: T) : NavigationAction()
    class NavigateToPathWithBackHandler(
        val path: String,
        val popupTo: String,
        val inclusive: Boolean = false,
    ) : NavigationAction()

    class NavigateToWithBackHandler<T : Any, P : Any>(
        val args: T,
        val popupTo: P,
        val inclusive: Boolean = false,
    ) : NavigationAction()

    class NavigateToWithPathBackHandler<T : Any>(
        val args: T,
        val popupTo: String,
        val inclusive: Boolean = false,
    ) : NavigationAction()

    data object NavigateBack : NavigationAction()
    class PopToPath(val path: String, val inclusive: Boolean) : NavigationAction()
    class PopTo<T : Any>(val args: T, val inclusive: Boolean) : NavigationAction()
}