package com.andresestevez.recipes.models

import android.app.Activity
import com.andresestevez.recipes.R

class RecipesRepository(val activity: Activity) {

    private val apiKey = activity.getString(R.string.api_key)
    private val countryCodeRepository = CountryCodeRepository(activity)

    suspend fun findRecipeById(recipeId: String) =
        TheMealDbClient.service.findMealById(apiKey, recipeId).meals.firstOrNull()

    suspend fun listRecipesByName(name: String) =
        TheMealDbClient.service.listMealsByName(apiKey, name.lowercase())

    suspend fun listRecipesByRegion() =
            countryCodeRepository.findLastLocationNationality()?.let {
                TheMealDbClient.service.listMealsByNationality(apiKey, it).meals
            }

}