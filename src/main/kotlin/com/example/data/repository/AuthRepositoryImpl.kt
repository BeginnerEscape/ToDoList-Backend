package com.example.data.repository

import com.example.data.model.GAuthTokenRequest
import com.example.data.model.GAuthTokenResponse
import com.example.data.model.GAuthUser
import com.example.data.model.GAuthUserInfo
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

        getGAuthCode(gAuthUserModel.asGAuthUser()).let { code ->
            getGAuthToken(GAuthTokenRequest(code)).let { token ->
                dbQuery {
                    Auths.insert {
                        it[accessToken] = token.accessToken
                        it[refreshToken] = token.refreshToken
                        it[Auths.accessTokenExp] = accessTokenExp
                        it[Auths.refreshTokenExp] = refreshTokenExp
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
                    it[accessToken] = tokens.accessToken
                    it[this.refreshToken] = tokens.refreshToken
                    it[Auths.accessTokenExp] = accessTokenExp
                    it[Auths.refreshTokenExp] = refreshTokenExp
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
                    it[Auths.grade] = grade
                    it[Auths.className] = className
                }
            }

            return UserItem(
                grade = grade,
                className = className
            )
        }
    }

    private suspend fun getGAuthCode(gAuthUser: GAuthUser): String = dbQuery {
        client.post("https://server.gauth.co.kr/oauth/code") {
            setBody(gAuthUser)
        }.body()
    }

    private suspend fun getGAuthToken(gAuthTokenRequest: GAuthTokenRequest): GAuthTokenResponse = dbQuery {
        client.post("https://server.gauth.co.kr/oauth/token") {
            setBody(gAuthTokenRequest)
        }.body()
    }

    private suspend fun reissueGAuthToken(refreshToken: String): GAuthTokenResponse = dbQuery {
        client.patch("https://server.gauth.co.kr/oauth/token") {
            headers { append("refreshToken", "Bearer $refreshToken") }
        }.body()
    }

    private suspend fun getGAuthUserInfo(accessToken: String): GAuthUserInfo = dbQuery {
        client.get("https://open.gauth.co.kr/user") {
            headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
        }.body()
    }
}