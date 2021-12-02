package com.andresestevez.recipes.ui.main.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
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
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltAndroidTest
class LocalRecipesFragmentTest {

    private var hiltRule = HiltAndroidRule(this)

    private var mGrantPermissionRule =
        GrantPermissionRule.grant("android.permission.ACCESS_COARSE_LOCATION")!!

    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(mGrantPermissionRule)

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
    fun localRecipesDisplayed() {

        launchFragmentInHiltContainer<LocalRecipesFragment> {
            this.viewModel.refresh()
        }

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

        launchFragmentInHiltContainer<LocalRecipesFragment> {
            this.viewModel.refresh()
            Navigation.setViewNavController(requireView(), navController)
        }

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

}