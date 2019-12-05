@file:Suppress("DEPRECATION")

package com.android.footballapi.feature

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import com.android.footballapi.R.id.*

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testRecyclerViewBehaviour() {
        /*
         i used "Thread.sleep(2000)" for waiting response from server,
         because if i didn't use that thread the test will be error, cause the data is null
         */
        Thread.sleep(2000)
        onView(withId(rvTest)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(rvTest)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
        Thread.sleep(1000)
        onView(withId(rvTest)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click())
        )
    }

}