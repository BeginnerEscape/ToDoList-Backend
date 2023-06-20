package com.example.di

import com.example.domain.usecase.auth.*
import com.example.domain.usecase.todo.*
import org.koin.dsl.module

val usecaseModule = module {
    // auth
    single { GAuthLoginUseCase(get()) }
    single { GetUserInfoUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { ReissueAccessTokenUseCase(get()) }
    single { IsTokenValidUseCase(get()) }

    // todo
    single { DeleteToDoItemUseCase(get()) }
    single { GetAllToDoItemUseCase(get()) }
    single { GetToDoItemUseCase(get()) }
    single { InsertToDoItemUseCase(get()) }
    single { UpdateToDoItemUseCase(get()) }
}