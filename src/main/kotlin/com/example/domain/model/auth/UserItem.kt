package com.example.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserItem(
    val grade: String,
    val className: String
)
