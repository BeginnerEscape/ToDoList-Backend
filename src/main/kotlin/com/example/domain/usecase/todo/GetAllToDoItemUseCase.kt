package com.example.domain.usecase.todo

import com.example.domain.repository.ToDoItemRepository

class GetAllToDoItemUseCase(private val repository: ToDoItemRepository) {
    suspend operator fun invoke() = repository.getAllToDoItem()
}