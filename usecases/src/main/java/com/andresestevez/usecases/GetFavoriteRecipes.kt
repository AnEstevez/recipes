package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteRecipes @Inject constructor(private val recipesRepository: RecipesRepository) {

    operator fun invoke(): Flow<Result<List<Recipe>>> = recipesRepository.getFavorites()
}