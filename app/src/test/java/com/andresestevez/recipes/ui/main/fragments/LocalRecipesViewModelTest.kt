package com.andresestevez.recipes.ui.main.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andresestevez.data.repository.NoDataFoundException
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.data.CountryCodeToNationality
import com.andresestevez.recipes.ui.di.FakeLocalDataSource
import com.andresestevez.recipes.ui.di.FakeLocationDataSource
import com.andresestevez.recipes.ui.di.FakeRemoteDataSource
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalRecipesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private var apiKey: String = "1"
    private var localDataSource: LocalDataSource = FakeLocalDataSource()
    private var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    private var locationDataSource: LocationDataSource = FakeLocationDataSource()

    private lateinit var vm: LocalRecipesViewModel
    private lateinit var getLocalRecipes: GetLocalRecipes
    private lateinit var recipesRepository: RecipesRepository


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
    fun `when refresh, data is loaded`() = runBlockingTest {
        // GIVEN japanese nationality
        (locationDataSource as FakeLocationDataSource).location =
            CountryCodeToNationality.JP.nationality
        // WHEN
        vm.refresh()
        // THEN japanese recipes
        assertEquals(CountryCodeToNationality.JP.nationality, vm.state.value.data[0].country)

        // GIVEN italian nationality
        (locationDataSource as FakeLocationDataSource).location =
            CountryCodeToNationality.IT.nationality
        // WHEN
        vm.refresh()
        // THEN italian recipes
        assertEquals(CountryCodeToNationality.IT.nationality, vm.state.value.data[0].country)

    }

    @Test
    fun `when exception, user message is updated`() = runBlockingTest {
        // GIVEN
        (localDataSource as FakeLocalDataSource).exceptionToThrow = NoDataFoundException("No data")

        // WHEN
        vm.refresh()

        // THEN
        assertEquals(emptyList<Recipe>(), vm.state.value.data)
        assertEquals("No data", vm.state.value.userMessage)
    }

}