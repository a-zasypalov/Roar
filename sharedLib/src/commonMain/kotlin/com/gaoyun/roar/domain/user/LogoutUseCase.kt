package com.gaoyun.roar.domain.user

import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import com.gaoyun.roar.util.SignOutExecutor
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException

class LogoutUseCase(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getPetUseCase: GetPetUseCase,
    private val removePetUseCase: RemovePetUseCase,
    private val removeInteraction: RemoveInteraction,
    private val userRepository: UserRepository,
    private val prefs: Preferences,
    private val signOutExecutor: SignOutExecutor
) {

    fun logout() = flow {
        try {
            val currentUserId = getCurrentUserUseCase.getCurrentUser().firstOrNull()?.id ?: return@flow
            userRepository.deleteUsers()

            val petIds = getPetUseCase.getPetByUserId(currentUserId).firstOrNull() ?: listOf()
            petIds.forEach {
                removeInteraction.removeInteractionByPet(it.id).firstOrNull()
                removePetUseCase.removePet(it.id).firstOrNull()
            }

            prefs.apply {
                setString(PreferencesKeys.CURRENT_USER_ID, "")
                setLong(PreferencesKeys.LAST_SYNCHRONISED_TIMESTAMP, 0)
            }

            signOutExecutor.signOut()

            emit(true)
        } catch (e: SerializationException) {
            e.printStackTrace()
            emit(false)
        }
    }

}