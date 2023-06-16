package com.example.server

import com.example.api.autoRoute
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        autoRoute()
    }
}