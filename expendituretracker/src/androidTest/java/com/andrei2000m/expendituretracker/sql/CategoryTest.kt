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
class CategoryTest {
    private lateinit var db: ExpenditureDb
    private lateinit var categoryDao: CategoryDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ExpenditureDb::class.java).build()
        categoryDao = db.categoryDao()
    }

    @Test
    fun canInsertCategoryThenGet() {
        val expected = Category(name = "category")

        categoryDao.insertCategory(expected)

        val result = categoryDao.getAll()

        result shouldHaveSize 1
        result.first().shouldBeEqualToIgnoringFields(expected, Category::categoryId)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}