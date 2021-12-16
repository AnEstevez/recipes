package com.andresestevez.recipes.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.andresestevez.testshared.mockedRecipe
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
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @Mock
    lateinit var getRecipeById: GetRecipeById

    @Mock
    lateinit var toggleRecipeFavorite: ToggleRecipeFavorite

    private lateinit var vm: DetailViewModel

    @Mock
    lateinit var observer: Observer<DetailViewModel.UiModel>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val stateHandle = SavedStateHandle()
        stateHandle.set(DetailViewModel.RECIPE_ID_NAV_ARGS, "777")
        vm = DetailViewModel(stateHandle, getRecipeById, toggleRecipeFavorite)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `observing LiveData finds the recipe`() = runBlockingTest {
        // GIVEN
        val recipe = mockedRecipe.copy(id = "777", favorite = false)
        whenever(getRecipeById.invoke("777")).thenReturn(recipe)

        // WHEN
        vm.model.observeForever(observer)

        // THEN
        verify(observer).onChanged(vm.model.value)

        vm.model.removeObserver(observer)
    }

    @Test
    fun `refresh calls getRecipeById`() = runBlockingTest {
        // GIVEN
        val recipe = mockedRecipe.copy(id = "777", favorite = false)
        whenever(getRecipeById.invoke("777")).thenReturn(recipe)

        // WHEN
        vm.refresh()

        // THEN
        verify(getRecipeById, timeout(100)).invoke("777")
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
            val recipe = mockedRecipe.copy(id = "777", favorite = false)
            val recipeClicked = recipe.copy(favorite = true)
            whenever(toggleRecipeFavorite.invoke(recipe)).thenReturn(recipeClicked)
            vm.setNewValue(DetailViewModel.UiModel(recipe))
            vm.model.observeForever(observer)

            // WHEN
            vm.onFavoriteClicked()

            // THEN
            verify(toggleRecipeFavorite).invoke(recipe)
            assertEquals(recipeClicked, vm.model.value?.recipe)

            vm.model.removeObserver(observer)
        }


    @Test
    fun `when favorite clicked, the toggleRecipeFavorite use case is invoked (true to false)`() =
        runBlockingTest {
            // GIVEN
            val recipe = mockedRecipe.copy(id = "777", favorite = true)
            val recipeClicked = recipe.copy(favorite = false)
            whenever(toggleRecipeFavorite.invoke(recipe)).thenReturn(recipeClicked)
            vm.setNewValue(DetailViewModel.UiModel(recipe))
            vm.model.observeForever(observer)

            // WHEN
            vm.onFavoriteClicked()

            // THEN
            verify(toggleRecipeFavorite).invoke(recipe)
            assertEquals(recipeClicked, vm.model.value?.recipe)

            vm.model.removeObserver(observer)
        }
}