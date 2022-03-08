package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.testshared.mockedRecipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
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
        runBlockingTest {
            // GIVEN
            val name = "carne"
            val recipes = listOf(mockedRecipe.copy(id = "rec01", name = name))

            whenever(recipesRepository.getRecipesByName(name)).thenReturn(flowOf(Result.success(recipes)))

            // WHEN
            val result = getRecipesByName(name).first()

            // THEN
            verify(recipesRepository).getRecipesByName(name)
            assertEquals(Result.success(recipes), result)
        }
    }
}