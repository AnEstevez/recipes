package com.andresestevez.recipes.data.server

import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.data.toDomain

class MealDBDataSource(private val mealDbClient: TheMealDbClient): RemoteDataSource {
    override suspend fun findById(apiKey: String, recipeId: String): Recipe? {
        return mealDbClient.service.findMealById(apiKey, recipeId).meals?.let {
            it.first().toDomain()
        }
    }

    override suspend fun listMealsByNationality(apiKey: String, nationality: String): List<Recipe> {
        return mealDbClient.service.listMealsByNationality(apiKey, nationality).meals?.map {
            it.toDomain()
        } ?: emptyList()
    }

    override suspend fun listMealsByName(apiKey: String, name: String): List<Recipe> {
        return mealDbClient.service.listMealsByName(apiKey, name).meals?.map {
            it.toDomain()
        } ?: emptyList()
    }

}