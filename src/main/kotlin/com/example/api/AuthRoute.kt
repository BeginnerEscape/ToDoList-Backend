package com.example.api

import com.example.domain.model.auth.GAuthUserModel
import com.example.domain.usecase.auth.*
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
    val isTokenValidUseCase: IsTokenValidUseCase by inject()

    route("/auth") {
        post {
            val gAuthUserModel = call.receive<GAuthUserModel>()
            val tokenItem = gAuthLoginUseCase(GAuthUserModel(gAuthUserModel.email, gAuthUserModel.password))
            call.respond(tokenItem)
        }

        get {
            val accessToken = call.request.headers["Authorization"] ?: return@get call.respondText("BadRequest AccessToken", status = HttpStatusCode.BadRequest)
            if (isTokenValidUseCase(accessToken)) {
                val userInfo = getUserInfoUseCase(accessToken)
                call.respond(userInfo)
            } else {
                return@get call.respondText("Unauthorized AccessToken", status = HttpStatusCode.Unauthorized)
            }
        }

        delete {
            val accessToken = call.request.headers["Authorization"] ?: return@delete call.respondText("BadRequest AccessToken", status = HttpStatusCode.BadRequest)
            if (isTokenValidUseCase(accessToken)) {
                logoutUseCase(accessToken)
                call.respondText("Logout correctly", status = HttpStatusCode.OK)
            } else {
                return@delete call.respondText("Unauthorized AccessToken", status = HttpStatusCode.Unauthorized)
            }
        }

        patch {
            val refreshToken = call.request.headers["refreshToken"] ?: return@patch call.respondText("BadRequest RefreshToken", status = HttpStatusCode.BadRequest)
            val tokenItem = reissueAccessTokenUseCase(refreshToken)
            call.respond(tokenItem)
        }
    }
}