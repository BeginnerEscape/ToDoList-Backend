package com.example.domain.usecase.auth

import com.example.domain.repository.AuthRepository

class ReissueAccessTokenUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(token: String) = repository.reissueAccessToken(token)
}