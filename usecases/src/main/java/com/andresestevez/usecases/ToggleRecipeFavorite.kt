package com.andresestevez.usecases

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.domain.Recipe
import javax.inject.Inject

class ToggleRecipeFavorite @Inject constructor(private val recipesRepository: RecipesRepository) {

    suspend operator fun invoke(recipe: Recipe): Unit = with(recipe) {
        recipesRepository.updateRecipe(copy(favorite = !favorite))
    }
}