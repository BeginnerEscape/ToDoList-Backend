package com.example.di

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.installKoin() {
    install(Koin) {
        modules(httpClientModule, usecaseModule, repositoryModule)
    }
}