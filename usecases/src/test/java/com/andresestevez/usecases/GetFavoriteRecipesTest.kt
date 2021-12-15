package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.testshared.mockedRecipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetFavoriteRecipesTest {

    @Mock lateinit var recipesRepository: RecipesRepository

    private lateinit var getFavoriteRecipes: GetFavoriteRecipes

    @Before
    fun setUp() {
        getFavoriteRecipes = GetFavoriteRecipes(recipesRepository)
    }

    @Test
    fun `invoke calls recipesRepository`() {
        runBlockingTest {
            // GIVEN
            val recipes = listOf(mockedRecipe.copy(id = "rec01"))

            whenever(recipesRepository.getFavorites()).thenReturn(recipes)

            // WHEN
            val result = getFavoriteRecipes.invoke()

            // THEN
            verify(recipesRepository).getFavorites()
            assertEquals(recipes, result)
        }
    }
}