package com.andresestevez.recipes.models

import com.andresestevez.recipes.R
import com.andresestevez.recipes.RecipesApp
import com.andresestevez.recipes.models.database.Recipe
import com.andresestevez.recipes.models.server.TheMealDbClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.andresestevez.recipes.models.server.Recipe as RecipeWS

class RecipesRepository(application: RecipesApp) {

    private val apiKey = application.getString(R.string.api_key)
    private val countryCodeRepository = CountryCodeRepository(application)
    private val db = application.db

    suspend fun findRecipeById(recipeId: String): Recipe? = withContext(Dispatchers.IO) {
        var recipe: Recipe? = null
        if (recipeId.isNotBlank()) {
            with(db.recipeDao()) {
                recipe = findByIdMeal(recipeId)
                if (recipe == null) {
                    val recipeWS =
                        TheMealDbClient.service.findMealById(apiKey, recipeId).meals?.firstOrNull()
                    recipeWS?.let {
                        insertRecipe(listOf(it.toRecipeDB()))
                        recipe = findByIdMeal(recipeId)
                    }
                }
            }
        }
        recipe
    }


    suspend fun listRecipesByName(name: String) = withContext(Dispatchers.IO) {
        TheMealDbClient.service.listMealsByName(apiKey, name.lowercase()).meals
    }

    suspend fun listRecipesByRegion() = withContext(Dispatchers.IO) {
        countryCodeRepository.findLastLocationNationality().let {
            TheMealDbClient.service.listMealsByNationality(apiKey, it).meals
        }
    }

}

private fun RecipeWS.toRecipeDB(): Recipe {

    val ingredientsList: MutableList<String> = mutableListOf()
    val measuresList: MutableList<String> = mutableListOf()

    if(!strIngredient1.isNullOrBlank()) {
        ingredientsList.add(strIngredient1)
        measuresList.add(strMeasure1 ?: "")
    }
    if(!strIngredient2.isNullOrBlank()) {
        ingredientsList.add(strIngredient2)
        measuresList.add(strMeasure2 ?: "")
    }
    if(!strIngredient3.isNullOrBlank()) {
        ingredientsList.add(strIngredient3)
        measuresList.add(strMeasure3 ?: "")
    }
    if(!strIngredient4.isNullOrBlank()) {
        ingredientsList.add(strIngredient4)
        measuresList.add(strMeasure4 ?: "")
    }
    if(!strIngredient5.isNullOrBlank()) {
        ingredientsList.add(strIngredient5)
        measuresList.add(strMeasure5 ?: "")
    }
    if(!strIngredient6.isNullOrBlank()) {
        ingredientsList.add(strIngredient6)
        measuresList.add(strMeasure6 ?: "")
    }
    if(!strIngredient7.isNullOrBlank()) {
        ingredientsList.add(strIngredient7)
        measuresList.add(strMeasure7 ?: "")
    }
    if(!strIngredient8.isNullOrBlank()) {
        ingredientsList.add(strIngredient8)
        measuresList.add(strMeasure8 ?: "")
    }
    if(!strIngredient9.isNullOrBlank()) {
        ingredientsList.add(strIngredient9)
        measuresList.add(strMeasure9 ?: "")
    }
    if(!strIngredient10.isNullOrBlank()) {
        ingredientsList.add(strIngredient10)
        measuresList.add(strMeasure10 ?: "")
    }
    if(!strIngredient11.isNullOrBlank()) {
        ingredientsList.add(strIngredient11)
        measuresList.add(strMeasure11 ?: "")
    }
    if(!strIngredient12.isNullOrBlank()) {
        ingredientsList.add(strIngredient12)
        measuresList.add(strMeasure12 ?: "")
    }
    if(!strIngredient13.isNullOrBlank()) {
        ingredientsList.add(strIngredient13)
        measuresList.add(strMeasure13 ?: "")
    }
    if(!strIngredient14.isNullOrBlank()) {
        ingredientsList.add(strIngredient14)
        measuresList.add(strMeasure14 ?: "")
    }
    if(!strIngredient15.isNullOrBlank()) {
        ingredientsList.add(strIngredient15)
        measuresList.add(strMeasure15 ?: "")
    }
    if(!strIngredient16.isNullOrBlank()) {
        ingredientsList.add(strIngredient16)
        measuresList.add(strMeasure16 ?: "")
    }
    if(!strIngredient17.isNullOrBlank()) {
        ingredientsList.add(strIngredient17)
        measuresList.add(strMeasure17 ?: "")
    }
    if(!strIngredient18.isNullOrBlank()) {
        ingredientsList.add(strIngredient18)
        measuresList.add(strMeasure18 ?: "")
    }
    if(!strIngredient19.isNullOrBlank()) {
        ingredientsList.add(strIngredient19)
        measuresList.add(strMeasure19 ?: "")
    }
    if(!strIngredient20.isNullOrBlank()) {
        ingredientsList.add(strIngredient20)
        measuresList.add(strMeasure20 ?: "")
    }

    return Recipe(
        0,
        id,
        name,
        thumbnail,
        instructions ?: "",
        country ?: "",
        ingredientsList,
        measuresList,
        false,
        strCategory,
        strCreativeCommonsConfirmed,
        strDrinkAlternate,
        strImageSource,
        strSource,
        strTags,
        strYoutube,
        dateModified
        )
}
