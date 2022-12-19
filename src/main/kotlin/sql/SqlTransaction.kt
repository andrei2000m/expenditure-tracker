package sql

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun insertTransaction(subcategoryName: String, transactionDate: LocalDate, transactionValue: Double, isDebit: Boolean) {
    transaction {
        val query = Subcategories.slice(Subcategories.subcategoryId).select { Subcategories.name eq subcategoryName }
        val id = query.iterator().next()[Subcategories.subcategoryId]

        Transactions.insert {
            it[subcategoryId] = id
            it[date] = transactionDate
            it[value] = transactionValue
            it[isDebitTransaction] = isDebit
        }
    }
}