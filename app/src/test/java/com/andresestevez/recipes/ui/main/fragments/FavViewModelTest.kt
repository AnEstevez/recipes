package com.andresestevez.recipes.ui.main.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andresestevez.data.repository.NoDataFoundException
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.recipes.ui.di.FakeLocalDataSource
import com.andresestevez.recipes.ui.di.FakeLocationDataSource
import com.andresestevez.recipes.ui.di.FakeRemoteDataSource
import com.andresestevez.usecases.GetFavoriteRecipes
import com.andresestevez.usecases.ToggleRecipeFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class FavViewModelTest {

    private var apiKey: String = "1"
    var localDataSource: LocalDataSource = FakeLocalDataSource()
    var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    var locationDataSource: LocationDataSource = FakeLocationDataSource()

    private lateinit var vm: FavViewModel
    private lateinit var getFavoriteRecipes: GetFavoriteRecipes
    private lateinit var toggleRecipeFavorite: ToggleRecipeFavorite
    private lateinit var recipesRepository: RecipesRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        recipesRepository =
            RecipesRepository(localDataSource, remoteDataSource, locationDataSource, apiKey)
        getFavoriteRecipes = GetFavoriteRecipes(recipesRepository)
        toggleRecipeFavorite = ToggleRecipeFavorite(recipesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `when init, favorite recipes are retrieved`() = runBlockingTest {
        // GIVEN -> viewmodel
        // WHEN -> init
        vm = FavViewModel(getFavoriteRecipes, toggleRecipeFavorite)

        // THEN
        assertEquals(1, vm.state.value.data.size)
        assertEquals(true, vm.state.value.data.first().bookmarked)

    }

    @Test
    fun `when exception, user message is updated`() = runBlockingTest {
        // GIVEN
        (localDataSource as FakeLocalDataSource).exceptionToThrow = NoDataFoundException()

        // WHEN
        vm = FavViewModel(getFavoriteRecipes, toggleRecipeFavorite)

        // THEN
        val expectedResult = FavViewModel.UiState(loading = false,
            data = emptyList(),
            userMessage = NoDataFoundException().message)
        assertEquals(expectedResult, vm.state.value)

    }

}