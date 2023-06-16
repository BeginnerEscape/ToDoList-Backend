package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GAuthUser(
    val email: String,
    val password: String
)
