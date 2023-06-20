package com.example.server

import com.example.domain.model.auth.Auths
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:~/todolist"
        val database = Database.connect(
            url = jdbcURL,
            driver = driverClassName,
            user = "sa",
            password = "sa"
        )

        transaction(database) {
            SchemaUtils.create(Auths)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}