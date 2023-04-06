package com.gaoyun.roar.domain.user

import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CheckUserExistingUseCase : KoinComponent {

    private val prefs: Preferences by inject()

    fun isUserExisting(): Boolean {
        return prefs.getString(PreferencesKeys.CURRENT_USER_ID, "").isNotEmpty()
    }

}