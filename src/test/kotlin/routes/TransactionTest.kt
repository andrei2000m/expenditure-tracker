package routes

import com.zaxxer.hikari.HikariDataSource
import config.configureRouting
import config.configureSerialization
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sql.Categories
import sql.Subcategories
import sql.Transaction
import sql.Transactions
import sql.configure
import java.time.LocalDate

internal class TransactionTest {

    private lateinit var dataSource: HikariDataSource

    @BeforeEach
    fun before() {
        dataSource = configure()
    }

    @Test
    fun canInsertTransaction() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

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

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expected = Transaction(transactionId = 1, date = date, subcategoryId = 1, value = value, isDebitTransaction = true)

        client.post("/transaction") {
            contentType(ContentType.Application.Json)
            setBody(TransactionInput(subcategoryName, date, value, true))
        }

        val actual = transaction {
            Transactions.selectAll().toList().map { Transaction.transformRow(it) }
        }

        actual shouldHaveSize 1
        actual.first() shouldBe expected
    }

    @AfterEach
    fun after() {
        dataSource.close()
    }
}