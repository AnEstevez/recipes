package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe

class GetRecipesByName(private val recipesRepository: RecipesRepository) {

    suspend fun invoke(name: String): List<Recipe> = recipesRepository.getRecipesByName(name)
}