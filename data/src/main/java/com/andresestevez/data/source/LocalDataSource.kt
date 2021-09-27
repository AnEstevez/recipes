package com.andresestevez.data.source

import com.andresestevez.domain.Recipe

interface LocalDataSource {
    suspend fun findById(recipeId: String): Recipe?
    suspend fun saveRecipe(recipe: Recipe?)
    suspend fun getFavorites(): List<Recipe>
    suspend fun updateRecipe(recipe: Recipe)
}