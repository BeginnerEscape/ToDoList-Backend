package com.example.data.repository

import com.example.data.model.*
import com.example.domain.model.auth.*
import com.example.server.DatabaseFactory.dbQuery
import com.example.domain.repository.AuthRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {
    override suspend fun gAuthLogin(gAuthUserModel: GAuthUserModel): TokenItem {
        val currentTime = LocalDateTime.now()
        val accessTokenExp = currentTime.plusMinutes(15).toString()
        val refreshTokenExp = currentTime.plusDays(7).toString()

        getGAuthCode(gAuthUserModel.asGAuthUser()).let { oauthCode ->
            getGAuthToken(GAuthTokenRequest(oauthCode.code)).let { token ->
                dbQuery {
                    Auths.insert {
                        it[this.email] = gAuthUserModel.email
                        it[this.password] = gAuthUserModel.password
                        it[this.accessToken] = token.accessToken
                        it[this.refreshToken] = token.refreshToken
                        it[this.accessTokenExp] = accessTokenExp
                        it[this.refreshTokenExp] = refreshTokenExp
                        it[this.grade] = "3" // 임시 더미 데이터
                        it[this.className] = "2" // 임시 더미 데이터
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
            dbQuery {
                Auths.update({ Auths.refreshToken eq refreshToken }) {
                    it[this.accessToken] = tokens.accessToken
                    it[this.refreshToken] = tokens.refreshToken
                    it[this.accessTokenExp] = accessTokenExp
                    it[this.refreshTokenExp] = refreshTokenExp
                }
            }

            return TokenItem(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
                accessTokenExp = accessTokenExp,
                refreshTokenExp = refreshTokenExp
            )
        }
    }

    override suspend fun logout(accessToken: String) {
        dbQuery { Auths.deleteWhere { this.accessToken eq accessToken } }
    }

    override suspend fun getUserInfo(accessToken: String): UserItem {
        getGAuthUserInfo(accessToken).let { user ->
            val grade = user.grade.toString()
            val className = user.classNum.toString()

            dbQuery {
                Auths.insert {
                    it[this.grade] = grade
                    it[this.className] = className
                }
            }

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