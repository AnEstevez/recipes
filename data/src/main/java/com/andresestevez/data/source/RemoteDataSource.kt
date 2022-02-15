package com.andresestevez.data.source

import com.andresestevez.domain.Recipe

interface RemoteDataSource {
    suspend fun findById(apiKey: String, recipeId: String): Result<Recipe>
    suspend fun listMealsByNationality(apiKey: String, nationality: String): Result<List<Recipe>>
    suspend fun listMealsByName(apiKey: String, name: String): Result<List<Recipe>>
}