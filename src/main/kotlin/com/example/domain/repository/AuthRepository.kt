package com.example.domain.repository

import com.example.domain.model.auth.GAuthUserModel
import com.example.domain.model.auth.TokenItem
import com.example.domain.model.auth.UserItem

interface AuthRepository {
    suspend fun gAuthLogin(gAuthUserModel: GAuthUserModel): TokenItem
    suspend fun reissueAccessToken(refreshToken: String): TokenItem
    suspend fun logout(accessToken: String)
    suspend fun getUserInfo(accessToken: String): UserItem
    suspend fun isTokenValid(accessToken: String): Boolean
}