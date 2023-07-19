package com.example.di

import com.example.data.repository.AuthRepositoryImpl
import com.example.domain.repository.AuthRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}