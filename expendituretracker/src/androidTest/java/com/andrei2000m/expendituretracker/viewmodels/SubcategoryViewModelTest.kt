package com.andrei2000m.expendituretracker.viewmodels

import androidx.lifecycle.lifecycleScope
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.andrei2000m.expendituretracker.MainActivity
import com.andrei2000m.expendituretracker.R
import com.andrei2000m.expendituretracker.sql.Category
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
internal class SubcategoryViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var db: ExpenditureDb

    @Before
    fun before() {
        hiltRule.inject()
    }

    //TODO: Fix this

    @Test
    fun submitsCategoryOnButtonPress() {
        val expected = Category(name = "category")

        onView(withId(R.id.category_name)).perform(typeText("category")).perform(closeSoftKeyboard())
        Thread.sleep(250)
        onView(withId(R.id.category_submit_button)).perform(click())

        val categories = db.categoryDao().getAll()

        categories shouldHaveSize 1
        categories.first().shouldBeEqualToIgnoringFields(expected, Category::categoryId)
    }

    @Test
    fun showsToastIfCategoryAlreadyExists() {

        val expectedMessage = "Category category already exists"

        onView(withId(R.id.category_name)).perform(typeText("category")).perform(closeSoftKeyboard())
        Thread.sleep(250)
        onView(withId(R.id.category_submit_button)).perform(click())
        Thread.sleep(250)
        onView(withId(R.id.category_submit_button)).perform(click())

        activityRule.scenario.onActivity {
            it.lifecycleScope.launch(Dispatchers.IO) {
                onView(withText(expectedMessage))
                .inRoot(withDecorView(not(it.window.decorView)))
                .check((matches(isDisplayed())))
            }
        }
    }
}