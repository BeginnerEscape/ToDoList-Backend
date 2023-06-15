package com.example.domain.usecase.auth

import com.example.domain.repository.AuthRepository

class GetUserInfoUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(token: String) = repository.getUserInfo(token)
}