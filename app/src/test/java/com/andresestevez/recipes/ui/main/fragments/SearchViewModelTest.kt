package com.andresestevez.recipes.ui.main.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andresestevez.recipes.ui.common.Event
import com.andresestevez.testshared.mockedRecipe
import com.andresestevez.usecases.GetRecipesByName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
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
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever




@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @Mock
    lateinit var getRecipesByName: GetRecipesByName

    private lateinit var vm: SearchViewModel

    @Mock
    lateinit var observerUiModel: Observer<SearchViewModel.UiModel>

    @Mock
    lateinit var observerNavigation: Observer<Event<String>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        vm = SearchViewModel(getRecipesByName)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when refresh, HideKeyboard is called`() = runBlockingTest {
        // GIVEN
        val name = "fabada"
        val recipes = listOf(mockedRecipe.copy(id = "777", name = "Fabada asturiana"))
        whenever(getRecipesByName.invoke(name)).thenReturn(recipes)
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh(name)

        // THEN
        verify(observerUiModel, timeout(100)).onChanged(SearchViewModel.UiModel.HideKeyboard)
        vm.model.removeObserver(observerUiModel)

    }

    @Test
    fun `when refresh, Loading is called`() = runBlockingTest {
        // GIVEN
        val name = "fabada"
        val recipes = listOf(mockedRecipe.copy(id = "777", name = "Fabada asturiana"))
        whenever(getRecipesByName.invoke(name)).thenReturn(recipes)
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh(name)

        // THEN
        verify(observerUiModel, timeout(100)).onChanged(SearchViewModel.UiModel.Loading)
        vm.model.removeObserver(observerUiModel)
    }

    @Test
    fun `when refresh, Content is called`() = runBlockingTest {
        // GIVEN
        val name = "fabada"
        val recipes = listOf(mockedRecipe.copy(id = "777", name = "Fabada asturiana"))
        whenever(getRecipesByName.invoke(name)).thenReturn(recipes)
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh(name)

        // THEN
        verify(observerUiModel, timeout(100)).onChanged(SearchViewModel.UiModel.Content(recipes))
        assertEquals(recipes, (vm.model.value as SearchViewModel.UiModel.Content).recipes)
        vm.model.removeObserver(observerUiModel)
    }

    @Test
    fun `when onRecipeClicked, navigation value is updated`() {
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