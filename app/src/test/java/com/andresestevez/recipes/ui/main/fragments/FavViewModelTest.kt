package com.andresestevez.recipes.ui.main.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andresestevez.recipes.ui.common.Event
import com.andresestevez.recipes.ui.main.fragments.FavViewModel.UiModel
import com.andresestevez.testshared.mockedRecipe
import com.andresestevez.usecases.GetFavoriteRecipes
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
class FavViewModelTest {

    @Mock
    lateinit var getFavoriteRecipes: GetFavoriteRecipes

    private lateinit var vm: FavViewModel

    @Mock
    lateinit var observerUiModel: Observer<UiModel>

    @Mock
    lateinit var observerNavigation: Observer<Event<String>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        vm = FavViewModel(getFavoriteRecipes)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when refresh, loading is shown`() {
        // GIVEN
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh()

        // THEN
        verify(observerUiModel, timeout(100)).onChanged(UiModel.Loading)
        vm.model.removeObserver(observerUiModel)

    }

    @Test
    fun `when refresh, getFavoriteRecipes is called`() = runBlockingTest {
        // GIVEN
        val recipes = listOf(mockedRecipe.copy(id = "777"))
        whenever(getFavoriteRecipes.invoke()).thenReturn(recipes)
        vm.model.observeForever(observerUiModel)

        // WHEN
        vm.refresh()

        // THEN
        verify(observerUiModel, timeout(100)).onChanged(UiModel.Content(recipes))
        verify(getFavoriteRecipes).invoke()
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