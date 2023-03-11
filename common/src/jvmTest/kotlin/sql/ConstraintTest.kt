package sql

import com.zaxxer.hikari.HikariDataSource
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class ConstraintTest {

    private lateinit var dataSource: HikariDataSource

    @BeforeEach
    fun before() {
        dataSource = configure()
    }

    @Test
    fun throwsErrorWhenCategoryNameUniqueIndexBroken() {
        val exception = shouldThrow<ExposedSQLException> {
            transaction {
                SchemaUtils.create(Categories)

                Categories.insert {
                    it[name] = "Category"
                }

                Categories.insert {
                    it[name] = "Category"
                }
            }
        }

        exception.message shouldBe
                "org.sqlite.SQLiteException: [SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed (UNIQUE constraint failed: Categories.name)"
    }

    @Test
    fun throwsErrorWhenSubcategoryNameUniqueIndexBroken() {
        val exception = shouldThrow<ExposedSQLException> {
            transaction {
                SchemaUtils.create(Categories, Subcategories)

                Categories.insert {
                    it[name] = "Category"
                }

                Subcategories.insert {
                    it[categoryId] = 1
                    it[name] = "Subcategory"
                }

                Subcategories.insert {
                    it[categoryId] = 1
                    it[name] = "Subcategory"
                }
            }
        }

        exception.message shouldBe
                "org.sqlite.SQLiteException: [SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed (UNIQUE constraint failed: Subcategories.category_id, Subcategories.name)"
    }

    @Test
    fun throwsErrorWhenCategoryIdForeignKeyBroken() {
        val exception = shouldThrow<ExposedSQLException> {
            transaction {
                SchemaUtils.create(Subcategories)

                Subcategories.insert {
                    it[categoryId] = 1
                    it[name] = "Subcategory"
                }
            }
        }

        exception.message shouldBe
                "org.sqlite.SQLiteException: [SQLITE_CONSTRAINT_FOREIGNKEY] A foreign key constraint failed (FOREIGN KEY constraint failed)"
    }

    @Test
    fun throwsErrorWhenSubcategoryIdForeignKeyBroken() {
        val exception = shouldThrow<ExposedSQLException> {
            transaction {
                SchemaUtils.create(Transactions)

                Transactions.insert {
                    it[subcategoryId] = 1
                    it[date] = LocalDate.of(2022, 12, 10)
                    it[value] = 0.1
                    it[isDebitTransaction] = true
                }
            }
        }

        exception.message shouldBe
                "org.sqlite.SQLiteException: [SQLITE_CONSTRAINT_FOREIGNKEY] A foreign key constraint failed (FOREIGN KEY constraint failed)"
    }

    @AfterEach
    fun after() {
        dataSource.close()
    }
}