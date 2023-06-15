package com.example.di

import com.example.domain.usecase.auth.GAuthLoginUseCase
import com.example.domain.usecase.auth.GetUserInfoUseCase
import com.example.domain.usecase.auth.LogoutUseCase
import com.example.domain.usecase.auth.ReissueAccessTokenUseCase
import com.example.domain.usecase.todo.*
import org.koin.dsl.module

val usecaseModule = module {
    // auth
    single { GAuthLoginUseCase(get()) }
    single { GetUserInfoUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { ReissueAccessTokenUseCase(get()) }

    // todo
    single { DeleteToDoItemUseCase(get()) }
    single { GetAllToDoItemUseCase(get()) }
    single { GetToDoItemUseCase(get()) }
    single { InsertToDoItemUseCase(get()) }
    single { UpdateToDoItemUseCase(get()) }
}