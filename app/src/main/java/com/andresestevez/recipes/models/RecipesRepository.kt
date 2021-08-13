package com.andresestevez.recipes.models

import android.app.Application
import com.andresestevez.recipes.R

class RecipesRepository(val application: Application) {

    private val apiKey = application.getString(R.string.api_key)
    private val countryCodeRepository = CountryCodeRepository(application)

    suspend fun findRecipeById(recipeId: String) =
        TheMealDbClient.service.findMealById(apiKey, recipeId).meals.firstOrNull()

    suspend fun listRecipesByName(name: String) =
        TheMealDbClient.service.listMealsByName(apiKey, name.lowercase())

    suspend fun listRecipesByRegion() =
        countryCodeRepository.findLastLocationNationality().let {
            TheMealDbClient.service.listMealsByNationality(apiKey, it).meals
        }

}