package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GAuthTokenRequest(
    val code: String,
    val clientId: String = "3c4a659279364866ae1f3b3c5cd0e26e2b08fac490a743b7898264cdcc5ca3d3",
    val clientSecret: String = "f48db76ba6d045698b26a9791504d6a5965766fb551b49a39b0a67f3a0bce169",
    val redirectUri: String = "http://localhost:3000"
)
