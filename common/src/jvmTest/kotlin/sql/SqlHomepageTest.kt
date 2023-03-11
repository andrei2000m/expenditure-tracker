package sql

import com.zaxxer.hikari.HikariDataSource
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class SqlHomepageTest {

    private lateinit var dateSource: HikariDataSource

    @BeforeEach
    fun before() {
        dateSource = configure()
    }

    @Test
    fun canGetAllCategories(@MockK query: Query,
                            @MockK row1: ResultRow,
                            @MockK row2: ResultRow) {
        transaction {
            SchemaUtils.create(Categories)
        }

        mockkStatic(Categories::selectAll)
        mockkStatic(::getAllCategories)
        every { Categories.selectAll() } returns query
        every { query.iterator() } returns listOf(row1, row2).iterator()
        every { row1[Categories.categoryId] } returns 1
        every { row1[Categories.name] } returns "name1"
        every { row2[Categories.categoryId] } returns 2
        every { row2[Categories.name] } returns "name2"

        val category1 = Category(1, "name1")
        val category2 = Category(2, "name2")

        val categoryList: List<Category> = getAllCategories()

        categoryList shouldContainExactly listOf(category1, category2)
    }

    @Test
    fun canCalculateTotalValuesPerCategory() {
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

        val categoryToValueMap: Map<String, Double> = calculateTotalValuesPerCategory()

        categoryToValueMap.size shouldBe 3
        categoryToValueMap shouldContainExactly mapOf(Pair("Food", 10.25), Pair("Entertainment", 0.0), Pair("Housing", 210.23))
    }

    @AfterEach
    fun after() {
        dateSource.close()
    }
}