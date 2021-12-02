package com.andresestevez.data.repository

import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe

class RecipesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val locationDataSource: LocationDataSource,
    private val apiKey: String
    ) {

    suspend fun findRecipeById(recipeId: String): Recipe? {
        var recipe = localDataSource.findById(recipeId)
        if (recipe == null) {
            recipe = remoteDataSource.findById(apiKey, recipeId).also { localDataSource.saveRecipe(it) }
        }
        return recipe
    }

    suspend fun getRecipesByRegion(): List<Recipe> {
        val nationality = locationDataSource.getLastLocationNationality()
        val recipes: List<Recipe> = remoteDataSource.listMealsByNationality(apiKey, nationality)
        checkFavorites(recipes)
        return recipes
    }

    suspend fun getRecipesByName(name: String): List<Recipe> {
        val recipes : List<Recipe> = remoteDataSource.listMealsByName(apiKey, name.lowercase())
        checkFavorites(recipes)
        return recipes
    }

    suspend fun checkFavorites(recipes: List<Recipe>) {
        if (!recipes.isNullOrEmpty()) {
            val favIdList = localDataSource.getFavorites().map { recipe -> recipe.id }
            recipes.forEach { it.favorite = favIdList.contains(it.id) }
        }
    }

    suspend fun updateRecipe(recipe: Recipe) {
        localDataSource.updateRecipe(recipe)
    }

    suspend fun getFavorites() : List<Recipe> {
        return localDataSource.getFavorites()
    }

}
