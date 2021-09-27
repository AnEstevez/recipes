package com.andresestevez.recipes.data.server

import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.data.toDomainRecipe

class MealDBDataSource: RemoteDataSource {
    override suspend fun findById(apiKey: String, recipeId: String): Recipe? {
        return TheMealDbClient.service.findMealById(apiKey, recipeId).meals?.let {
            it.first().toDomainRecipe()
        }
    }

    override suspend fun listMealsByNationality(apiKey: String, nationality: String): List<Recipe> {
        return TheMealDbClient.service.listMealsByNationality(apiKey, nationality).meals?.map {
            it.toDomainRecipe()
        } ?: emptyList()
    }

    override suspend fun listMealsByName(apiKey: String, name: String): List<Recipe> {
        return TheMealDbClient.service.listMealsByName(apiKey, name).meals?.map {
            it.toDomainRecipe()
        } ?: emptyList()
    }

}