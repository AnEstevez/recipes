package com.andresestevez.recipes.ui.detail

import androidx.lifecycle.*
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    sealed class UiModel {
        class Content(val recipe: Recipe?): UiModel()
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

}
