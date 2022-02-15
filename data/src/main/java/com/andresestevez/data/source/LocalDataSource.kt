package com.andresestevez.data.source

import com.andresestevez.domain.Recipe
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun findById(recipeId: String): Flow<Recipe>
    suspend fun saveRecipe(recipe: Recipe?)
    suspend fun saveAll(recipes: List<Recipe>)
    fun getFavorites(): Flow<List<Recipe>>
    fun searchByCountry(country: String): Flow<List<Recipe>>
    fun searchByName(name: String): Flow<List<Recipe>>
    suspend fun updateRecipe(recipe: Recipe)
}