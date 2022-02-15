package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe
import kotlinx.coroutines.flow.Flow

class GetRecipeById(private val recipesRepository: RecipesRepository) {

    operator fun invoke(id: String): Flow<Result<Recipe>> = recipesRepository.findRecipeById(id)
}