package com.bignerdranch.android.wellnesspal

import androidx.test.ext.junit.rules.activityScenarioRule
import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class EspressoUITests {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()
    // tests user sign-up and transition into MainActivity.
    @Test
    fun testSignUpFlowAndRequiredFields() {
        onView(withId(R.id.fieldFname)).perform()
    }
}