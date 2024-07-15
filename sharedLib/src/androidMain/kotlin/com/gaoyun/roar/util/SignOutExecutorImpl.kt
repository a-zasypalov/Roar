package com.gaoyun.roar.util

import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class SignOutExecutorImpl : SignOutExecutor {
    override fun signOut() {
        Firebase.auth.signOut()
    }
}