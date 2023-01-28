package routes

import com.zaxxer.hikari.HikariDataSource
import config.configureRouting
import config.configureSerialization
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockkStatic
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sql.Categories
import sql.Category
import sql.Subcategories
import sql.Transactions
import sql.configure
import sql.getAllCategories
import java.time.LocalDate

internal class CategoryTest {

    private lateinit var dataSource: HikariDataSource

    @BeforeEach
    fun before() {
        dataSource = configure()
    }

    @Test
    fun canGetAllCategories() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val category1 = Category(1, "name1")
        val category2 = Category(2, "name2")

        mockkStatic(::getAllCategories)
        transaction {
            SchemaUtils.create(Categories)
            every { getAllCategories() } returns listOf(category1, category2)
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/category") {
            accept(ContentType.Application.Json)
        }

        val categoryList: List<Category> = response.body()

        categoryList shouldContainAll listOf(category1, category2)
    }

    @Test
    fun canCalculateTotalValuesPerCategory() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        transaction {
            SchemaUtils.create(Categories, Subcategories, Transactions)
        }

        transaction {
            Categories.insert {
                it[name] = "Housing"
            }

            Categories.insert {
                it[name] = "Food"
            }

            Categories.insert {
                it[name] = "Entertainment"
            }

            Subcategories.insert {
                it[categoryId] = 1
                it[name] = "Rent"
            }

            Subcategories.insert {
                it[categoryId] = 1
                it[name] = "Utilities"
            }

            Subcategories.insert {
                it[categoryId] = 2
                it[name] = "Meat"
            }

            Transactions.insert {
                it[subcategoryId] = 1
                it[date] = LocalDate.of(2022, 9, 15)
                it[value] = 157.67
                it[isDebitTransaction] = true
            }

            Transactions.insert {
                it[subcategoryId] = 2
                it[date] = LocalDate.of(2022, 10, 21)
                it[value] = 52.56
                it[isDebitTransaction] = true
            }

            Transactions.insert {
                it[subcategoryId] = 3
                it[date] = LocalDate.of(2022, 12, 1)
                it[value] = 10.25
                it[isDebitTransaction] = true
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/category/values") {
            accept(ContentType.Application.Json)
        }

        val categoryToValueMap: Map<String, Double> = response.body()

        categoryToValueMap.size shouldBe 3
        categoryToValueMap shouldContainExactly mapOf(Pair("Food", 10.25), Pair("Entertainment", 0.0), Pair("Housing", 210.23))
    }

    @Test
    fun canInsertCategory() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val categoryName = "category"

        transaction {
            SchemaUtils.create(Categories)
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expected = Category(categoryId = 1, name = categoryName)

        client.post("/category") {
            contentType(ContentType.Application.Json)
            setBody(categoryName)
        }

        val actual = transaction {
            Categories.selectAll().toList().map { Category.transformRow(it) }
        }

        actual shouldHaveSize 1
        actual.first() shouldBe expected
    }

    @AfterEach
    fun after() {
        dataSource.close()
    }
}