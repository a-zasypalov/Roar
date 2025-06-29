package com.gaoyun.roar.ui.features.user.edit_user

import com.gaoyun.roar.model.domain.User

class EditUserScreenContract {

    sealed class Event {
        class OnSaveAccountClick(val user: User) : Event()
    }
}