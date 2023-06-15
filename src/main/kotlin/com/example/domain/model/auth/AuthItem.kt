package com.example.domain.model.auth

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class AuthItem(
    val userId: Int,
    val tokenItem: TokenItem,
    val userItem: UserItem
)

object Auths: Table() {
    val userid = integer("user_id").autoIncrement()
    val accessToken = varchar("access_token", 50)
    val refreshToken = varchar("refresh_token", 50)
    val accessTokenExp = varchar("access_token_exp", 20)
    val refreshTokenExp = varchar("refresh_token_exp", 20)
    val grade = varchar("grade", 1)
    val className = varchar("class_name", 1)

    override val primaryKey = PrimaryKey(userid)
}
