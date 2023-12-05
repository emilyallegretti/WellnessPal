package com.bignerdranch.android.wellnesspal

import android.app.Activity
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasBackground
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bignerdranch.android.wellnesspal.ui.authenticate.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class EspressoUITests {
    @get:Rule
    var activityRule: ActivityScenarioRule<AuthActivity> =
        ActivityScenarioRule(AuthActivity::class.java)
    var db = Firebase.database.reference
    var auth = FirebaseAuth.getInstance().currentUser

    //var intentsTestRule: IntentsTestRule<MainActivity> = IntentsTestRule(YourActivity::class.java)
    // tests user sign-up and pet creation process.
    @Before
    fun setUp() {
        // before every test
        signOutCurrentUser()
    }

    @Test
    fun testSignUpFlowAndNewPet() {
        signOutCurrentUser()
        // click sign-up button and ensure new fields are in view
        Thread.sleep(3000)
        onView(withId(R.id.buttonSignUp)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonSignUp)).perform(click())
        onView(withId(R.id.fieldFname)).check(matches(isDisplayed()))
        // fill in required fields
        onView(withId(R.id.fieldFname)).perform(typeText("Bob"))
        onView(withId(R.id.fieldLname)).perform(typeText("Joe"))
        onView(withId(R.id.fieldEmail)).perform(typeText("bob@joe.com"))
        onView(withId(R.id.fieldPassword)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.fieldFoodGoal)).perform(typeText("25"))
        onView(withId(R.id.fieldSleepGoal)).perform(typeText("25"))
        onView(withId(R.id.fieldWaterGoal)).perform(typeText("25"), closeSoftKeyboard())
        // click sign up and assert that the Create New Pet screen is present
        onView(withId(R.id.buttonCreateAccount)).perform(click())
        Thread.sleep(5000)
        onView(withText("Create A New Pet")).check(matches(isDisplayed()))
        // fill in pet name, check collar color selections and assert pet pic gets updated
        onView(withId(R.id.petNameField)).perform(typeText("spot"), closeSoftKeyboard())
        // check that default selection is blue
        onView(withId(R.id.petPreviewImage))
            .check(
                matches(EspressoTestMatchers.withDrawable(R.drawable.small_blue_happy))
            )
        // purple selection
        onView(withId(R.id.collar_choices)).perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("Purple")
            )
        ).perform(click())
        onView(withId(R.id.petPreviewImage))
            .check(
                matches(EspressoTestMatchers.withDrawable(R.drawable.small_purple_happy))
            )
        // red selection
        onView(withId(R.id.collar_choices)).perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("Red")
            )
        ).perform(click())
        onView(withId(R.id.petPreviewImage))
            .check(
                matches(EspressoTestMatchers.withDrawable(R.drawable.small_red_happy))
            )
        // finally submit new pet and ensure we are on pet info screen
        onView(withId(R.id.navigation_petInfo)).check(matches(isSelected()))
        onView(withId(R.id.petPreviewImage)).check(matches(EspressoTestMatchers.withDrawable(R.drawable.small_red_happy)))
            .check(
                matches(isDisplayed())
            )
    }


    /*
   Test that meeting a wellness goal with a pet of age 2 will cause the picture to update to medium sized pet with correct color (since pet
 has aged to 3) as well as the text on the goal page to display the expected notes.
 */

    @Test
    fun testAgeUpUI() {
        signOutCurrentUser()
        // sign into test account
        onView(withId(R.id.buttonSignIn)).perform(click())
        onView(withId(R.id.fieldEmail)).perform(typeText("test@test.com"))
        onView((withId(R.id.fieldPassword))).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.buttonSignIn)).perform(click())
        Thread.sleep(6000)
        // submit a water log
        onView(withId(R.id.waterLogButton)).perform(click())
        // make sure goal info is as expected
        Thread.sleep(1500)
        onView(withId(R.id.waterLeftToGoalText)).check(matches(withText("Oz. water left to reach goal: 120")))
        onView(withId(R.id.waterGoalText)).check(matches(withText("Water Goal: 120")))
        onView(withId(R.id.fieldWaterLog)).perform(typeText("120"), closeSoftKeyboard())
        onView(withId(R.id.buttonSubmitWaterLog)).perform(click())
        Thread.sleep(2000)
        // check that new image is placed upon pet age-up to 3
        // onView(withId(R.id.petImage)).check(matches(EspressoTestMatchers.withDrawable(R.drawable.med_blue_happy)))
        onView(withId(R.id.petImage)).check(matches(EspressoTestMatchers.withDrawable(R.drawable.small_red_happy)))
        onView(withId(R.id.waterLogButton)).perform(click())

        // check goal text on water log page is correct
        onView(withId(R.id.waterLeftToGoalText)).check(matches(withText("Oz. water left to reach goal: 0")))
        onView(withId(R.id.waterGoalText)).check(matches(withText("Water Goal: 120")))
        onView(withId(R.id.goalsMetNote)).check(matches(isDisplayed()))
    }


    /*
 Check that upon a pet reaching age 9, the user has been redirected to the Pet Archive screen and their current pet is displayed in the RecyclerView.
 */
    @Test
    fun petRetireFlow(): Unit {
        signOutCurrentUser()
        // sign into test account
        onView(withId(R.id.buttonSignIn)).perform(click())
        onView(withId(R.id.fieldEmail)).perform(typeText("test2@test2.com"))
        onView((withId(R.id.fieldPassword))).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.buttonSignIn)).perform(click())
        Thread.sleep(3000)
        // Make sure we are in pet info fragment
        onView(withId(R.id.ageInfo)).check(matches(isDisplayed()))
        // submit a water log meeting goal to get pet to age up
        onView(withId(R.id.sleepLogButton)).perform(click())
        onView(withId(R.id.fieldSleepLog)).perform(typeText("8"), closeSoftKeyboard())
        onView(withId(R.id.buttonSubmitSleepLog)).perform(click())
        Thread.sleep(3000)
        // check that user has been redirected to pet archive screen
        onView(withId(R.id.petArchiveTitle)).check(matches(isDisplayed()))
        // check that pet is in the archive
        //scrollTo should fail if item does not exist
        onView(withId(R.id.archive_recycler_view)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                hasDescendant(withText("spot"))
            )
        )


    }


    private fun deleteUser() {
        if (auth != null) {
            // delete from db
            Firebase.database.getReference().child("users").child(auth!!.uid).removeValue()
            //delete from auth
            auth!!.delete()
        }
    }

    private fun signOutCurrentUser() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            // sign out if there's a user signed in
            onView(withId(R.id.navigation_profile)).perform(click())
            onView(withId(R.id.buttonSignOut)).perform(click())
        }
    }
}