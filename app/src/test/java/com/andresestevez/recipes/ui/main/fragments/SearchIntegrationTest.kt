package com.andresestevez.recipes.ui.main.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchIntegrationTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var apiKey: String = "1"
    private var localDataSource: LocalDataSource = FakeLocalDataSource()
    private var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    private var locationDataSource: LocationDataSource = FakeLocationDataSource()

    private lateinit var vm: SearchViewModel
    private lateinit var getRecipesByName: GetRecipesByName
    private lateinit var recipesRepository: RecipesRepository

    @Mock
    lateinit var observerUiModel: Observer<SearchViewModel.UiModel>

    @Mock
    lateinit var observerNavigation: Observer<Event<String>>

    private val testDispatcher = TestCoroutineDispatcher()

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
    fun `when refresh, HideKeyboard is called`() = runBlockingTest {
        // GIVEN
        val name = "pork"
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh(name)

        // THEN
        verify(observerUiModel).onChanged(SearchViewModel.UiModel.HideKeyboard)

        vm.model.removeObserver(observerUiModel)

    }

    @Test
    fun `when refresh, Loading is called`() = runBlockingTest {
        // GIVEN
        val name = "pork"
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh(name)

        // THEN
        verify(observerUiModel).onChanged(SearchViewModel.UiModel.Loading)

        vm.model.removeObserver(observerUiModel)
    }

    @Test
    fun `when refresh, Content is called`() = runBlockingTest {
        // GIVEN
        val name = "pork"
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh(name)

        // THEN
        verify(observerUiModel).onChanged(SearchViewModel.UiModel.Content(
            defaultFakeRecipes.filter { it.name.contains(name) }))
        assertEquals(defaultFakeRecipes.filter { it.name.contains(name) },
            (vm.model.value as SearchViewModel.UiModel.Content).recipes)

        vm.model.removeObserver(observerUiModel)
    }

    @Test
    fun `when onRecipeClicked, navigation value is updated`() = runBlockingTest {
        // GIVEN
        vm.navigation.observeForever(observerNavigation)

        // WHEN
        vm.onRecipeClicked(mockedRecipe)

        // THEN
        verify(observerNavigation).onChanged(any())
        assertEquals(mockedRecipe.id, vm.navigation.value?.getContentIfNotHandled())

        vm.navigation.removeObserver(observerNavigation)
    }
}