package com.example.domain.usecase.auth

import com.example.domain.repository.AuthRepository

class GAuthLoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(code: String) = repository.gAuthLogin(code)
}