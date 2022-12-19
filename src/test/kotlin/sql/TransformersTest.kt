package sql

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.jetbrains.exposed.sql.ResultRow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class TransformersTest {

    @Test
    fun canTransformCategoriesRow(@MockK row: ResultRow) {
        val id = 1
        val name = "category"
        val expected = Category(id, name)
        every { row[Categories.categoryId] } returns id
        every { row[Categories.name] } returns name

        val actual = Category.transformRow(row)

        actual shouldBe expected
    }

    @Test
    fun canTransformSubcategoriesRow(@MockK row: ResultRow) {
        val id = 1
        val categoryId = 1
        val name = "subcategory"
        val expected = Subcategory(id, categoryId, name)
        every { row[Subcategories.subcategoryId] } returns id
        every { row[Subcategories.categoryId] } returns categoryId
        every { row[Subcategories.name] } returns name

        val actual = Subcategory.transformRow(row)

        actual shouldBe expected
    }

    @Test
    fun canTransformTransactions(@MockK date: LocalDate,
                                 @MockK row: ResultRow) {
        val id = 1
        val subcategoryId = 1
        val value = 0.01
        val isDebit = true
        val expected = Transaction(id, date, subcategoryId, value, isDebit)
        every { row[Transactions.transactionId] } returns id
        every { row[Transactions.subcategoryId] } returns subcategoryId
        every { row[Transactions.date] } returns date
        every { row[Transactions.value] } returns value
        every { row[Transactions.isDebitTransaction] } returns isDebit

        val actual = Transaction.transformRow(row)

        actual shouldBe expected
    }
}