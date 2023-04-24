package com.andrei2000m.expendituretracker.viewmodels

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.andrei2000m.expendituretracker.R
import com.andrei2000m.expendituretracker.sql.Category
import com.andrei2000m.expendituretracker.sql.CategoryDao
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import javax.inject.Inject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
internal class CategoryViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: ExpenditureDb

    private lateinit var categoryDao: CategoryDao

    @Before
    fun before() {
        hiltRule.inject()
        categoryDao = db.categoryDao()
    }

    @Test
    fun submitsCategoryOnButtonPress() {
        val expected = Category(name = "category")

        onView(withId(R.id.category_name)).perform(typeText("category"))
        onView(withId(R.id.category_submit_button)).perform(click())

        val categories = categoryDao.getAll().value!!

        categories shouldHaveSize 1
        categories.first().shouldBeEqualToIgnoringFields(expected, Category::categoryId)
    }

    @After
    fun after() {
        db.close()
    }
}