package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.testshared.mockedRecipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class GetRecipeByIdTest {

    @Mock
    lateinit var recipesRepository: RecipesRepository

    private lateinit var getRecipeById: GetRecipeById

    @Before
    fun setUp() {
        getRecipeById = GetRecipeById(recipesRepository)
    }

    @Test
    fun `invoke calls recipesRepository`() {
        runBlockingTest {
            // GIVEN
            val recipeId = "rec06"
            val recipe = mockedRecipe.copy(id = recipeId)

            whenever(recipesRepository.findRecipeById(recipeId)).thenReturn(flowOf(Result.success(recipe)))

            // WHEN
            val result = getRecipeById(recipeId).first()

            // THEN
            verify(recipesRepository).findRecipeById(recipeId)
            assertEquals(Result.success(recipe), result)
        }
    }
}