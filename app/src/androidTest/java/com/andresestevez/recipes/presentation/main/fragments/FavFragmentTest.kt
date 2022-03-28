package com.andresestevez.recipes.presentation.main.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.andresestevez.recipes.R
import com.andresestevez.recipes.framework.database.RecipeDatabase
import com.andresestevez.recipes.framework.toEntity
import com.andresestevez.recipes.di.defaultFakeRecipes
import com.andresestevez.recipes.launchFragmentInHiltContainer
import com.andresestevez.recipes.presentation.main.MainFragmentDirections
import com.andresestevez.recipes.presentation.main.RecipesAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
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

        runBlocking {
            recipeDataBase.recipeDao().insertAll(defaultFakeRecipes.map { it.toEntity() })
        }

    }

    @After
    fun tearDown() {
        recipeDataBase.close()
    }

    @Test
    fun favoriteItemsDisplayed() {

        launchFragmentInHiltContainer<FavFragment>()

        Thread.sleep(200)

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

        Thread.sleep(300)

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


    @Test
    fun click_btnFav_updates_favorites_list() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<FavFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        val recipeBananaBread = onView(
            Matchers.allOf(withId(R.id.cardView),
                withChild(withChild(withText("Chocolate Chip Banana Bread"))),
                isDisplayed()))

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions
                .scrollToPosition<RecipesAdapter.ViewHolder>(1))
        onView(withText("Chocolate Chip Banana Bread")).check(matches(isDisplayed()))

        onView(withId(R.id.recycler)).perform(actionOnItemAtPosition<RecipesAdapter.ViewHolder>(0,
            click()))

        val imageButton = onView(
            Matchers.allOf(withId(R.id.btnFav),
                withParent(withChild(withText("Chocolate Chip Banana Bread"))),
                isDisplayed()))

        imageButton.check(matches(withTagValue(Matchers.equalTo(R.drawable.ic_baseline_favorite_24))))

        imageButton.perform(click())

        recipeBananaBread.check(ViewAssertions.doesNotExist())
    }

}