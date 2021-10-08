package com.andresestevez.data.repository

import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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
        runBlocking {
            // GIVEN
            val id = "rec00001"
            val recipe = mockedRecipe.copy(id = id)
            whenever(localDataSource.findById(id)).thenReturn(recipe)

            // WHEN
            val result = recipesRepository.findRecipeById(id)

            // THEN
            verify(localDataSource, times(1)).findById(id)
            verify(remoteDataSource, times(0)).findById(anyString(), anyString())
            assertEquals(recipe, result)
        }
    }

    @Test
    fun `findRecipeById calls remoteDataSource when no local data`() {
        runBlocking {
            // GIVEN
            val id = "rec00001"
            val recipe = mockedRecipe.copy(id = id)
            whenever(localDataSource.findById(id)).thenReturn(null)
            whenever(remoteDataSource.findById(apiKey, id)).thenReturn(recipe)

            // WHEN
            val result = recipesRepository.findRecipeById(id)

            // THEN
            assertEquals(recipe, result)
            verify(localDataSource, times(1)).findById(id)
            verify(remoteDataSource, times(1)).findById(apiKey, id)
            verify(localDataSource, times(1)).saveRecipe(recipe)

        }
    }

    @Test
    fun `getRecipesByRegion calls locationDataSource and gets from remoteDataSource`() {
        runBlocking {
            // GIVEN
            val spanishRecipes = listOf(mockedRecipe.copy(id = "rec00003"))
            val nationality = "spanish"
            whenever(locationDataSource.getLastLocationNationality()).thenReturn(nationality)
            whenever(remoteDataSource.listMealsByNationality(apiKey, nationality)).thenReturn(spanishRecipes)
            whenever(localDataSource.getFavorites()).thenReturn(emptyList())

            // WHEN
            val result = recipesRepository.getRecipesByRegion()

            // THEN
            verify(locationDataSource).getLastLocationNationality()
            verify(remoteDataSource).listMealsByNationality(apiKey, nationality)
            verify(localDataSource).getFavorites()
            assertEquals(spanishRecipes, result)
        }
    }

    @Test
    fun `getRecipesByName gets from remoteDataSource`() {
        runBlocking {
            // GIVEN
            val recipes = listOf(mockedRecipe.copy(id = "rec00003"))
            val name = "carne"
            whenever(remoteDataSource.listMealsByName(apiKey, name)).thenReturn(recipes)
            whenever(localDataSource.getFavorites()).thenReturn(emptyList())

            // WHEN
            val result = recipesRepository.getRecipesByName(name)

            // THEN
            verify(remoteDataSource).listMealsByName(apiKey, name)
            verify(localDataSource).getFavorites()
            assertEquals(recipes, result)
        }
    }

    @Test
    fun `updateRecipe calls localDataSource`() {
        runBlocking {
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
        runBlocking {
            // GIVEN
            val recipes = listOf(mockedRecipe.copy(id = "rec00003"))
            whenever(localDataSource.getFavorites()).thenReturn(recipes)

            // WHEN
            val result = recipesRepository.getFavorites()

            // THEN
            verify(localDataSource).getFavorites()
            assertEquals(recipes, result)
        }
    }

    @Test
    fun `checkFavorites updates recipes favorite flag`() {
        runBlocking {
            // GIVEN
            val recipes = listOf(mockedRecipe.copy(id = "rec01"), mockedRecipe.copy(id = "rec02"))
            val favoriteRecipes = listOf(mockedRecipe.copy(id = "rec02"))
            whenever(localDataSource.getFavorites()).thenReturn(favoriteRecipes)

            // WHEN
            recipesRepository.checkFavorites(recipes)

            // THEN
            verify(localDataSource).getFavorites()
            assertFalse(recipes[0].favorite)
            assertTrue(recipes[1].favorite)
        }
    }

    private val mockedRecipe = Recipe(
        "recipe01",
        "Carne o caldeiro",
        "http://www.cocinayrecetasfaciles.com/files/styles/receta_facebook/public/receta/carne-o-caldeiro.png-1.png",
        "Cortar la carne de ternera en trozos medianos. Ponerla en una cazuela.Añadir el unto y sal. Cubrirla con agua fría y llevar a ebullición.En el momento que comience a hervir disminuir la fuente de calor y cocer a fuego lento hasta que la carne esté tierna, 60/75 minutos aproximadamente.Retirar la carne cocida a otro recipiente con caldo de la cocción para mantenerla caliente.Cocer las patatas, peladas y enteras, en el resto del caldo 15/20′.Colocar la carne, las patatas y un poco de caldo en una fuente.Espolvorear con el pimentón elegido y rociar con aceite de oliva.Servir caliente.",
        "spanish",
        listOf("falda de ternera", "unto", "patatas", "Pimentón", "Aceite de oliva", "sal"),
        listOf("1,5kg", "75g", "12", "al gusto", "al gusto", "al gusto"),
        false,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )


}