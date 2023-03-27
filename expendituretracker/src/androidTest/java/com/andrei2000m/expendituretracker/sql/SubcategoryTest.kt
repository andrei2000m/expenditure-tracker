package com.andrei2000m.expendituretracker.sql

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import java.io.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubcategoryTest {
    private lateinit var db: ExpenditureDb
    private lateinit var categoryDao: CategoryDao
    private lateinit var subcategoryDao: SubcategoryDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ExpenditureDb::class.java).build()
        categoryDao = db.categoryDao()
        subcategoryDao = db.subcategoryDao()
    }

    @Test
    fun canInsertSubcategoryThenGet() {
        val category = Category(name = "category")
        val expected = Subcategory(name = "subcategory", categoryId = 1)

        categoryDao.insertCategory(category)
        subcategoryDao.insertSubcategory(expected)

        val result = subcategoryDao.getAll()

        result shouldHaveSize 1
        result.first().shouldBeEqualToIgnoringFields(expected, Subcategory::subcategoryId)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}