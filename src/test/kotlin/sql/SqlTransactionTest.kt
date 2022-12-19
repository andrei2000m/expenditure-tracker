package sql

import com.zaxxer.hikari.HikariDataSource
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SqlTransactionTest {

    private lateinit var dataSource: HikariDataSource

    @BeforeEach
    fun before() {
        dataSource = configure()
    }

    @Test
    fun canInsertTransaction() {
        val categoryName = "category"
        val subcategoryName = "subcategory"
        val date = LocalDate.of(2022, 12, 10)
        val value = 1.25

        transaction {
            SchemaUtils.create(Categories, Subcategories, Transactions)
            Categories.insert {
                it[name] = categoryName
            }
            Subcategories.insert {
                it[name] = subcategoryName
                it[categoryId] = 1
            }
        }

        insertTransaction(subcategoryName = subcategoryName, transactionDate = date, transactionValue = value, isDebit = true)

        val transaction = Transaction(transactionId = 1, date = date, subcategoryId = 1, value = value, isDebitTransaction = true)

        val actual = transaction {
            Transactions.selectAll().toList().map { Transaction.transformRow(it) }
        }

        actual shouldHaveSize 1
        actual.first() shouldBe transaction
    }

    @AfterEach
    fun after() {
        dataSource.close()
    }
}