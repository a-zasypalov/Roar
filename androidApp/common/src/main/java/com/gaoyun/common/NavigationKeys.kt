package com.gaoyun.common

object NavigationKeys {

    object Arg {
    }

    object RouteLocal {

    }

    object RouteGlobal {
        private const val REGISTER_USER = "REGISTER_USER"
        private const val ADD_PET = "ADD_PET"

        const val HOME_ROUTE = "HOME"
        const val REGISTER_USER_ROUTE = "$HOME_ROUTE/$REGISTER_USER"
        const val ADD_PET_ROUTE = "$HOME_ROUTE/$ADD_PET"

    }

}