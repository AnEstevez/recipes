package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeById @Inject constructor(private val recipesRepository: RecipesRepository) {

    operator fun invoke(id: String): Flow<Result<Recipe>> = recipesRepository.findRecipeById(id)
}