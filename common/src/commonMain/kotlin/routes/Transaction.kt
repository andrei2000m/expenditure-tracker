package routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import sql.DateSerializer
import sql.insertTransaction
import java.time.LocalDate

fun Route.transactionRouting() {
    route("transaction") {
        post {
            val (subcategoryName, transactionDate, transactionValue, isDebit) = call.receive<TransactionInput>()
            insertTransaction(subcategoryName, transactionDate, transactionValue, isDebit)
        }
    }
}

@Serializable
data class TransactionInput(val subcategoryName: String, @Serializable(DateSerializer::class) val transactionDate: LocalDate, val transactionValue: Double, val isDebit: Boolean)