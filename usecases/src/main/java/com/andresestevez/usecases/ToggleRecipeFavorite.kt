package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe
import java.util.*

class ToggleRecipeFavorite(private val recipesRepository: RecipesRepository) {

    suspend fun invoke(recipe: Recipe): Recipe = with(recipe) {
        copy(favorite = !favorite, dateModified = Date()).also { recipesRepository.updateRecipe(it) }
    }
}