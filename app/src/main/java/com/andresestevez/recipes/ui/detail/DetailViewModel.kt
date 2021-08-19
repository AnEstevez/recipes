package com.andresestevez.recipes.ui.detail

import androidx.lifecycle.*
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.models.database.Recipe
import kotlinx.coroutines.launch

class DetailViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    sealed class UiModel(val recipe: Recipe) {
        class Content(recipe: Recipe): UiModel(recipe)
        class Favorite(recipe: Recipe): UiModel(recipe)
    }

    private val _model = MutableLiveData<UiModel>()
    val model : LiveData<UiModel>
        get() = _model

    fun refresh(recipeId: String?) {
        if (!recipeId.isNullOrEmpty()) {
            viewModelScope.launch {
                _model.value = UiModel.Content(recipesRepository.findRecipeById(recipeId))
            }
        }
    }

    fun onFavoriteClicked() {
        _model.value?.recipe?.let {
            viewModelScope.launch {
                val updatedRecipe = it.copy(favorite = !it.favorite)
                recipesRepository.updateRecipe(updatedRecipe)
                _model.value = UiModel.Favorite(updatedRecipe)
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val recipesRepository: RecipesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(recipesRepository) as T
    }
}