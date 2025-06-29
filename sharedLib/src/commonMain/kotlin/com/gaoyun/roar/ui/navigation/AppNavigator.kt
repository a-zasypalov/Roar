package com.gaoyun.roar.ui.navigation

import androidx.navigation.NavType
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.ui.features.add_pet.avatar.ToPetData
import com.gaoyun.roar.ui.features.add_pet.pet_type.ToPetAvatar
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

class AppNavigator(
    private val closeAppActionHandler: CloseAppActionHandler
) : KoinComponent {

    fun navigate(call: NavigationSideEffect): NavigationAction? = when (call) {
        is ToUserRegistration -> NavigationAction.NavigateToPath(NavigationKeys.Route.REGISTER_USER_ROUTE)
        is ToAddPet -> NavigationAction.NavigateToPath(NavigationKeys.Route.ADD_PET_ROUTE)
        is ToAddReminder -> NavigationAction.NavigateTo(AddReminderArgs(call.petId))
        is ToPetScreen -> NavigationAction.NavigateTo(PetDetailArgs(call.petId))
        is ToInteractionDetails -> NavigationAction.NavigateTo(InteractionDetailArgs(call.interactionId))
        is ToUserScreen -> NavigationAction.NavigateToPath(NavigationKeys.Route.USER_ROUTE)
        is ToPetAdding -> NavigationAction.PopToPath(NavigationKeys.Route.HOME_ROUTE, inclusive = false)
        is ToPetAvatar -> NavigationAction.NavigateTo(AddPetAvatarArgs(call.petType))
        is ToPetData -> NavigationAction.NavigateTo(AddPetDataArgs(call.petType, call.avatar))
        is ToAvatarEdit -> NavigationAction.NavigateTo(PetEditAvatarArgs(call.petType.name, call.petId))
        is ToPetSetup -> NavigationAction.NavigateTo(AddPetSetupArgs(call.petId))
        is FinishPetSetup -> NavigationAction.PopToPath(NavigationKeys.Route.ADD_PET_ROUTE, inclusive = true)
        is OpenTemplates -> NavigationAction.NavigateTo(AddReminderArgs(call.petId))
        is ToUserEdit -> NavigationAction.NavigateToPath(NavigationKeys.Route.USER_EDIT_ROUTE)
        is ToAboutScreen -> NavigationAction.NavigateToPath(NavigationKeys.Route.ABOUT_ROUTE)
        is ToInteractionTemplates -> NavigationAction.NavigateTo(AddReminderArgs(call.petId))
        is BackToTemplates -> NavigationAction.PopTo(AddReminderArgs(call.petId), inclusive = false)
        is FinishReminderAdding -> NavigationAction.PopTo(AddReminderArgs(call.petId), inclusive = false)

        is ToEditPet -> NavigationAction.NavigateTo(
            PetEditArgs(
                petId = call.pet.id,
                avatar = call.pet.avatar,
                petType = call.pet.petType.name
            )
        )

        is ToReminderSetup -> NavigationAction.NavigateTo(
            SetupReminderArgs(
                petId = call.petId,
                templateId = call.templateId
            )
        )

        is ToComplete -> NavigationAction.NavigateTo(
            SetupReminderCompleteArgs(
                petId = call.petId,
                templateId = call.templateId,
                avatar = call.petAvatar
            )
        )


        is ToEditInteraction -> NavigationAction.NavigateTo(
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
}

class ToAvatarEdit(val petId: String, val petType: PetType) : NavigationSideEffect
class ToPetSetup(val petId: String) : NavigationSideEffect
object ToUserRegistration : NavigationSideEffect
object ToAddPet : NavigationSideEffect
class ToAddReminder(val petId: String) : NavigationSideEffect
class ToPetScreen(val petId: String) : NavigationSideEffect
class ToInteractionDetails(val interactionId: String) : NavigationSideEffect
object ToUserScreen : NavigationSideEffect
object FinishPetSetup : NavigationSideEffect
data class OpenTemplates(val petId: String) : NavigationSideEffect
object ToUserEdit : NavigationSideEffect
object ToAboutScreen : NavigationSideEffect
class ToInteractionTemplates(val petId: String) : NavigationSideEffect
class ToEditPet(val pet: Pet) : NavigationSideEffect
object ToPetAdding : NavigationSideEffect
class BackToTemplates(val petId: String) : NavigationSideEffect
class ToComplete(val petAvatar: String, val petId: String, val templateId: String) : NavigationSideEffect
class ToEditInteraction(val petId: String, val interaction: InteractionWithReminders) : NavigationSideEffect
class ToReminderSetup(val petId: String, val templateId: String) : NavigationSideEffect
class FinishReminderAdding(val petId: String) : NavigationSideEffect