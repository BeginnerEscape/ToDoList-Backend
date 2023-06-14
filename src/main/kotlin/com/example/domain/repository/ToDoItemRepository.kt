package com.example.domain.repository

import com.example.domain.model.ToDoItem

interface ToDoItemRepository {
    suspend fun insertToDoItem(toDoItem: ToDoItem)
    suspend fun getAllToDoItem(): List<ToDoItem>
    suspend fun getToDoItem(itemId: Int): ToDoItem?
    suspend fun updateToDoItem(toDoItem: ToDoItem)
    suspend fun deleteToDoItem(itemId: Int)
}