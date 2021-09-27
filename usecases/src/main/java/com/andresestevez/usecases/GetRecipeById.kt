package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe

class GetRecipeById(private val recipesRepository: RecipesRepository) {

    suspend fun invoke(id: String): Recipe? = recipesRepository.findRecipeById(id)
}