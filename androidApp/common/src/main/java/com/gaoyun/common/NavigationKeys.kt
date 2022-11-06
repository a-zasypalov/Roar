package com.gaoyun.common

object NavigationKeys {

    object Arg {
        const val PET_ID_KEY = "PET_ID_KEY"
        const val PET_TYPE_KEY = "PET_TYPE_KEY"
        const val AVATAR_KEY = "AVATAR_KEY"
    }

    object RouteLocal {

    }

    object RouteGlobal {
        const val ADD_PET_SETUP = "ADD_PET_SETUP"

        const val HOME_ROUTE = "HOME"
        const val REGISTER_USER_ROUTE = "REGISTER_USER"

        const val ADD_PET_ROUTE = "ADD_PET"
        const val ADD_PET_AVATAR_ROUTE = "$ADD_PET_ROUTE/{${Arg.PET_TYPE_KEY}}"
        const val ADD_PET_DATA_ROUTE = "$ADD_PET_ROUTE/{${Arg.PET_TYPE_KEY}}/{${Arg.AVATAR_KEY}}"
        const val ADD_PET_SETUP_ROUTE = "$ADD_PET_SETUP/{${Arg.PET_ID_KEY}}"

        const val ADD_REMINDER = "ADD_REMINDER"
        const val ADD_REMINDER_ROUTE = "$ADD_REMINDER/{${Arg.PET_ID_KEY}}"

        const val PET_DETAIL = "PET_DETAIL"
        const val PET_DETAIL_ROUTE = "$PET_DETAIL/{${Arg.PET_ID_KEY}}"
    }

}