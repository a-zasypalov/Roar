package com.gaoyun.roar.ui.navigation

interface CloseAppActionHandler {
    fun closeApp()
}

class NoopCloseAppActionHandler : CloseAppActionHandler {
    override fun closeApp() {}
}