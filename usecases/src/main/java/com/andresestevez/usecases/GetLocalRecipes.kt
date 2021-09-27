package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe

class GetLocalRecipes(private val recipesRepository: RecipesRepository) {

    suspend fun invoke(): List<Recipe> = recipesRepository.getRecipesByRegion()
}