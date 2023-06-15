package com.example.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenItem(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExp: String,
    val refreshTokenExp: String
)
