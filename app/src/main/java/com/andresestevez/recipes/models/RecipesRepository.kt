package com.andresestevez.recipes.models

import com.andresestevez.recipes.R
import com.andresestevez.recipes.RecipesApp
import com.andresestevez.recipes.models.database.Recipe
import com.andresestevez.recipes.models.server.TheMealDbClient
import com.andresestevez.recipes.ui.common.toRecipeDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepository(application: RecipesApp) {

    private val apiKey = application.getString(R.string.api_key)
    private val countryCodeRepository = CountryCodeRepository(application)
    private val db = application.db

    suspend fun findRecipeById(recipeId: String): Recipe = withContext(Dispatchers.IO) {

            with(db.recipeDao()) {
                var recipe = findByIdMeal(recipeId)
                if (recipe == null) {
                    val recipeWS =
                        TheMealDbClient.service.findMealById(apiKey, recipeId).meals?.first()
                    recipeWS?.let {
                        insertRecipe(listOf(it.toRecipeDB()))
                        recipe = findByIdMeal(recipeId)
                    }
                }
                recipe
            }

    }

    suspend fun listRecipesByName(name: String) = withContext(Dispatchers.IO) {
        var recipesDB: List<Recipe>? = null
        val recipesServer = TheMealDbClient.service.listMealsByName(apiKey, name.lowercase()).meals
        if (!recipesServer.isNullOrEmpty()){
            val favIdList = db.recipeDao().getFavorites().map { x -> x.idMeal }
            recipesDB = recipesServer.map { recipe -> recipe.toRecipeDB() }
            recipesDB.forEach { it.favorite = favIdList.contains(it.idMeal) }
        }
        recipesDB
    }

    suspend fun listRecipesByRegion() = withContext(Dispatchers.IO) {
        countryCodeRepository.findLastLocationNationality().let { nationality ->
            var recipesDB: List<Recipe>? = null

            val recipesServer = TheMealDbClient.service.listMealsByNationality(apiKey, nationality).meals
            if (!recipesServer.isNullOrEmpty()){
                val favIdList = db.recipeDao().getFavorites().map { x -> x.idMeal }
                recipesDB = recipesServer.map { recipe -> recipe.toRecipeDB() }
                recipesDB.forEach { it.favorite = favIdList.contains(it.idMeal) }
            }
            recipesDB
        }
    }

    suspend fun updateRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        db.recipeDao().updateRecipe(recipe)
    }

    suspend fun listFavorites() : List<Recipe> = withContext(Dispatchers.IO) {
        db.recipeDao().getFavorites()
    }
}
