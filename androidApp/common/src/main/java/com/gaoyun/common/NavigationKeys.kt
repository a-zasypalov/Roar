package com.gaoyun.common

object NavigationKeys {

    object Arg {
    }

    object RouteLocal {

    }

    object RouteGlobal {
        private const val REGISTER_USER = "REGISTER_USER"

        const val HOME_ROUTE = "HOME"
        const val REGISTER_USER_ROUTE = "$HOME_ROUTE/$REGISTER_USER"

    }

}