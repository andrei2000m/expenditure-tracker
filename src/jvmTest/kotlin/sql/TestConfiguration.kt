package sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun configure(): HikariDataSource {
    val configuration: HikariConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:sqlite::memory:"
        maximumPoolSize = 1
    }
    val dataSource = HikariDataSource(configuration)
    Database.connect(dataSource, setupConnection = { connection ->
        connection.createStatement().executeUpdate("PRAGMA foreign_keys = ON")
    })
    return dataSource
}