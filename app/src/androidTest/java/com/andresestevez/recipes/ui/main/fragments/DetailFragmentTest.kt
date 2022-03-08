package com.andresestevez.recipes.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.andresestevez.recipes.MockServerDispatcher
import com.andresestevez.recipes.R
import com.andresestevez.recipes.asAndroidX
import com.andresestevez.recipes.data.database.RecipeDatabase
import com.andresestevez.recipes.data.server.TheMealDbClient
import com.andresestevez.recipes.data.toEntity
import com.andresestevez.recipes.di.mockedRecipe
import com.andresestevez.recipes.launchFragmentInHiltContainer
import com.andresestevez.recipes.ui.detail.DetailFragment
import com.andresestevez.recipes.ui.detail.DetailViewModel
import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsString
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltAndroidTest
class DetailFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var recipeDbClient: TheMealDbClient

    @Inject
    lateinit var recipeDataBase: RecipeDatabase

    @Before
    fun setUp() {
        hiltRule.inject()

        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)

        val resource =
            OkHttp3IdlingResource.create("OkHttp", recipeDbClient.okHttpClient).asAndroidX()
        IdlingRegistry.getInstance().register(resource)

        val recipe = mockedRecipe.copy(id = "53037", instructions = "").toEntity()
        runBlocking {
            recipeDataBase.recipeDao().insertAll(listOf(recipe))
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        recipeDataBase.close()
    }

    @Test
    fun itemDetailsDisplayed() {

        val myBundle = Bundle()
        myBundle.putString(DetailViewModel.RECIPE_ID_NAV_ARGS, "53037")

        launchFragmentInHiltContainer<DetailFragment>(myBundle) {
            Log.e("SavedStateHandle keys: ", viewModel.stateHandle.keys().toString())
        }

        Thread.sleep(200)

        val textView = onView(withId(R.id.ingredientsSectionText))
        textView.check(matches(withText("Ingredients")))

        val textView2 = onView(Matchers.allOf(withId(R.id.ingredients), isDisplayed()))
        textView2.check(matches(withText(containsString("BUTTER"))))

        val textView3 = onView(
            Matchers.allOf(withId(R.id.methodSectionText), withText("Method"),
                withParent(Matchers.allOf(withId(R.id.methodSectionCard),
                    withParent(IsInstanceOf.instanceOf(LinearLayout::class.java)))),
                isDisplayed()))
        textView3.check(matches(withText("Method")))

        val textView4 = onView(withId(R.id.instructions))
        textView4.check(matches(withSubstring("Heat the butter in a casserole dish until sizzling")))
    }

}