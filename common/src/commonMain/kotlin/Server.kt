import config.configureRouting
import config.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import sql.Categories
import sql.Subcategories
import sql.Transactions
import sql.databaseConnection

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    databaseConnection
    transaction {
        SchemaUtils.create(Categories, Subcategories, Transactions)
    }
    configureRouting()
    configureSerialization()
}