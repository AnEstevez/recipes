package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import kotlinx.coroutines.launch

class LocalRecipesPresenter(private val recipesRepository: RecipesRepository) {

    interface View {
        fun navigateTo(recipeId: String)
        fun updateData(recipes: List<Recipe>?)
        fun showProgress()
        fun hideProgress()
    }

    private var view : View? = null

    fun onViewCreated(view: View) {
        this.view = view
    }

    fun onDestroyView() {
        this.view = null
    }

    fun onRecipeClicked(id: String) {
        view?.navigateTo(id)
    }

    fun onLocalRecipesRequested() {
        view?.let {
            (it as LifecycleOwner).lifecycleScope.launch {
                it.showProgress()
                it.updateData(recipesRepository.listRecipesByRegion())
                it.hideProgress()
            }
        }
    }

}