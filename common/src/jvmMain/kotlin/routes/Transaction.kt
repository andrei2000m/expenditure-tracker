package routes

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
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