package com.andrei2000m.expendituretracker

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
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
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.io.IOException
import java.time.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConstraintTest {
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
    fun returnsNegativeWhenCategoryNameUniqueIndexBroken() {
        val category = Category(name = "Category")
        categoryDao.insertCategory(category)

        val changedRow = categoryDao.insertCategory(category)

        changedRow shouldBe -1
    }

    @Test
    fun returnsNegativeWhenSubcategoryNameUniqueIndexBroken() {
        val category = Category(name = "Category")
        val subcategory = Subcategory(categoryId = 1, name = "Subcategory")
        categoryDao.insertCategory(category)
        subcategoryDao.insertSubcategory(subcategory)

        val changedRow = subcategoryDao.insertSubcategory(subcategory)

        changedRow shouldBe -1
    }

    @Test
    fun throwsExceptionWhenCategoryIdForeignKeyBroken() {
        val subcategory = Subcategory(categoryId = 1, name = "Subcategory")
        val exception = shouldThrow<SQLiteConstraintException> {
            subcategoryDao.insertSubcategory(subcategory)
        }

        exception.message shouldBe "FOREIGN KEY constraint failed (code 787 SQLITE_CONSTRAINT_FOREIGNKEY)"
    }

    @Test
    fun throwsExceptionWhenSubcategoryIdForeignKeyBroken() {
        val transaction = Transaction(subcategoryId = 1, date = LocalDate.of(2022, 12, 10), value = 0.1, isDebitTransaction = true)
        val exception = shouldThrow<SQLiteConstraintException> {
            transactionDao.insertTransaction(transaction)
        }

        exception.message shouldBe "FOREIGN KEY constraint failed (code 787 SQLITE_CONSTRAINT_FOREIGNKEY)"
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}