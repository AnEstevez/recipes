package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.repository.mockedRecipe
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetLocalRecipesTest {

    @Mock
    lateinit var recipesRepository: RecipesRepository

    private lateinit var getLocalRecipes: GetLocalRecipes

    @Before
    fun setUp() {
        getLocalRecipes = GetLocalRecipes(recipesRepository)
    }

    @Test
    fun `invoke calls recipesRepository`() {
        runBlocking {
            // GIVEN
            val recipes = listOf(mockedRecipe.copy(id = "rec01"))

            whenever(recipesRepository.getRecipesByRegion()).thenReturn(recipes)

            // WHEN
            val result = getLocalRecipes.invoke()

            // THEN
            verify(recipesRepository).getRecipesByRegion()
            assertEquals(recipes, result)
        }
    }
}