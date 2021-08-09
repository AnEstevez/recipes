package com.andresestevez.recipes.ui.detail

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import kotlinx.coroutines.launch

class DetailPresenter(private val recipesRepository: RecipesRepository) {

    interface View {
        fun updateUI(recipe: Recipe?)
    }

    private var view: View? = null

    fun onCreate(view: View, recipeId: String?) {
        this.view = view

        (view as LifecycleOwner).lifecycleScope.launch {
            recipeId?.let {
                view.updateUI(recipesRepository.findRecipeById(it))
            }
        }
    }

    fun onDestroy() {
        this.view = null
    }

}