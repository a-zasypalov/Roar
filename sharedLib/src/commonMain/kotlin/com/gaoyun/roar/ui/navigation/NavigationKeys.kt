package com.gaoyun.roar.ui.navigation

import kotlinx.serialization.Serializable

object NavigationKeys {
    object Route {
        const val ONBOARDING_ROUTE = "ONBOARDING"
        const val REGISTER_USER_ROUTE = "REGISTER_USER"
        const val HOME_ROUTE = "HOME"
        const val ADD_PET_ROUTE = "$HOME_ROUTE/ADD_PET"
        const val USER_ROUTE = "$HOME_ROUTE/USER"
        const val USER_EDIT_ROUTE = "$USER_ROUTE/EDIT"
        const val ABOUT_ROUTE = "$USER_ROUTE/ABOUT"
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
