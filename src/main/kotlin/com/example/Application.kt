package com.example

import com.example.di.installKoin
import com.example.server.DatabaseFactory
import com.example.server.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.server.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init(environment.config)
    configureSerialization()
    configureRouting()
    installKoin()
}
