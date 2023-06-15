package com.example.domain.usecase

import com.example.domain.model.ToDoItem
import com.example.domain.repository.ToDoItemRepository

class UpdateToDoItemUseCase(private val repository: ToDoItemRepository) {
    suspend operator fun invoke(toDoItem: ToDoItem) = repository.updateToDoItem(toDoItem)
}