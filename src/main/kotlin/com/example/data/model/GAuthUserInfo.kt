package com.example.data.model

data class GAuthUserInfo(
    val email: String,
    val name: String?,
    val grade: Int?, // 학년
    val classNum: Int?, // 반
    val num: Int?, // 번호
    val gender: String,
    val profileUrl: String?,
    val role: String
)
