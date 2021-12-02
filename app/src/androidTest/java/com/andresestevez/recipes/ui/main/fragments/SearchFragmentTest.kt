package com.andresestevez.recipes.ui.main.fragments

import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.andresestevez.recipes.MockServerDispatcher
import com.andresestevez.recipes.R
import com.andresestevez.recipes.asAndroidX
import com.andresestevez.recipes.data.server.TheMealDbClient
import com.andresestevez.recipes.launchFragmentInHiltContainer
import com.andresestevez.recipes.ui.main.MainFragmentDirections
import com.andresestevez.recipes.ui.main.RecipesAdapter
import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltAndroidTest
class SearchFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var recipeDbClient: TheMealDbClient

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)

        val resource =
            OkHttp3IdlingResource.create("OkHttp", recipeDbClient.okHttpClient).asAndroidX()
        IdlingRegistry.getInstance().register(resource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun searchByName_displayItems() {

        launchFragmentInHiltContainer<SearchFragment> ()

        val appCompatImageView = onView(
            Matchers.allOf(withId(R.id.search_button), withContentDescription("Search"),
                childAtPosition(
                    Matchers.allOf(withId(R.id.search_bar),
                        childAtPosition(
                            withId(R.id.searchView),
                            0)),
                    1),
                isDisplayed()))
        appCompatImageView.perform(click())

        val searchAutoComplete = onView(
            Matchers.allOf(withId(R.id.search_src_text),
                childAtPosition(
                    Matchers.allOf(withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1)),
                    0),
                isDisplayed()))
        searchAutoComplete.perform(replaceText("pork"), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            Matchers.allOf(withId(R.id.search_src_text),
                childAtPosition(
                    Matchers.allOf(withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1)),
                    0),
                isDisplayed()))
        searchAutoComplete2.perform(pressImeActionButton())

        Thread.sleep(300)

        val textView = onView(
            Matchers.allOf(withId(R.id.textViewRecipe), withText("TONKATSU PORK"),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()))
        textView.check(matches(withText("TONKATSU PORK")))

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions
                .scrollToPosition<RecipesAdapter.ViewHolder>(0))
        onView(withText("Tonkatsu pork")).check(matches(isDisplayed()))

    }

    @Test
    fun clickRecyclerViewItem_navigateToDetailFragment() {

        val recipeId = "53032"
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<SearchFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        val appCompatImageView = onView(
            Matchers.allOf(withId(R.id.search_button), withContentDescription("Search"),
                childAtPosition(
                    Matchers.allOf(withId(R.id.search_bar),
                        childAtPosition(
                            withId(R.id.searchView),
                            0)),
                    1),
                isDisplayed()))
        appCompatImageView.perform(click())

        val searchAutoComplete = onView(
            Matchers.allOf(withId(R.id.search_src_text),
                childAtPosition(
                    Matchers.allOf(withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1)),
                    0),
                isDisplayed()))
        searchAutoComplete.perform(replaceText("pork"), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            Matchers.allOf(withId(R.id.search_src_text),
                childAtPosition(
                    Matchers.allOf(withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1)),
                    0),
                isDisplayed()))
        searchAutoComplete2.perform(pressImeActionButton())

        Thread.sleep(300)

        val textView = onView(
            Matchers.allOf(withId(R.id.textViewRecipe), withText("TONKATSU PORK"),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()))
        textView.check(matches(withText("TONKATSU PORK")))

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions
                .scrollToPosition<RecipesAdapter.ViewHolder>(0))
        onView(withText("Tonkatsu pork")).check(matches(isDisplayed()))

        onView(withId(R.id.recycler)).perform(actionOnItemAtPosition<RecipesAdapter.ViewHolder>(0,
            click()))

        val direction = MainFragmentDirections.actionMainFragmentToDetailFragment(recipeId)
        verify(navController).navigate(direction)

    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int,
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}