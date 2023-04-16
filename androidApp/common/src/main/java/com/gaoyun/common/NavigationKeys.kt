package com.gaoyun.common

import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_pet.avatar.AddPetAvatarScreenContract
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenContract
import com.gaoyun.roar.presentation.add_pet.type.AddPetPetTypeScreenContract
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.home_screen.HomeScreenContract
import com.gaoyun.roar.presentation.interactions.InteractionScreenContract
import com.gaoyun.roar.presentation.pet_screen.PetScreenContract
import com.gaoyun.roar.presentation.user_register.RegisterUserScreenContract
import com.gaoyun.roar.presentation.user_screen.UserScreenContract

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
        const val USER_ROUTE = "$HOME_ROUTE/${USER}"
        const val USER_EDIT_ROUTE = "$HOME_ROUTE/${USER}/EDIT"

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

sealed class NavigationAction {
    class NavigateTo(val path: String) : NavigationAction()
    object NavigateBack : NavigationAction()
    class PopTo(val path: String) : NavigationAction()

}

object AppNavigator {
    fun navigate(call: NavigationSideEffect) = when (call) {
        is HomeScreenContract.Effect.Navigation.ToUserRegistration -> toUserRegistration()
        is HomeScreenContract.Effect.Navigation.ToAddPet -> toAddPet()
        is HomeScreenContract.Effect.Navigation.ToAddReminder -> toAddReminder(call)
        is HomeScreenContract.Effect.Navigation.ToPetScreen -> toPetScreen(call)
        is HomeScreenContract.Effect.Navigation.ToInteractionDetails -> toInteractionDetails(call)
        is HomeScreenContract.Effect.Navigation.ToEditPet -> toEditPet(call)
        is HomeScreenContract.Effect.Navigation.ToUserScreen -> toUserScreen()

        is RegisterUserScreenContract.Effect.Navigation.ToPetAdding -> toPetAdding()
        is AddPetPetTypeScreenContract.Effect.Navigation.ToPetAvatar -> toPetAvatar(call)
        is AddPetAvatarScreenContract.Effect.Navigation.ToPetData -> toPetData(call)
        is AddPetDataScreenContract.Effect.Navigation.ToAvatarEdit -> toAvatarEdit(call)
        is AddPetDataScreenContract.Effect.Navigation.ToPetSetup -> toPetSetup(call)
        is AddPetSetupScreenContract.Effect.Navigation.Continue -> finishPetSetup()

        is UserScreenContract.Effect.Navigation.ToUserEdit -> toUserEdit()

        is PetScreenContract.Effect.Navigation.ToInteractionDetails -> toInteractionDetails(call)
        is PetScreenContract.Effect.Navigation.ToInteractionTemplates -> toInteractionTemplates(call)
        is PetScreenContract.Effect.Navigation.ToEditPet -> toEditPet(call)

        is AddReminderScreenContract.Effect.Navigation.ToReminderSetup -> toReminderSetup(call)
        is SetupReminderScreenContract.Effect.Navigation.ToComplete -> toCompleteInteractionSetup(call)
        is AddReminderCompleteScreenContract.Effect.Navigation.Continue -> completeReminderCreation()

        is InteractionScreenContract.Effect.Navigation.ToEditInteraction -> toEditInteraction(call)
        else -> null
    }

    private fun toUserRegistration() = NavigationAction.NavigateTo(NavigationKeys.Route.REGISTER_USER_ROUTE)
    private fun toAddPet() = NavigationAction.NavigateTo(NavigationKeys.Route.ADD_PET_ROUTE)
    private fun toAddReminder(effect: HomeScreenContract.Effect.Navigation.ToAddReminder) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.ADD_REMINDER}/${effect.petId}")

    private fun toPetScreen(effect: HomeScreenContract.Effect.Navigation.ToPetScreen) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.PET_DETAIL}/${effect.petId}")

    private fun toInteractionDetails(effect: HomeScreenContract.Effect.Navigation.ToInteractionDetails) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.INTERACTION_DETAIL}/${effect.interactionId}")

    private fun toEditPet(effect: HomeScreenContract.Effect.Navigation.ToEditPet) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.EDIT}/${NavigationKeys.Route.PET_DETAIL}/${effect.pet.id}/${effect.pet.avatar}/${effect.pet.petType}")

    private fun toUserScreen() =
        NavigationAction.NavigateTo("${NavigationKeys.Route.HOME_ROUTE}/${NavigationKeys.Route.USER}")

    private fun toPetAdding() = NavigationAction.NavigateTo(NavigationKeys.Route.HOME_ROUTE)
    private fun toPetAvatar(effect: AddPetPetTypeScreenContract.Effect.Navigation.ToPetAvatar) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.ADD_PET_ROUTE}/${effect.petType}")

    private fun toPetData(effect: AddPetAvatarScreenContract.Effect.Navigation.ToPetData) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.ADD_PET_ROUTE}/${effect.petType}/${effect.avatar}")

    private fun toAvatarEdit(effect: AddPetDataScreenContract.Effect.Navigation.ToAvatarEdit) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.EDIT}/${NavigationKeys.Route.AVATAR}/${NavigationKeys.Route.PET_DETAIL}/${effect.petType}/${effect.petId}")

    private fun toPetSetup(effect: AddPetDataScreenContract.Effect.Navigation.ToPetSetup) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.ADD_PET_SETUP}/${effect.petId}")

    private fun finishPetSetup() = NavigationAction.PopTo(NavigationKeys.Route.ADD_PET_ROUTE)

    private fun toUserEdit() = NavigationAction.NavigateTo(NavigationKeys.Route.USER_EDIT_ROUTE)

    private fun toInteractionDetails(effect: PetScreenContract.Effect.Navigation.ToInteractionDetails) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.INTERACTION_DETAIL}/${effect.interactionId}")

    private fun toInteractionTemplates(effect: PetScreenContract.Effect.Navigation.ToInteractionTemplates) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.ADD_REMINDER}/${effect.petId}")

    private fun toEditPet(effect: PetScreenContract.Effect.Navigation.ToEditPet) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.EDIT}/${NavigationKeys.Route.PET_DETAIL}/${effect.pet.id}/${effect.pet.avatar}/${effect.pet.petType}")

    private fun toEditInteraction(effect: InteractionScreenContract.Effect.Navigation.ToEditInteraction) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.EDIT}/${NavigationKeys.Route.ADD_REMINDER}/${effect.petId}/${effect.interaction.templateId}/${effect.interaction.id}")

    private fun toReminderSetup(effect: AddReminderScreenContract.Effect.Navigation.ToReminderSetup) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.ADD_REMINDER}/${effect.petId}/${effect.templateId}")

    private fun toCompleteInteractionSetup(effect: SetupReminderScreenContract.Effect.Navigation.ToComplete) =
        NavigationAction.NavigateTo("${NavigationKeys.Route.ADD_REMINDER}/${effect.petId}/${effect.templateId}/${effect.petAvatar}")

    private fun completeReminderCreation() = NavigationAction.PopTo(NavigationKeys.Route.ADD_REMINDER_ROUTE)
}
