package com.andresestevez.recipes.data.server

import com.andresestevez.data.repository.NoDataFoundException
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.data.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealDBDataSource(private val mealDbClient: TheMealDbClient) : RemoteDataSource {
    override suspend fun findById(apiKey: String, recipeId: String): Result<Recipe> =
        withContext(Dispatchers.IO) {
            try {
                val result = mealDbClient.service.findMealById(apiKey, recipeId).meals
                if (result.isNullOrEmpty()) {
                    Result.failure(NoDataFoundException())
                } else {
                    Result.success(result.first().toDomain())
                }
            } catch (t: Throwable) {
                Result.failure<Recipe>(t)
            }
        }

    override suspend fun listMealsByNationality(
        apiKey: String,
        nationality: String,
    ): Result<List<Recipe>> =
        withContext(Dispatchers.IO) {
            try {
                val result = mealDbClient.service.listMealsByNationality(apiKey, nationality).meals
                if (result.isNullOrEmpty()) {
                    Result.failure<List<Recipe>>(NoDataFoundException("No recipes found"))
                } else {
                    Result.success(result.map { it.toDomain() })
                }
            } catch (t: Throwable) {
                Result.failure<List<Recipe>>(t)
            }
        }

    override suspend fun listMealsByName(apiKey: String, name: String): Result<List<Recipe>> =
        withContext(Dispatchers.IO) {
            try {
                val result = mealDbClient.service.listMealsByName(apiKey, name).meals
                if (result.isNullOrEmpty()) {
                    Result.failure<List<Recipe>>(NoDataFoundException("No recipes found"))
                } else {
                    Result.success(result.map { it.toDomain() })
                }
            } catch (t: Throwable) {
                Result.failure<List<Recipe>>(t)
            }
        }

}