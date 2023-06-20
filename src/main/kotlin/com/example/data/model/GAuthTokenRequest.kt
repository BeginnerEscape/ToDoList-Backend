package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GAuthTokenRequest(
    val code: String,
    val clientId: String = System.getenv("CLIENT_ID"),
    val clientSecret: String = System.getenv("CLIENT_SECRET"),
    val redirectUri: String = System.getenv("REDIRECT_URI")
)
