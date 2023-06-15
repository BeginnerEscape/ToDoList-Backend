package com.example.domain.usecase.todo

import com.example.domain.repository.ToDoItemRepository

class GetToDoItemUseCase(private val repository: ToDoItemRepository) {
    suspend operator fun invoke(itemId: Int) = repository.getToDoItem(itemId)
}