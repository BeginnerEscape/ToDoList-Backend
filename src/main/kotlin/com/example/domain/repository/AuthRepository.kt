package com.example.domain.repository

import com.example.domain.model.auth.TokenItem
import com.example.domain.model.auth.UserItem

interface AuthRepository {
    suspend fun gAuthLogin(code: String): TokenItem
    suspend fun reissueAccessToken(token: String): TokenItem
    suspend fun logout(token: String)
    suspend fun getUserInfo(token: String): UserItem
}