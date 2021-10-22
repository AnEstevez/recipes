package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.testshared.mockedRecipe
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

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
        runBlocking {
            // GIVEN
            val recipe = mockedRecipe.copy(id = "rec02", favorite = false, dateModified = null)

            // WHEN
            val result = toggleRecipeFavorite.invoke(recipe)

            // THEN
            verify(recipesRepository).updateRecipe(any())
            assertEquals(recipe.id, result.id)
        }
    }

    @Test
    fun `unfavorite recipe becomes favorite`() {
        runBlocking {
            // GIVEN
            val recipe = mockedRecipe.copy(id = "rec02", favorite = false, dateModified = null)

            // WHEN
            val result = toggleRecipeFavorite.invoke(recipe)

            // THEN
            verify(recipesRepository).updateRecipe(any())
            assertEquals(recipe.id, result.id)
            assertTrue(result.favorite)
            assertNotNull(result.dateModified)
        }
    }

    @Test
    fun `favorite recipe becomes unfavorite`() {
        runBlocking {
            // GIVEN
            val recipe = mockedRecipe.copy(id = "rec02", favorite = true, dateModified = null)

            // WHEN
            val result = toggleRecipeFavorite.invoke(recipe)

            // THEN
            verify(recipesRepository).updateRecipe(any())
            assertEquals(recipe.id, result.id)
            assertFalse(result.favorite)
            assertNotNull(result.dateModified)
        }
    }
}