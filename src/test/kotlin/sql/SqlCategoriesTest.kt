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

internal class SqlCategoriesTest {

    private lateinit var dataSource: HikariDataSource

    @BeforeEach
    fun before() {
        dataSource = configure()
    }

    @Test
    fun canInsertCategory() {
        val categoryName = "category"

        transaction {
            SchemaUtils.create(Categories)
        }

        insertCategory(categoryName = categoryName)

        val category = Category(categoryId = 1, name = categoryName)

        val actual = transaction {
            Categories.selectAll().toList().map { Category.transformRow(it) }
        }

        actual shouldHaveSize 1
        actual.first() shouldBe category
    }

    @Test
    fun canInsertSubCategory() {
        val categoryName = "category"
        val subcategoryName = "subcategory"

        transaction {
            SchemaUtils.create(Categories, Subcategories)
            Categories.insert {
                it[name] = categoryName
            }
        }

        insertSubcategory(categoryName = categoryName, subcategoryName = subcategoryName)

        val subcategory = Subcategory(subcategoryId = 1, name = subcategoryName, categoryId = 1)

        val actual = transaction {
            Subcategories.selectAll().toList().map { Subcategory.transformRow(it) }
        }

        actual shouldHaveSize 1
        actual.first() shouldBe subcategory
    }

    @AfterEach
    fun after() {
        dataSource.close()
    }
}