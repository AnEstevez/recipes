package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import kotlinx.coroutines.launch

class SearchPresenter(private val recipesRepository: RecipesRepository) {

    interface View {
        fun updateData(recipes: List<Recipe>)
        fun navigateTo(recipeId: String)
        fun hideKeyboard()
        fun showProgress()
        fun hideProgress()
    }

    private var view: View? = null

    fun onViewCreated(view: View) {
        this.view = view
    }

    fun onDestroyView() {
        this.view = null
    }

    fun onQueryTextSubmited(query: String?) {
        if (!query.isNullOrBlank()) {
            view?.let {


                (it as LifecycleOwner).lifecycleScope.launch {
                    it.hideKeyboard()
                    it.showProgress()
                    it.updateData(recipesRepository.listRecipesByName(query).meals)
                    it.hideProgress()
                }

            }
        }
    }

    fun onRecipeClicked(id: String) {
        view?.navigateTo(id)
    }

}