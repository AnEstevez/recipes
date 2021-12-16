package com.andresestevez.recipes.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.runBlocking
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
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailIntegrationTest {

    @get:Rule
    val instanteTaskrule = InstantTaskExecutorRule()

    private var apiKey: String = "1"
    var localDataSource: LocalDataSource = FakeLocalDataSource()
    var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    var locationDataSource: LocationDataSource = FakeLocationDataSource()

    private lateinit var vm: DetailViewModel
    private lateinit var getRecipeById: GetRecipeById
    private lateinit var toggleRecipeFavorite: ToggleRecipeFavorite
    private lateinit var recipesRepository: RecipesRepository

    @Mock
    lateinit var observer: Observer<DetailViewModel.UiModel>

    private val testDispatcher = TestCoroutineDispatcher()

    companion object {
        private const val TONKATSU_RECIPE_ID = "53032"
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val stateHandle = SavedStateHandle()
        stateHandle.set(DetailViewModel.RECIPE_ID_NAV_ARGS, TONKATSU_RECIPE_ID)
        recipesRepository =
            RecipesRepository(localDataSource, remoteDataSource, locationDataSource, apiKey)
        getRecipeById = GetRecipeById(recipesRepository)
        toggleRecipeFavorite = ToggleRecipeFavorite(recipesRepository)
        vm = DetailViewModel(stateHandle, getRecipeById, toggleRecipeFavorite)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `observing LiveData finds the recipe`() = runBlockingTest {
        // GIVEN
        // WHEN
        vm.model.observeForever(observer)

        // THEN
        verify(observer).onChanged(vm.model.value)
        assertEquals(defaultFakeRecipes.first { it.id == TONKATSU_RECIPE_ID },
            vm.model.value!!.recipe)

        vm.model.removeObserver(observer)
    }

    @Test
    fun `refresh loads recipe details`() = runBlockingTest {

        // GIVEN
        val recipe = defaultFakeRecipes.first { it.id == TONKATSU_RECIPE_ID }
        runBlockingTest {
            // WHEN
            vm.refresh()
        }
        // THEN
        assertEquals(recipe, vm.model.value?.recipe)

    }

    @Test(expected = IllegalStateException::class)
    fun `refresh trows exception when recipe id not found`() = runBlocking {
        // GIVEN
        val vmWithoutRecipeId =
            DetailViewModel(SavedStateHandle(), getRecipeById, toggleRecipeFavorite)

        // WHEN
        vmWithoutRecipeId.refresh()

        // THEN exception expected

    }

    @Test
    fun `when favorite clicked, the toggleRecipeFavorite use case is invoked (false to true)`() =
        runBlockingTest {
            // GIVEN
            val recipe =
                defaultFakeRecipes.first { it.id == TONKATSU_RECIPE_ID }.copy(favorite = false)
            vm.setNewValue(DetailViewModel.UiModel(recipe))
            vm.model.observeForever(observer)

            // WHEN
            vm.onFavoriteClicked()

            // THEN
            verify(observer).onChanged(vm.model.value)

            assertEquals(true, vm.model.value!!.recipe.favorite)

            vm.model.removeObserver(observer)
        }


    @Test
    fun `when favorite clicked, the toggleRecipeFavorite use case is invoked (true to false)`() =
        runBlockingTest {
            // GIVEN
            val recipe =
                defaultFakeRecipes.first { it.id == TONKATSU_RECIPE_ID }.copy(favorite = true)
            vm.setNewValue(DetailViewModel.UiModel(recipe))
            vm.model.observeForever(observer)

            // WHEN
            vm.onFavoriteClicked()

            // THEN
            verify(observer, atLeastOnce()).onChanged(any())
            assertEquals(false, vm.model.value!!.recipe.favorite)

            vm.model.removeObserver(observer)
        }
}