package com.example.domain.model.auth

import com.example.data.model.GAuthUser

data class GAuthUserModel(
    val email: String,
    val password: String
)

fun GAuthUserModel.asGAuthUser() = GAuthUser(
    email = email,
    password = password
)