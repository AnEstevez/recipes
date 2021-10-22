package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.testshared.mockedRecipe
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetRecipesByNameTest {

    @Mock
    lateinit var recipesRepository: RecipesRepository

    private lateinit var getRecipesByName: GetRecipesByName

    @Before
    fun setUp() {
        getRecipesByName = GetRecipesByName(recipesRepository)
    }

    @Test
    fun `invoke calls recipesRepository`() {
        runBlocking {
            // GIVEN
            val name = "carne"
            val recipes = listOf(mockedRecipe.copy(id = "rec01", name = name))

            whenever(recipesRepository.getRecipesByName(name)).thenReturn(recipes)

            // WHEN
            val result = getRecipesByName.invoke(name)

            // THEN
            verify(recipesRepository).getRecipesByName(name)
            assertEquals(recipes, result)
        }
    }
}