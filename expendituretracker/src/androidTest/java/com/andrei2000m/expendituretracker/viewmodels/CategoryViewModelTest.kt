package com.andrei2000m.expendituretracker.viewmodels

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.andrei2000m.expendituretracker.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
internal class CategoryViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun submitsCategoryOnButtonPress() {
        onView(withId(R.id.category_name)).perform(typeText("category"))
        onView(withId(R.id.category_submit_button)).perform(click())
    }
}