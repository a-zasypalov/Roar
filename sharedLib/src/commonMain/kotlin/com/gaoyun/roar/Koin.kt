package com.gaoyun.roar

import com.gaoyun.roar.domain.DeleteUserUseCase
import com.gaoyun.roar.domain.GetCurrentUserUseCase
import com.gaoyun.roar.domain.RegisterUserUseCase
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.repository.UserRepositoryImpl
import com.gaoyun.roar.util.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(platformModule(), repositoryModule, useCaseModule)
}

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}

val useCaseModule = module {
    single { RegisterUserUseCase() }
    single { GetCurrentUserUseCase() }
    single { DeleteUserUseCase() }
}