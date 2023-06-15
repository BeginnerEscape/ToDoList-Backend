package com.example.data.model

data class GAuthTokenRequest(
    val code: String,
    val clientId: String = "",
    val clientSecret: String = "",
    val redirectUri: String = ""
)
