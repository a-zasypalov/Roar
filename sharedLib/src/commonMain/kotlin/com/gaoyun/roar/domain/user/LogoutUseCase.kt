package com.gaoyun.roar.domain.user

import com.gaoyun.roar.domain.interaction.RemoveInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogoutUseCase : KoinComponent {

    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val getPetUseCase: GetPetUseCase by inject()

    private val removePetUseCase: RemovePetUseCase by inject()
    private val removeInteraction: RemoveInteraction by inject()

    private val userRepository: UserRepository by inject()
    private val prefs: Preferences by inject()

    fun logout() = flow {
        try {
            val currentUserId = getCurrentUserUseCase.getCurrentUser().firstOrNull()?.id ?: ""
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

            emit(true)
        } catch (e: SerializationException) {
            e.printStackTrace()
            emit(false)
        }
    }

}