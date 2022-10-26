package com.gaoyun.roar

import com.gaoyun.roar.domain.CheckUserExistingUseCase
import com.gaoyun.roar.domain.GetCurrentUserUseCase
import com.gaoyun.roar.domain.RegisterUserUseCase
import com.gaoyun.roar.model.entity.PetEntity
import com.gaoyun.roar.model.entity.RoarDatabase
import com.gaoyun.roar.repository.UserRepository
import com.gaoyun.roar.repository.UserRepositoryImpl
import com.gaoyun.roar.util.DriverFactory
import com.gaoyun.roar.util.Preferences
import com.gaoyun.roar.util.platformModule
import com.squareup.sqldelight.ColumnAdapter
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(platformModule(), repositoryModule, useCaseModule, dbModule, preferencesModule)
}

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}

val useCaseModule = module {
    single { RegisterUserUseCase() }
    single { GetCurrentUserUseCase() }
    single { CheckUserExistingUseCase() }
}

val dbModule = module {
    single {
        RoarDatabase(
            get<DriverFactory>().createDriver(),
            PetEntityAdapter = PetEntity.Adapter(listOfStringsAdapter)
        )
    }
}

val preferencesModule = module {
    single { Preferences("app_prefs") }
}

val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String) =
        if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(",")
        }

    override fun encode(value: List<String>) = value.joinToString(separator = ",")
}
