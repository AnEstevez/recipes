package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.*
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import kotlinx.coroutines.launch

class LocalRecipesViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    sealed class UiModel {
        object Loading : UiModel()
        class Content(val recipes: List<Recipe>) : UiModel()
        class Navigation(val recipeId: String) : UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val model : LiveData<UiModel>
            get() = _model

    fun onRecipeClicked(id: String) {
        _model.value = UiModel.Navigation(id)
    }

    fun refresh() {
        viewModelScope.launch {
            _model.value = UiModel.Loading
            _model.value = UiModel.Content(recipesRepository.listRecipesByRegion() ?: emptyList())
        }

    }

}

@Suppress("UNCHECKED_CAST")
class LocalRecipesViewModelFactory(private val recipesRepository: RecipesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocalRecipesViewModel(recipesRepository) as T
    }
}