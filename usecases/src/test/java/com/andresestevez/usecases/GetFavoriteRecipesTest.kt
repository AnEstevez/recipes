package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.testshared.mockedRecipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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

            whenever(recipesRepository.getFavorites()).thenReturn(flowOf(Result.success(recipes)))

            // WHEN
            val result = getFavoriteRecipes().first()

            // THEN
            verify(recipesRepository).getFavorites()
            assertEquals(Result.success(recipes), result)
        }
    }
}