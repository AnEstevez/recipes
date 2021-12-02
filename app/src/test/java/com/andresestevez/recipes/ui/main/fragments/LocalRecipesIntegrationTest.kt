package com.andresestevez.recipes.ui.main.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.recipes.data.CountryCodeToNationality
import com.andresestevez.recipes.ui.common.Event
import com.andresestevez.recipes.ui.di.FakeLocalDataSource
import com.andresestevez.recipes.ui.di.FakeLocationDataSource
import com.andresestevez.recipes.ui.di.FakeRemoteDataSource
import com.andresestevez.recipes.ui.di.defaultFakeRecipes
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModel.UiModel
import com.andresestevez.testshared.mockedRecipe
import com.andresestevez.usecases.GetLocalRecipes
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
class LocalRecipesIntegrationTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var apiKey: String = "1"
    private var localDataSource: LocalDataSource = FakeLocalDataSource()
    private var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    private var locationDataSource: LocationDataSource = FakeLocationDataSource()

    private lateinit var vm: LocalRecipesViewModel
    private lateinit var getLocalRecipes: GetLocalRecipes
    private lateinit var recipesRepository: RecipesRepository

    @Mock
    lateinit var observerUiModel: Observer<UiModel>

    @Mock
    lateinit var observerNavigation: Observer<Event<String>>

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        recipesRepository =
            RecipesRepository(localDataSource, remoteDataSource, locationDataSource, apiKey)
        getLocalRecipes = GetLocalRecipes(recipesRepository)
        vm = LocalRecipesViewModel(getLocalRecipes)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `when refresh, loading is shown`() = runBlockingTest {
        // GIVEN
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh()

        // THEN
        verify(observerUiModel).onChanged(UiModel.Loading)

        vm.model.removeObserver(observerUiModel)

    }

    @Test
    fun `when refresh, local recipes are retrieved`() = runBlockingTest {
        // GIVEN
        val spanishRecipe = mockedRecipe.copy(country = CountryCodeToNationality.SP.nationality)
        defaultFakeRecipes.add(spanishRecipe)
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh()

        // THEN
        verify(observerUiModel).onChanged(UiModel.Loading)
        assertEquals(defaultFakeRecipes.filter { it.country == CountryCodeToNationality.SP.nationality },
            (vm.model.value as UiModel.Content).recipes)

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