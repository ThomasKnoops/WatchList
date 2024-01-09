package org.thoteman.watchlist

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.thoteman.watchlist.authentication.SignUpActivity


@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {
    @JvmField
    @Rule
    var activityRule = ActivityScenarioRule(
        SignUpActivity::class.java
    )

    @Test
    fun testEmptyFields() {
        // Click the sign-up button without entering any data
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUpR)).perform(ViewActions.click())

        // Check if the alert dialog for empty fields is displayed
        Espresso.onView(ViewMatchers.withText(R.string.empty_fields))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testInvalidEmail() {
        // Enter an invalid email address
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmailR))
            .perform(ViewActions.typeText("invalidemail"), ViewActions.closeSoftKeyboard())

        // Enter all the rest correctly
        Espresso.onView(ViewMatchers.withId(R.id.editTextPasswordR))
            .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.editTextPasswordConfirmationR))
            .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())

        // Click the sign-up button
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUpR)).perform(ViewActions.click())

        // Check if the alert dialog for invalid email is displayed
        Espresso.onView(ViewMatchers.withText(R.string.invalid_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testShortPassword() {
        // Enter a password less than 6 characters
        Espresso.onView(ViewMatchers.withId(R.id.editTextPasswordR))
            .perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.editTextPasswordConfirmationR))
            .perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard())

        // Enter all the rest correctly
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmailR))
            .perform(ViewActions.typeText("this@is.valid"), ViewActions.closeSoftKeyboard())

        // Click the sign-up button
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUpR)).perform(ViewActions.click())

        // Check if the alert dialog for invalid password is displayed
        Espresso.onView(ViewMatchers.withText(R.string.invalid_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testPasswordMismatch() {
        // Enter a password less than 6 characters
        Espresso.onView(ViewMatchers.withId(R.id.editTextPasswordR))
            .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.editTextPasswordConfirmationR))
            .perform(ViewActions.typeText("1234567"), ViewActions.closeSoftKeyboard())

        // Enter all the rest correctly
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmailR))
            .perform(ViewActions.typeText("this@is.valid"), ViewActions.closeSoftKeyboard())

        // Click the sign-up button
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUpR)).perform(ViewActions.click())

        // Check if the alert dialog for password mismatch is displayed
        Espresso.onView(ViewMatchers.withText(R.string.password_mismatch))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}