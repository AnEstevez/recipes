package com.andresestevez.recipes.ui.main.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.andresestevez.recipes.R
import com.andresestevez.recipes.data.database.RecipeDatabase
import com.andresestevez.recipes.data.toDBRecipe
import com.andresestevez.recipes.di.defaultFakeRecipes
import com.andresestevez.recipes.launchFragmentInHiltContainer
import com.andresestevez.recipes.ui.main.MainFragmentDirections
import com.andresestevez.recipes.ui.main.RecipesAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltAndroidTest
class FavFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var recipeDataBase: RecipeDatabase

    @Before
    fun setUp() {
        hiltRule.inject()

        recipeDataBase.recipeDao().insertRecipe(defaultFakeRecipes.map { it.toDBRecipe() })

    }

    @After
    fun tearDown() {
        recipeDataBase.close()
    }

    @Test
    fun favoriteItemsDisplayed() {

        launchFragmentInHiltContainer<FavFragment>()

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions
                .scrollToPosition<RecipesAdapter.ViewHolder>(0))
        onView(withText("Chocolate Chip Banana Bread")).check(matches(isDisplayed()))

    }

    @Test
    fun clickRecyclerViewItem_navigateToDetailFragment() {

        val recipeId = "1"
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<FavFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions
                .scrollToPosition<RecipesAdapter.ViewHolder>(1))
        onView(withText("Chocolate Chip Banana Bread")).check(matches(isDisplayed()))

        onView(withId(R.id.recycler)).perform(actionOnItemAtPosition<RecipesAdapter.ViewHolder>(0,
            click()))

        Thread.sleep(300)

        val direction = MainFragmentDirections.actionMainFragmentToDetailFragment(recipeId)
        verify(navController).navigate(direction)

    }

}