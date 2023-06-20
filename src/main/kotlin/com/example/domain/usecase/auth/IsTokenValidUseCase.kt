package com.example.domain.usecase.auth

import com.example.domain.repository.AuthRepository

class IsTokenValidUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(accessToken: String) = authRepository.isTokenValid(accessToken)
}