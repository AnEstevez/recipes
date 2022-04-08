package com.andresestevez.recipes.presentation.main.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andresestevez.data.repository.NoDataFoundException
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.presentation.di.FakeLocalDataSource
import com.andresestevez.recipes.presentation.di.FakeLocationDataSource
import com.andresestevez.recipes.presentation.di.FakeRemoteDataSource
import com.andresestevez.recipes.presentation.di.defaultFakeRecipes
import com.andresestevez.usecases.GetRecipesByName
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
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var localDataSource: LocalDataSource = FakeLocalDataSource()
    private var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    private var locationDataSource: LocationDataSource = FakeLocationDataSource()

    private lateinit var vm: SearchViewModel
    private lateinit var getRecipesByName: GetRecipesByName
    private lateinit var toggleRecipeFavorite: ToggleRecipeFavorite
    private lateinit var recipesRepository: RecipesRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        recipesRepository =
            RecipesRepository(localDataSource, remoteDataSource, locationDataSource)
        getRecipesByName = GetRecipesByName(recipesRepository)
        toggleRecipeFavorite = ToggleRecipeFavorite(recipesRepository)
        vm = SearchViewModel(getRecipesByName, toggleRecipeFavorite)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `when refresh, data is updated`() = runBlockingTest {
        // GIVEN
        val name = "chicken"

        // WHEN
        vm.refresh(name)

        // THEN
        assertEquals(defaultFakeRecipes.filter { recipe -> recipe.name.lowercase().contains(name) }.map { it.name },
            vm.state.value.data.map { it.title })
        assertEquals(2, vm.state.value.data.size)
    }

    @Test
    fun `when exception, user message is updated`() = runBlockingTest {
        // GIVEN
        val name = "chicken"
        (localDataSource as FakeLocalDataSource).exceptionToThrow = NoDataFoundException("No data")

        // WHEN
        vm.refresh(name)

        // THEN
        assertEquals("No data", vm.state.value.userMessage)
        assertEquals(emptyList<Recipe>(), vm.state.value.data)

    }

}