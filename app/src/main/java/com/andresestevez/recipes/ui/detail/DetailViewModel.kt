package com.andresestevez.recipes.ui.detail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.andresestevez.domain.Recipe
import com.andresestevez.usecases.GetRecipeById
import com.andresestevez.usecases.ToggleRecipeFavorite
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    var stateHandle: SavedStateHandle,
    private val getRecipeById: GetRecipeById,
    private val toggleRecipeFavorite: ToggleRecipeFavorite,
) : ViewModel() {

    companion object {
        const val RECIPE_ID_NAV_ARGS = "recipeID"
    }

    sealed class UiModel(val recipe: Recipe) {
        class Content(recipe: Recipe) : UiModel(recipe)
        class Favorite(recipe: Recipe) : UiModel(recipe)
    }

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    fun refresh() {
        val recipeIdFromNavArgs = stateHandle.get<String>(RECIPE_ID_NAV_ARGS)
            ?: throw IllegalStateException("Recipe id not found in the state handle")
        viewModelScope.launch {
            getRecipeById.invoke(recipeIdFromNavArgs)?.let {
                _model.value = UiModel.Content(it)
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

    @VisibleForTesting
    fun setNewValue(newValue: UiModel) {
        _model.value = newValue
    }
}