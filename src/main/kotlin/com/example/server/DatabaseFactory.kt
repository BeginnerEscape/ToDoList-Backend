package com.example.server

import io.ktor.server.config.*

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("storage.driverClassName").getString()
    }
}