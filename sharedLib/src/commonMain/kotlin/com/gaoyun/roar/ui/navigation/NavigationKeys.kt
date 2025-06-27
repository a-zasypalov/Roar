package com.gaoyun.roar.ui.navigation

import kotlinx.serialization.Serializable

object NavigationKeys {

    object Arg {
        const val PET_ID_KEY = "PET_ID_KEY"
        const val PET_TYPE_KEY = "PET_TYPE_KEY"
        const val AVATAR_KEY = "AVATAR_KEY"
        const val TEMPLATE_ID_KEY = "TEMPLATE_ID_KEY"
        const val INTERACTION_ID_KEY = "INTERACTION_ID_KEY"
    }

    object Route {
        const val EDIT = "EDIT"
        const val AVATAR = "AVATAR"
        const val ADD_PET_SETUP = "ADD_PET_SETUP"
        const val USER = "USER"

        const val ONBOARDING_ROUTE = "ONBOARDING"
        const val HOME_ROUTE = "HOME"
        const val REGISTER_USER_ROUTE = "REGISTER_USER"
        const val USER_ROUTE = "$HOME_ROUTE/$USER"
        const val USER_EDIT_ROUTE = "$HOME_ROUTE/$USER/EDIT"
        const val ABOUT_ROUTE = "$HOME_ROUTE/$USER/ABOUT"

        const val ADD_PET_ROUTE = "ADD_PET"
        const val ADD_PET_AVATAR_ROUTE = "$ADD_PET_ROUTE/{${Arg.PET_TYPE_KEY}}"
        const val ADD_PET_DATA_ROUTE = "$ADD_PET_ROUTE/{${Arg.PET_TYPE_KEY}}/{${Arg.AVATAR_KEY}}"
        const val ADD_PET_SETUP_ROUTE = "$ADD_PET_SETUP/{${Arg.PET_ID_KEY}}"

        const val ADD_REMINDER = "ADD_REMINDER"
        const val ADD_REMINDER_ROUTE = "$ADD_REMINDER/{${Arg.PET_ID_KEY}}"
        const val SETUP_REMINDER_ROUTE = "$ADD_REMINDER/{${Arg.PET_ID_KEY}}/{${Arg.TEMPLATE_ID_KEY}}"
        const val EDIT_REMINDER_ROUTE = "$EDIT/$ADD_REMINDER/{${Arg.PET_ID_KEY}}/{${Arg.TEMPLATE_ID_KEY}}/{${Arg.INTERACTION_ID_KEY}}"
        const val SETUP_REMINDER_COMPLETE_ROUTE = "$ADD_REMINDER/{${Arg.PET_ID_KEY}}/{${Arg.TEMPLATE_ID_KEY}}/{${Arg.AVATAR_KEY}}"

        const val PET_DETAIL = "PET_DETAIL"
        const val PET_EDIT_ROUTE = "$EDIT/$PET_DETAIL/{${Arg.PET_ID_KEY}}/{${Arg.AVATAR_KEY}}/{${Arg.PET_TYPE_KEY}}"
        const val PET_EDIT_AVATAR_ROUTE = "$EDIT/$AVATAR/$PET_DETAIL/{${Arg.PET_TYPE_KEY}}/{${Arg.PET_ID_KEY}}"
        const val PET_DETAIL_ROUTE = "$PET_DETAIL/{${Arg.PET_ID_KEY}}"

        const val INTERACTION_DETAIL = "INTERACTION_DETAIL"
        const val INTERACTION_DETAIL_ROUTE = "$INTERACTION_DETAIL/{${Arg.INTERACTION_ID_KEY}}"
    }

}

@Serializable
data class AddPetAvatarArgs(
    val petType: String
)

@Serializable
data class AddPetDataArgs(
    val petType: String,
    val avatar: String
)

@Serializable
data class AddPetSetupArgs(
    val petId: String
)

@Serializable
data class AddReminderArgs(
    val petId: String
)

@Serializable
data class SetupReminderArgs(
    val petId: String,
    val templateId: String
)

@Serializable
data class EditReminderArgs(
    val petId: String,
    val templateId: String?,
    val interactionId: String
)

@Serializable
data class SetupReminderCompleteArgs(
    val petId: String,
    val templateId: String,
    val avatar: String
)

@Serializable
data class PetEditArgs(
    val petId: String,
    val avatar: String,
    val petType: String
)

@Serializable
data class PetEditAvatarArgs(
    val petType: String,
    val petId: String
)

@Serializable
data class PetDetailArgs(
    val petId: String
)

@Serializable
data class InteractionDetailArgs(
    val interactionId: String
)
