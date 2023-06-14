package com.example.domain.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class ToDoItem(
    val itemId: Int,
    val content: String
)

object ToDoItems: Table() {
    val itemId = integer("item_id").autoIncrement()
    val content = varchar("content", 30)

    override val primaryKey = PrimaryKey(itemId)
}
