package com.gaoyun.roar.domain.user

import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.PreferencesKeys

class CheckUserExistingUseCase(private val prefs: Preferences) {

    fun isUserExisting(): Boolean {
        return prefs.getString(PreferencesKeys.CURRENT_USER_ID, "").isNotEmpty()
    }

}