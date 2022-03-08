package com.andresestevez.recipes.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.andresestevez.data.repository.NoDataFoundException
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.recipes.ui.di.FakeLocalDataSource
import com.andresestevez.recipes.ui.di.FakeLocationDataSource
import com.andresestevez.recipes.ui.di.FakeRemoteDataSource
import com.andresestevez.recipes.ui.di.defaultFakeRecipes
import com.andresestevez.usecases.GetRecipeById
import com.andresestevez.usecases.ToggleRecipeFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private var apiKey: String = "1"
    var localDataSource: LocalDataSource = FakeLocalDataSource()
    var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    var locationDataSource: LocationDataSource = FakeLocationDataSource()

    private lateinit var vm: DetailViewModel
    private lateinit var getRecipeById: GetRecipeById
    private lateinit var toggleRecipeFavorite: ToggleRecipeFavorite
    private lateinit var recipesRepository: RecipesRepository

    companion object {
        private const val TONKATSU_RECIPE_ID = "53032"
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        recipesRepository =
            RecipesRepository(localDataSource, remoteDataSource, locationDataSource, apiKey)
        getRecipeById = GetRecipeById(recipesRepository)
        toggleRecipeFavorite = ToggleRecipeFavorite(recipesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `when init, recipe is retrieved`() = runBlockingTest {
        // GIVEN
        val stateHandle = SavedStateHandle().apply {
            set(DetailViewModel.RECIPE_ID_NAV_ARGS, TONKATSU_RECIPE_ID)
        }

        // WHEN
        vm = DetailViewModel(stateHandle, getRecipeById, toggleRecipeFavorite)

        // THEN
        assertEquals(TONKATSU_RECIPE_ID, vm.state.value.data?.id)
    }

    @Test
    fun `when exception, user message updated`() = runBlockingTest {
        // GIVEN
        val stateHandle = SavedStateHandle().apply {
            set(DetailViewModel.RECIPE_ID_NAV_ARGS, TONKATSU_RECIPE_ID)
        }

        (localDataSource as FakeLocalDataSource).exceptionToThrow = NoDataFoundException("No data")

        // WHEN
        vm = DetailViewModel(stateHandle, getRecipeById, toggleRecipeFavorite)

        // THEN
        assertEquals("No data", vm.state.value.userMessage)
    }

    @Test
    fun `when favorite clicked, the toggleRecipeFavorite use case is invoked (false to true)`() =
        runBlockingTest {
            // GIVEN
            val stateHandle = SavedStateHandle().apply {
                set(DetailViewModel.RECIPE_ID_NAV_ARGS, TONKATSU_RECIPE_ID)
            }

            val tonkatsu = defaultFakeRecipes.first { recipe -> recipe.id == TONKATSU_RECIPE_ID }

            localDataSource.updateRecipe(tonkatsu.copy(favorite = false))

            vm = DetailViewModel(stateHandle, getRecipeById, toggleRecipeFavorite)

            // WHEN
            vm.onFavoriteClicked()

            // THEN
            assertEquals(TONKATSU_RECIPE_ID, vm.state.value.data?.id)
            assertEquals(true, localDataSource.findById(TONKATSU_RECIPE_ID).first().favorite)

        }

    @Test
    fun `when favorite clicked, the toggleRecipeFavorite use case is invoked (true to false)`() =
        runBlockingTest {
            // GIVEN
            val stateHandle = SavedStateHandle().apply {
                set(DetailViewModel.RECIPE_ID_NAV_ARGS, TONKATSU_RECIPE_ID)
            }

            val tonkatsu = defaultFakeRecipes.first { recipe -> recipe.id == TONKATSU_RECIPE_ID }

            localDataSource.updateRecipe(tonkatsu.copy(favorite = true))

            vm = DetailViewModel(stateHandle, getRecipeById, toggleRecipeFavorite)

            // WHEN
            vm.onFavoriteClicked()

            // THEN
            assertEquals(TONKATSU_RECIPE_ID, vm.state.value.data?.id)
            assertEquals(false, localDataSource.findById(TONKATSU_RECIPE_ID).first().favorite)
        }
}