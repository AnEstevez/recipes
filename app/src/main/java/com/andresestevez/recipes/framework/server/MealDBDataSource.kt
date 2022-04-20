package com.andresestevez.recipes.framework.server

import com.andresestevez.data.repository.NoDataFoundException
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.framework.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

internal class MealDBDataSource @Inject constructor(
    private val mealDbClient: TheMealDbClient,
    @Named("apiKey") private val apiKey: String,
) : RemoteDataSource {
    override suspend fun findById(recipeId: String): Result<Recipe> =
        withContext(Dispatchers.IO) {
            try {
                val result = mealDbClient.service.findMealById(apiKey, recipeId).meals
                if (result.isNullOrEmpty()) {
                    Timber.w("Recipe with ID[%s] not found", recipeId)
                    Result.failure(NoDataFoundException())
                } else {
                    Result.success(result.first().toDomain())
                }
            } catch (t: Throwable) {
                Timber.e(t)
                Result.failure<Recipe>(t)
            }
        }

    override suspend fun listMealsByNationality(
        nationality: String,
    ): Result<List<Recipe>> =
        withContext(Dispatchers.IO) {
            try {
                val result = mealDbClient.service.listMealsByNationality(apiKey, nationality).meals
                if (result.isNullOrEmpty()) {
                    Timber.w("No recipes found with nationality [%s]", nationality)
                    Result.failure<List<Recipe>>(NoDataFoundException("No recipes found"))
                } else {
                    Result.success(result.map { it.toDomain() })
                }
            } catch (t: Throwable) {
                Timber.e(t)
                Result.failure<List<Recipe>>(t)
            }
        }

    override suspend fun listMealsByName(name: String): Result<List<Recipe>> =
        withContext(Dispatchers.IO) {
            try {
                val result = mealDbClient.service.listMealsByName(apiKey, name).meals
                if (result.isNullOrEmpty()) {
                    Result.failure<List<Recipe>>(NoDataFoundException("No recipes found"))
                } else {
                    Result.success(result.map { it.toDomain() })
                }
            } catch (t: Throwable) {
                Timber.e(t)
                Result.failure<List<Recipe>>(t)
            }
        }

}