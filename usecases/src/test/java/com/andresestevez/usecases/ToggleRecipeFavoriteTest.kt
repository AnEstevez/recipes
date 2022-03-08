package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.testshared.mockedRecipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ToggleRecipeFavoriteTest {

    @Mock
    lateinit var recipesRepository: RecipesRepository

    private lateinit var toggleRecipeFavorite: ToggleRecipeFavorite

    @Before
    fun setUp() {
        toggleRecipeFavorite = ToggleRecipeFavorite(recipesRepository)
    }

    @Test
    fun `invoke calls recipesRepository`() {
        runBlockingTest {
            // GIVEN
            val recipe = mockedRecipe.copy(id = "rec02", favorite = false, dateModified = null)

            // WHEN
            toggleRecipeFavorite(recipe)

            // THEN
            verify(recipesRepository).updateRecipe(any())
        }
    }
}