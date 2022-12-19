package sql

import com.zaxxer.hikari.HikariDataSource
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SqlHomepageIT {

    private lateinit var dateSource: HikariDataSource

    @BeforeEach
    fun before() {
        dateSource = configure()
    }

    @Test
    fun canInsertIntoDatabaseAndCalculateTotalValues() {
        transaction {
            SchemaUtils.create(Categories, Subcategories, Transactions)
        }

        insertCategory("Housing")
        insertCategory("Food")
        insertCategory("Entertainment")

        insertSubcategory(categoryName = "Housing", subcategoryName = "Rent")
        insertSubcategory(categoryName = "Housing", subcategoryName = "Utilities")
        insertSubcategory(categoryName = "Food", subcategoryName = "Meat")

        insertTransaction(subcategoryName = "Rent", transactionDate = LocalDate.of(2022, 9, 15), transactionValue = 157.67, isDebit = true)
        insertTransaction(subcategoryName = "Utilities", transactionDate = LocalDate.of(2022, 10, 21), transactionValue = 52.56, isDebit = true)
        insertTransaction(subcategoryName = "Meat", transactionDate = LocalDate.of(2022, 12, 1), transactionValue = 10.25, isDebit = true)

        val categoryToValueMap: Map<String, Double> = calculateTotalValuesPerCategory()

        categoryToValueMap.size shouldBe 3
        categoryToValueMap shouldContainExactly mapOf(Pair("Food", 10.25), Pair("Entertainment", 0.0), Pair("Housing", 210.23))
    }

    @AfterEach
    fun after() {
        dateSource.close()
    }
}