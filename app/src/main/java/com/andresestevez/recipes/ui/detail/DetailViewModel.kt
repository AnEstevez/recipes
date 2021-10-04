package com.andresestevez.recipes.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.domain.Recipe
import com.andresestevez.usecases.GetRecipeById
import com.andresestevez.usecases.ToggleRecipeFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val getRecipeById: GetRecipeById, private val toggleRecipeFavorite: ToggleRecipeFavorite ) : ViewModel() {

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
                getRecipeById.invoke(recipeId)?.let {
                    _model.value = UiModel.Content(it)
                } ?: throw Exception("Recipe not found") // TODO
            }
        }
    }

    fun onFavoriteClicked() {
        _model.value?.recipe?.let {
            viewModelScope.launch {
                val updatedRecipe = toggleRecipeFavorite.invoke(it)
                _model.value = UiModel.Favorite(updatedRecipe)
            }
        }
    }

}