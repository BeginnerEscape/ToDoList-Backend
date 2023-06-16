package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GAuthTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
