package com.andresestevez.data.source

import com.andresestevez.domain.Recipe

interface RemoteDataSource {
    suspend fun findById(recipeId: String): Result<Recipe>
    suspend fun listMealsByNationality(nationality: String): Result<List<Recipe>>
    suspend fun listMealsByName(name: String): Result<List<Recipe>>
}