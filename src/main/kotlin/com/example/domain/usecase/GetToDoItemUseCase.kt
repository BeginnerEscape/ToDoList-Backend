package com.example.domain.usecase

import com.example.domain.repository.ToDoItemRepository

class GetToDoItemUseCase(private val repository: ToDoItemRepository) {
    suspend operator fun invoke(itemId: Int) = repository.getToDoItem(itemId)
}