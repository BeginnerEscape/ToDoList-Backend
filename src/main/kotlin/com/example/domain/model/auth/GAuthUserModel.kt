package com.example.domain.model.auth

import com.example.data.model.GAuthUser
import kotlinx.serialization.Serializable

@Serializable
data class GAuthUserModel(
    val email: String,
    val password: String
)

fun GAuthUserModel.asGAuthUser() = GAuthUser(
    email = email,
    password = password
)