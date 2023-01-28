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
import sql.Subcategory
import sql.configure

internal class SubcategoryTest {

    private lateinit var dataSource: HikariDataSource

    @BeforeEach
    fun before() {
        dataSource = configure()
    }

    @Test
    fun canInsertSubCategory() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val categoryName = "category"
        val subcategoryName = "subcategory"

        transaction {
            SchemaUtils.create(Categories, Subcategories)
            Categories.insert {
                it[name] = categoryName
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expected = Subcategory(subcategoryId = 1, name = subcategoryName, categoryId = 1)

        client.post("/subcategory") {
            contentType(ContentType.Application.Json)
            setBody(Pair(categoryName, subcategoryName))
        }

        val actual = transaction {
            Subcategories.selectAll().toList().map { Subcategory.transformRow(it) }
        }

        actual shouldHaveSize 1
        actual.first() shouldBe expected
    }

    @AfterEach
    fun after() {
        dataSource.close()
    }
}