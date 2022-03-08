package com.andresestevez.data.repository

import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.testshared.mockedRecipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RecipesRepositoryTest {

    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var locationDataSource: LocationDataSource
    private val apiKey: String = "123456"

    private lateinit var recipesRepository: RecipesRepository

    @Before
    fun setUp() {
        recipesRepository =
            RecipesRepository(localDataSource, remoteDataSource, locationDataSource, apiKey)
    }

    @Test
    fun `findRecipeById calls and retrieves data from localDataSource`() {
        runBlockingTest {
            // GIVEN
            val id = "rec00001"
            val recipe = mockedRecipe.copy(id = id)
            whenever(localDataSource.findById(id)).thenReturn(flowOf(recipe))

            // WHEN
            val result = recipesRepository.findRecipeById(id).first()

            // THEN
            verify(localDataSource, times(1)).findById(id)
            verify(remoteDataSource, times(0)).findById(anyString(), anyString())
            assertEquals(Result.success(recipe), result)
        }
    }

    /**
     * remoteDataSource is called when "instructions" field is empty in local data
     */
    @Test
    fun `findRecipeById calls remoteDataSource when no complete local data`() {
        runBlockingTest {
            // GIVEN
            val id = "rec00001"
            val localRecipe = mockedRecipe.copy(id = id, instructions = "")
            val remoteRecipe = mockedRecipe.copy(id = id)

            whenever(localDataSource.findById(id)).thenReturn(flowOf(localRecipe))
            whenever(remoteDataSource.findById(apiKey, id)).thenReturn(Result.success(remoteRecipe))

            // WHEN
            recipesRepository.findRecipeById(id).collect()

            // THEN
            verify(localDataSource, times(1)).findById(id)
            verify(remoteDataSource, times(1)).findById(apiKey, id)
            verify(localDataSource, times(1)).updateRecipe(remoteRecipe)

        }
    }

    @Test
    fun `getRecipesByRegion calls remoteDataSource and gets from locationDataSource`() {
        runBlockingTest {
            // GIVEN
            val spanishRecipesWithoutCountry =
                listOf(mockedRecipe.copy(id = "tortilla", country = ""))
            val spanishRecipes = listOf(mockedRecipe.copy(id = "tortilla"))

            val nationality = "spanish"
            whenever(remoteDataSource.listMealsByNationality(apiKey, nationality)).thenReturn(
                Result.success(spanishRecipesWithoutCountry))
            whenever(locationDataSource.getLastLocationNationality()).thenReturn(nationality)
            whenever(localDataSource.searchByCountry(nationality)).thenReturn(flowOf(spanishRecipes))

            // WHEN
            var result = recipesRepository.getRecipesByRegion().last()

            // THEN
            verify(locationDataSource).getLastLocationNationality()
            verify(remoteDataSource).listMealsByNationality(apiKey, nationality)
            verify(localDataSource).saveAll(spanishRecipes)
            verify(localDataSource).searchByCountry(nationality)
            assertEquals(Result.success(spanishRecipes), result)
        }
    }

    @Test
    fun `GIVEN localDataSource and remoteDataSource empty WHEN getRecipesByRegion THEN NoDataFoundException`() {
        runBlockingTest {
            // GIVEN
            val spanishRecipesWithoutCountry =
                listOf(mockedRecipe.copy(id = "tortilla", country = ""))
            val spanishRecipes = listOf(mockedRecipe.copy(id = "tortilla"))

            val nationality = "spanish"
            whenever(remoteDataSource.listMealsByNationality(apiKey, nationality)).thenReturn(
                Result.failure(NoDataFoundException()))
            whenever(locationDataSource.getLastLocationNationality()).thenReturn(nationality)
            whenever(localDataSource.searchByCountry(nationality)).thenReturn(flowOf(emptyList()))

            // WHEN
            var result = recipesRepository.getRecipesByRegion().last()

            // THEN
            verify(locationDataSource).getLastLocationNationality()
            verify(remoteDataSource).listMealsByNationality(apiKey, nationality)
            verify(localDataSource).searchByCountry(nationality)
            assertTrue(result.fold({}) { it } is NoDataFoundException)
            assertEquals("No data found", result.fold({}) { it.message })
        }
    }

    @Test
    fun `getRecipesByName calls localDataSource and gets from remoteDataSource`() {
        runBlockingTest {
            // GIVEN
            val localRecipes = listOf(mockedRecipe.copy(id = "rec00003", name = "Eggs Benedict"))
            val remoteRecipes = listOf(mockedRecipe.copy(id = "rec00003", name = "Eggs Benedict"),
                mockedRecipe.copy(id = "rec00004", name = "Fried Eggs"))

            val name = "eggs"
            whenever(remoteDataSource.listMealsByName(apiKey, name)).thenReturn(Result.success(
                remoteRecipes))
            whenever(localDataSource.searchByName(name)).thenReturn(flowOf(localRecipes))

            // WHEN
            recipesRepository.getRecipesByName(name).collect()

            // THEN
            verify(localDataSource).searchByName(name)
            verify(remoteDataSource).listMealsByName(apiKey, name)
            verify(localDataSource).saveAll(remoteRecipes)
        }
    }

    @Test
    fun `GIVEN localDataSource and remoteDataSource empty WHEN getRecipesByName THEN NoDataFoundException`() {
        runBlockingTest {
            // GIVEN
            val recipes = listOf(mockedRecipe.copy(id = "rec00003", name = "Eggs Benedict"))
            val name = "eggs"
            whenever(remoteDataSource.listMealsByName(apiKey, name)).thenReturn(Result.failure(
                NoDataFoundException()))
            whenever(localDataSource.searchByName(name)).thenReturn(flowOf(emptyList()))

            // WHEN
            val result = recipesRepository.getRecipesByName(name).last()

            // THEN
            verify(remoteDataSource).listMealsByName(apiKey, name)
            verify(localDataSource).searchByName(name)
            assertTrue(result.fold({}) { it } is NoDataFoundException)
            assertEquals("No data found", result.fold({}) { it.message })
        }
    }

    @Test
    fun `updateRecipe calls localDataSource`() {
        runBlockingTest {
            // GIVEN
            val recipe = mockedRecipe.copy(id = "rec00003")

            // WHEN
            recipesRepository.updateRecipe(recipe)

            // THEN
            verify(localDataSource).updateRecipe(recipe)
        }
    }

    @Test
    fun `getFavorites calls localDataSource`() {
        runBlockingTest {
            // GIVEN
            val recipes = listOf(mockedRecipe.copy(id = "rec00003"))
            whenever(localDataSource.getFavorites()).thenReturn(flowOf(recipes))

            // WHEN
            val result = recipesRepository.getFavorites().first()

            // THEN
            verify(localDataSource).getFavorites()
            assertEquals(Result.success(recipes), result)
        }
    }

    @Test
    fun `getFavorites gets Result failure when localDataSource throws exception`() {
        runBlockingTest {
            // GIVEN
            val exception = Exception("localDataSource Exception")
            whenever(localDataSource.getFavorites()).thenReturn(flow <List<Recipe>> { throw exception })

            // WHEN
            val result = recipesRepository.getFavorites().first()

            // THEN
            verify(localDataSource).getFavorites()
            assertEquals(Result.failure<List<Recipe>>(exception), result)
        }
    }
}