package com.andrei2000m.expendituretracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrei2000m.expendituretracker.sql.Category
import com.andrei2000m.expendituretracker.sql.CategoryDao
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import com.andrei2000m.expendituretracker.sql.Subcategory
import com.andrei2000m.expendituretracker.sql.SubcategoryDao
import com.andrei2000m.expendituretracker.sql.Transaction
import com.andrei2000m.expendituretracker.sql.TransactionDao
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.io.IOException
import java.time.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionTest {
    private lateinit var db: ExpenditureDb
    private lateinit var categoryDao: CategoryDao
    private lateinit var subcategoryDao: SubcategoryDao
    private lateinit var transactionDao: TransactionDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ExpenditureDb::class.java).build()
        categoryDao = db.categoryDao()
        subcategoryDao = db.subcategoryDao()
        transactionDao = db.transactionDao()
    }

    @Test
    fun canInsertTransactionThenGet() {
        val category = Category(name = "category")
        val subcategory = Subcategory(name = "subcategory", categoryId = 1)

        val date = LocalDate.of(2022, 12, 10)
        val value = 1.25
        val expected = Transaction(subcategoryId = 1, date = date, value = value, isDebitTransaction = true)

        categoryDao.insertCategory(category)
        subcategoryDao.insertSubcategory(subcategory)
        transactionDao.insertTransaction(expected)

        val result = transactionDao.getAll()

        result shouldHaveSize 1
        result.first().shouldBeEqualToIgnoringFields(expected, Transaction::transactionId)
    }

    @Test
    fun canCalculateTotalValuesByCategory() {
        val category1 = Category(name = "Housing")
        val category2 = Category(name = "Food")
        val category3 = Category(name = "Entertainment")

        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)
        categoryDao.insertCategory(category3)

        val subcategory1 = Subcategory(categoryId = 1, name = "Rent")
        val subcategory2 = Subcategory(categoryId = 1, name = "Utilities")
        val subcategory3 = Subcategory(categoryId = 2, name = "Meat")

        subcategoryDao.insertSubcategory(subcategory1)
        subcategoryDao.insertSubcategory(subcategory2)
        subcategoryDao.insertSubcategory(subcategory3)

        val transaction1 = Transaction(subcategoryId = 1, date = LocalDate.of(2022, 9, 15), value = 157.67, isDebitTransaction = true)
        val transaction2 = Transaction(subcategoryId = 2, date = LocalDate.of(2022, 10, 21), value = 52.56, isDebitTransaction = true)
        val transaction3 = Transaction(subcategoryId = 3, date = LocalDate.of(2022, 12, 1), value = 10.25, isDebitTransaction = true)

        transactionDao.insertTransaction(transaction1)
        transactionDao.insertTransaction(transaction2)
        transactionDao.insertTransaction(transaction3)

        val categoryToValueMap: Map<String, Double> = transactionDao.calculateTotalValuesPerCategory()

        categoryToValueMap.size shouldBe 3
        categoryToValueMap shouldContainExactly mapOf(Pair("Food", 10.25), Pair("Entertainment", 0.0), Pair("Housing", 210.23))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}