package com.example.data.repository

import com.example.data.model.*
import com.example.domain.model.auth.*
import com.example.server.DatabaseFactory.dbQuery
import com.example.domain.repository.AuthRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {
    override suspend fun gAuthLogin(gAuthUserModel: GAuthUserModel): TokenItem {
        val currentTime = LocalDateTime.now()
        val accessTokenExp = currentTime.plusMinutes(15).toString()
        val refreshTokenExp = currentTime.plusDays(7).toString()

        getGAuthCode(gAuthUserModel.asGAuthUser()).let { oauthCode ->
            getGAuthToken(GAuthTokenRequest(oauthCode.code)).let { token ->
                val userInfo = getGAuthUserInfo(token.accessToken)
                dbQuery {
                    val email = Auths.select { Auths.email eq userInfo.email }.map { it[Auths.email] }.singleOrNull()

                    if(email != null) {
                        Auths.update({ Auths.email eq email}) {
                            it[this.refreshToken] = token.refreshToken
                        }
                    } else {
                        Auths.insert {
                            it[this.refreshToken] = token.refreshToken
                            it[this.email] = gAuthUserModel.email
                            it[this.password] = gAuthUserModel.password
                            it[this.grade] = userInfo.grade.toString()
                            it[this.className] = userInfo.classNum.toString()
                        }
                    }
                }

                return TokenItem(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                    accessTokenExp = accessTokenExp,
                    refreshTokenExp = refreshTokenExp
                )
            }
        }
    }

    override suspend fun reissueAccessToken(refreshToken: String): TokenItem {
        val currentTime = LocalDateTime.now()
        val accessTokenExp = currentTime.plusMinutes(15).toString()
        val refreshTokenExp = currentTime.plusDays(7).toString()

        reissueGAuthToken(refreshToken).let { tokens ->
            return TokenItem(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
                accessTokenExp = accessTokenExp,
                refreshTokenExp = refreshTokenExp
            )
        }
    }

    override suspend fun logout(accessToken: String) {
        val email = getGAuthUserInfo(accessToken).email
        dbQuery {
            Auths.update({ Auths.email eq email }) {
                it[refreshToken] = ""
            }
        }
    }

    override suspend fun getUserInfo(accessToken: String): UserItem {
        getGAuthUserInfo(accessToken).let { user ->
            val grade = user.grade.toString()
            val className = user.classNum.toString()

            return UserItem(
                grade = grade,
                className = className
            )
        }
    }

    private suspend fun getGAuthCode(gAuthUser: GAuthUser): GAuthCode = dbQuery {
        val response = client.post("https://server.gauth.co.kr/oauth/code") {
            contentType(ContentType.Application.Json)
            setBody(gAuthUser)
        }.body<GAuthCode>()
        println(response.code)
        response
    }

    private suspend fun getGAuthToken(gAuthTokenRequest: GAuthTokenRequest): GAuthTokenResponse = dbQuery {
        client.post("https://server.gauth.co.kr/oauth/token") {
            contentType(ContentType.Application.Json)
            setBody(gAuthTokenRequest)
        }.body()
    }

    private suspend fun reissueGAuthToken(refreshToken: String): GAuthTokenResponse = dbQuery {
        client.patch("https://server.gauth.co.kr/oauth/token") {
            contentType(ContentType.Application.Json)
            headers { append("refreshToken", "Bearer $refreshToken") }
        }.body()
    }

    private suspend fun getGAuthUserInfo(accessToken: String): GAuthUserInfo = dbQuery {
        client.get("https://open.gauth.co.kr/user") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
        }.body()
    }
}