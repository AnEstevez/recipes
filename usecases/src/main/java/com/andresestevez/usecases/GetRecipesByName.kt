package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe
import kotlinx.coroutines.flow.Flow

class GetRecipesByName(private val recipesRepository: RecipesRepository) {

    operator fun invoke(name: String): Flow<Result<List<Recipe>>> = recipesRepository.getRecipesByName(name)
}