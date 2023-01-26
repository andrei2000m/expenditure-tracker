import com.zaxxer.hikari.HikariDataSource
import config.configureRouting
import config.configureSerialization
import io.kotest.matchers.collections.shouldContainAll
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockkStatic
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sql.Categories
import sql.Category
import sql.configure
import sql.getAllCategories

internal class ServerTest {

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

        val response = client.get("/categories") {
            accept(ContentType.Application.Json)
        }

        val categoryList: List<Category> = response.body()

        categoryList shouldContainAll listOf(category1, category2)
    }
}