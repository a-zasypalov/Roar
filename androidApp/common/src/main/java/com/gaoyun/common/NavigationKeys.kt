package com.gaoyun.common

object NavigationKeys {

    object Arg {
        const val PET_ID_KEY = "PET_ID_KEY"
    }

    object RouteLocal {

    }

    object RouteGlobal {
        private const val REGISTER_USER = "REGISTER_USER"
        private const val ADD_PET = "ADD_PET"
        private const val ADD_REMINDER = "ADD_REMINDER"

        const val HOME_ROUTE = "HOME"
        const val REGISTER_USER_ROUTE = "$HOME_ROUTE/$REGISTER_USER"
        const val ADD_PET_ROUTE = "$HOME_ROUTE/$ADD_PET"

        const val ADD_REMINDER_ROUTE = "$HOME_ROUTE/$ADD_REMINDER/{${Arg.PET_ID_KEY}}"
        const val ADD_REMINDER_ROUTE_BUILDER = "$HOME_ROUTE/$ADD_REMINDER"

    }

}