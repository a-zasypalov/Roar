package com.gaoyun.roar.ui.navigation

import androidx.navigation.NavType
import com.gaoyun.roar.presentation.add_pet.ToPetData
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenContract
import com.gaoyun.roar.presentation.add_pet.ToPetAvatar
import com.gaoyun.roar.presentation.add_pet.data.ToAvatarEdit
import com.gaoyun.roar.presentation.add_pet.data.ToPetSetup
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.home_screen.HomeScreenContract
import com.gaoyun.roar.presentation.interactions.InteractionScreenContract
import com.gaoyun.roar.presentation.pet_screen.PetScreenContract
import com.gaoyun.roar.presentation.user_register.RegisterUserScreenContract
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import org.koin.core.component.KoinComponent
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface NavigationSideEffect
object BackNavigationEffect : NavigationSideEffect
object CloseAppNavigationSideEffect : NavigationSideEffect


val appArgsTypeMap: Map<KType, NavType<*>> = mapOf(
    typeOf<AddPetAvatarArgs>() to serializableNavType<AddPetAvatarArgs>(),
    typeOf<AddPetDataArgs>() to serializableNavType<AddPetDataArgs>(),
    typeOf<AddPetSetupArgs>() to serializableNavType<AddPetSetupArgs>(),
    typeOf<AddReminderArgs>() to serializableNavType<AddReminderArgs>(),
    typeOf<SetupReminderArgs>() to serializableNavType<SetupReminderArgs>(),
    typeOf<EditReminderArgs>() to serializableNavType<EditReminderArgs>(),
    typeOf<SetupReminderCompleteArgs>() to serializableNavType<SetupReminderCompleteArgs>(),
    typeOf<PetEditArgs>() to serializableNavType<PetEditArgs>(),
    typeOf<PetEditAvatarArgs>() to serializableNavType<PetEditAvatarArgs>(),
    typeOf<PetDetailArgs>() to serializableNavType<PetDetailArgs>(),
    typeOf<InteractionDetailArgs>() to serializableNavType<InteractionDetailArgs>()
)

class AppNavigator(private val closeAppActionHandler: CloseAppActionHandler) : KoinComponent {
    fun navigate(call: NavigationSideEffect): NavigationAction? = when (call) {
        is HomeScreenContract.Effect.Navigation.ToUserRegistration -> toUserRegistration()
        is HomeScreenContract.Effect.Navigation.ToAddPet -> toAddPet()
        is HomeScreenContract.Effect.Navigation.ToAddReminder -> toAddReminder(call)
        is HomeScreenContract.Effect.Navigation.ToPetScreen -> toPetScreen(call)
        is HomeScreenContract.Effect.Navigation.ToInteractionDetails -> toInteractionDetails(call)
        is HomeScreenContract.Effect.Navigation.ToEditPet -> toEditPet(call)
        is HomeScreenContract.Effect.Navigation.ToUserScreen -> toUserScreen()

        is RegisterUserScreenContract.Effect.Navigation.ToPetAdding -> toPetAdding()
        is ToPetAvatar -> NavigationAction.NavigateTo(AddPetAvatarArgs(call.petType))
        is ToPetData -> NavigationAction.NavigateTo(AddPetDataArgs(call.petType, call.avatar))
        is ToAvatarEdit -> NavigationAction.NavigateTo(PetEditAvatarArgs(call.petType.name, call.petId))
        is ToPetSetup -> NavigationAction.NavigateTo(AddPetSetupArgs(call.petId))
        is AddPetSetupScreenContract.Effect.Navigation.Continue -> finishPetSetup()
        is AddPetSetupScreenContract.Effect.Navigation.OpenTemplates -> NavigationAction.NavigateTo(AddReminderArgs(call.petId))

        is UserScreenContract.Effect.Navigation.ToUserEdit -> toUserEdit()
        is UserScreenContract.Effect.Navigation.ToAboutScreen -> toAboutScreen()

        is PetScreenContract.Effect.Navigation.ToInteractionDetails -> NavigationAction.NavigateTo(InteractionDetailArgs(call.interactionId))
        is PetScreenContract.Effect.Navigation.ToInteractionTemplates -> NavigationAction.NavigateTo(AddReminderArgs(call.petId))
        is PetScreenContract.Effect.Navigation.ToEditPet -> NavigationAction.NavigateTo(
            PetEditArgs(
                petId = call.pet.id,
                avatar = call.pet.avatar,
                petType = call.pet.petType.name
            )
        )

        is AddReminderScreenContract.Effect.Navigation.ToReminderSetup -> NavigationAction.NavigateTo(
            SetupReminderArgs(
                petId = call.petId,
                templateId = call.templateId
            )
        )

        is SetupReminderScreenContract.Effect.Navigation.ToComplete -> NavigationAction.NavigateTo(
            SetupReminderCompleteArgs(
                petId = call.petId,
                templateId = call.templateId,
                avatar = call.petAvatar
            )
        )

        is SetupReminderScreenContract.Effect.Navigation.BackToTemplates -> completeReminderCreation()
        is AddReminderCompleteScreenContract.Effect.Navigation.Continue -> completeReminderCreation()

        is InteractionScreenContract.Effect.Navigation.ToEditInteraction -> NavigationAction.NavigateTo(
            EditReminderArgs(
                petId = call.petId,
                templateId = call.interaction.templateId,
                interactionId = call.interaction.id
            )
        )

        is CloseAppNavigationSideEffect -> {
            closeAppActionHandler.closeApp(); null
        }

        else -> null
    }

    private fun toUserRegistration() =
        NavigationAction.NavigateTo(NavigationKeys.Route.REGISTER_USER_ROUTE)

    private fun toAddPet() =
        NavigationAction.NavigateTo(NavigationKeys.Route.ADD_PET_ROUTE)

    private fun toAddReminder(effect: HomeScreenContract.Effect.Navigation.ToAddReminder) =
        NavigationAction.NavigateTo(AddReminderArgs(effect.petId))

    private fun toPetScreen(effect: HomeScreenContract.Effect.Navigation.ToPetScreen) =
        NavigationAction.NavigateTo(PetDetailArgs(effect.petId))

    private fun toInteractionDetails(effect: HomeScreenContract.Effect.Navigation.ToInteractionDetails) =
        NavigationAction.NavigateTo(InteractionDetailArgs(effect.interactionId))

    private fun toEditPet(effect: HomeScreenContract.Effect.Navigation.ToEditPet) =
        NavigationAction.NavigateTo(
            PetEditArgs(
                petId = effect.pet.id,
                avatar = effect.pet.avatar,
                petType = effect.pet.petType.name
            )
        )

    private fun toUserScreen() =
        NavigationAction.NavigateTo(NavigationKeys.Route.USER_ROUTE)

    private fun toPetAdding() =
        NavigationAction.PopTo(NavigationKeys.Route.HOME_ROUTE, inclusive = false)

    private fun finishPetSetup() =
        NavigationAction.PopTo(NavigationKeys.Route.ADD_PET_ROUTE, inclusive = true)

    private fun toUserEdit() =
        NavigationAction.NavigateTo(NavigationKeys.Route.USER_EDIT_ROUTE)

    private fun toAboutScreen() =
        NavigationAction.NavigateTo(NavigationKeys.Route.ABOUT_ROUTE)

    private fun completeReminderCreation() =
        NavigationAction.PopTo(NavigationKeys.Route.ADD_REMINDER_ROUTE, inclusive = false)
}