package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe
import java.util.*

class ToggleRecipeFavorite(private val recipesRepository: RecipesRepository) {

    suspend operator fun invoke(recipe: Recipe): Unit = with(recipe) {
        recipesRepository.updateRecipe(copy(favorite = !favorite, dateModified = Date()))
    }
}