package com.gaoyun.roar.presentation.user_edit

import com.gaoyun.roar.model.domain.User

class EditUserScreenContract {

    sealed class Event {
        class OnSaveAccountClick(val user: User) : Event()
    }
}