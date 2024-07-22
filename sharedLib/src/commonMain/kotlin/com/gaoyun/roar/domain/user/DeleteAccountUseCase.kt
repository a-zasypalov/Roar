package com.gaoyun.roar.domain.user

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class DeleteAccountUseCase(
    private val logoutUseCase: LogoutUseCase,
    private val deleteRemoteAccountExecutor: DeleteRemoteAccountExecutor
) {

    fun deleteAccount() = flow {
        deleteRemoteAccountExecutor.deleteAccount()
        emit(logoutUseCase.logout().firstOrNull())
    }

}