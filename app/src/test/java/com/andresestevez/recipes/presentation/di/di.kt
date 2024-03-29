package com.andresestevez.recipes.presentation.di

import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.framework.CountryCodeToNationality
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

val mockedRecipe = Recipe(
    "recipe01",
    "Carne o caldeiro",
    "http://www.cocinayrecetasfaciles.com/files/styles/receta_facebook/public/receta/carne-o-caldeiro.png-1.png",
    "Cortar la carne de ternera en trozos medianos. Ponerla en una cazuela.Añadir el unto y sal. Cubrirla con agua fría y llevar a ebullición.En el momento que comience a hervir disminuir la fuente de calor y cocer a fuego lento hasta que la carne esté tierna, 60/75 minutos aproximadamente.Retirar la carne cocida a otro recipiente con caldo de la cocción para mantenerla caliente.Cocer las patatas, peladas y enteras, en el resto del caldo 15/20′.Colocar la carne, las patatas y un poco de caldo en una fuente.Espolvorear con el pimentón elegido y rociar con aceite de oliva.Servir caliente.",
    CountryCodeToNationality.ES.nationality,
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

val defaultFakeRecipes = mutableListOf(
    mockedRecipe.copy("1",
        "Chocolate Chip Banana Bread",
        favorite = true,
        country = CountryCodeToNationality.FR.nationality),
    mockedRecipe.copy("2", "Chicken Chow Mein", country = CountryCodeToNationality.CH.nationality),
    mockedRecipe.copy("3", "Chicken Parmesan", country = CountryCodeToNationality.IT.nationality),
    mockedRecipe.copy("4", "French Onion Soup", country = CountryCodeToNationality.FR.nationality),
    mockedRecipe.copy("53032", "Tonkatsu pork", country = CountryCodeToNationality.JP.nationality)
)

class FakeLocalDataSource(var exceptionToThrow: Throwable? = null) : LocalDataSource {

    var recipes: MutableList<Recipe> = defaultFakeRecipes

    override fun findById(recipeId: String): Flow<Recipe> {
        exceptionToThrow?.let { throw it } ?: return flowOf(recipes.first { it.id == recipeId })
    }

    override suspend fun saveRecipe(recipe: Recipe?) {
        exceptionToThrow?.let { throw it } ?: recipe?.let {
            if (!recipes.contains(it)) {
                recipes.add(it)
            }
        }
    }

    override fun getFavorites(): Flow<List<Recipe>> =
        exceptionToThrow?.let { throw it } ?: flowOf(recipes.filter { it.favorite })

    override suspend fun updateRecipe(recipe: Recipe) {
        exceptionToThrow?.let { throw it } ?: recipes.indexOfFirst { it.id == recipe.id }.let {
            recipes.removeAt(it)
            recipes.add(it, recipe)
        }
    }

    override suspend fun saveAll(newRecipes: List<Recipe>) {
        exceptionToThrow?.let { throw it } ?: newRecipes.forEach {
            if (!recipes.contains(it)) {
                recipes.add(it)
            }
        }
    }

    override fun searchByCountry(country: String): Flow<List<Recipe>> =
        exceptionToThrow?.let { throw it } ?: flowOf(recipes.filter { it.country == country })

    override fun searchByName(name: String): Flow<List<Recipe>> =
        exceptionToThrow?.let { throw it } ?: flowOf(recipes.filter {
            it.name.lowercase().contains(name.lowercase())
        })
}

class FakeRemoteDataSource() : RemoteDataSource {

    var recipes: MutableList<Recipe> = defaultFakeRecipes

    override suspend fun findById(recipeId: String): Result<Recipe> =
        Result.success(recipes.first { it.id == recipeId })

    override suspend fun listMealsByNationality(
        nationality: String,
    ): Result<List<Recipe>> =
        Result.success(recipes.filter { it.country == nationality })

    override suspend fun listMealsByName(name: String): Result<List<Recipe>> =
        Result.success(recipes.filter { it.name.contains(name) })
}

class FakeLocationDataSource : LocationDataSource {

    var location = CountryCodeToNationality.ES.nationality

    override suspend fun getLastLocationNationality(): String = location
}