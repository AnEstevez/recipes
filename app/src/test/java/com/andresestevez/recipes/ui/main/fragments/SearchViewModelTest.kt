package com.andresestevez.recipes.ui.main.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andresestevez.data.repository.NoDataFoundException
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.ui.common.Event
import com.andresestevez.recipes.ui.di.FakeLocalDataSource
import com.andresestevez.recipes.ui.di.FakeLocationDataSource
import com.andresestevez.recipes.ui.di.FakeRemoteDataSource
import com.andresestevez.recipes.ui.di.defaultFakeRecipes
import com.andresestevez.testshared.mockedRecipe
import com.andresestevez.usecases.GetRecipesByName
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
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var apiKey: String = "1"
    private var localDataSource: LocalDataSource = FakeLocalDataSource()
    private var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    private var locationDataSource: LocationDataSource = FakeLocationDataSource()

    private lateinit var vm: SearchViewModel
    private lateinit var getRecipesByName: GetRecipesByName
    private lateinit var recipesRepository: RecipesRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var observerNavigation: Observer<Event<String>>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        recipesRepository =
            RecipesRepository(localDataSource, remoteDataSource, locationDataSource, apiKey)
        getRecipesByName = GetRecipesByName(recipesRepository)
        vm = SearchViewModel(getRecipesByName)
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
        assertEquals(defaultFakeRecipes.filter { recipe -> recipe.name.lowercase().contains(name) },
            vm.state.value.data)
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

    @Test
    fun `when onRecipeClicked, navigation value is updated`() = runBlockingTest {
        // GIVEN
        val recipe = mockedRecipe.copy(id = "777")
        vm.navigation.observeForever(observerNavigation)

        // WHEN
        vm.onRecipeClicked(recipe)

        // THEN
        verify(observerNavigation).onChanged(any())
        assertEquals(recipe.id, vm.navigation.value?.getContentIfNotHandled())

        vm.navigation.removeObserver(observerNavigation)
    }
}