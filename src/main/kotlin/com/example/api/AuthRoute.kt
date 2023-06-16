package com.example.api

import com.example.domain.model.auth.GAuthUserModel
import com.example.domain.usecase.auth.GAuthLoginUseCase
import com.example.domain.usecase.auth.GetUserInfoUseCase
import com.example.domain.usecase.auth.LogoutUseCase
import com.example.domain.usecase.auth.ReissueAccessTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.autoRoute() {
    val gAuthLoginUseCase: GAuthLoginUseCase by inject()
    val getUserInfoUseCase: GetUserInfoUseCase by inject()
    val logoutUseCase: LogoutUseCase by inject()
    val reissueAccessTokenUseCase: ReissueAccessTokenUseCase by inject()

    route("/auth") {
        post {
            val gAuthUserModel = call.receive<GAuthUserModel>()
            val tokenItem = gAuthLoginUseCase(GAuthUserModel(gAuthUserModel.email, gAuthUserModel.password))
            call.respond(tokenItem)
        }

        get {
            val accessToken = call.request.headers["Authorization"] ?: return@get call.respondText("Missing accessToken", status = HttpStatusCode.BadRequest)
            val userInfo = getUserInfoUseCase(accessToken)
            call.respond(userInfo)
        }

        delete {
            val accessToken = call.request.headers["Authorization"] ?: return@delete call.respondText("Missing accessToken", status = HttpStatusCode.BadRequest)
            logoutUseCase(accessToken)
            call.respondText("Logout correctly", status = HttpStatusCode.Accepted)
        }

        patch {
            val refreshToken = call.parameters["refresh_token"] ?: return@patch call.respondText("Missing refreshToken", status = HttpStatusCode.BadRequest)
            val tokenItem = reissueAccessTokenUseCase(refreshToken)
            call.respond(tokenItem)
        }
    }
}