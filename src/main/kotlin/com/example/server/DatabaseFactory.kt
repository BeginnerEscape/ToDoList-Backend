package com.example.server

import com.example.domain.model.auth.Auths
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val database = Database.connect(
            url = System.getenv("DRIVER_CLASS_NAME"),
            driver = System.getenv("JDBC_URL"),
            user = System.getenv("USER"),
            password = System.getenv("PASSWORD")
        )

        transaction(database) {
            SchemaUtils.create(Auths)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}