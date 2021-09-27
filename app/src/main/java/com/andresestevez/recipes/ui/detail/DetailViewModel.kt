package com.andresestevez.recipes.ui.detail

import androidx.lifecycle.*
import com.andresestevez.domain.Recipe
import com.andresestevez.usecases.GetRecipeById
import com.andresestevez.usecases.ToggleRecipeFavorite
import kotlinx.coroutines.launch

class DetailViewModel(private val getRecipeById: GetRecipeById, private val toggleRecipeFavorite: ToggleRecipeFavorite ) : ViewModel() {

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
                } ?: Exception("Recipe not found") // TODO ver como gestionar excepciones correctamente
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

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val getRecipeById: GetRecipeById, private val toggleRecipeFavorite: ToggleRecipeFavorite ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(getRecipeById, toggleRecipeFavorite) as T
    }
}