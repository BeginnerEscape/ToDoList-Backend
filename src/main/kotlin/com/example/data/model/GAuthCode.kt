package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GAuthCode(
    val code: String
)