package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.*
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.models.database.Recipe
import com.andresestevez.recipes.ui.common.Event
import kotlinx.coroutines.launch

class FavViewModel(private val repository: RecipesRepository): ViewModel() {

    sealed class UiModel {
        object Loading: UiModel()
        class Content(val recipes: List<Recipe>) : UiModel()
    }

    private var _model = MutableLiveData<UiModel>()
    val model : LiveData<UiModel>
        get() = _model

    private var _navigation = MutableLiveData<Event<String>>()
    val navigation : LiveData<Event<String>>
        get() = _navigation

    fun refresh() {
        viewModelScope.launch {
            _model.value = UiModel.Loading
            _model.value = UiModel.Content(repository.listFavorites())
        }
    }

    fun onRecipeClicked(recipe: Recipe) {
        _navigation.value = Event(recipe.idMeal)
    }
}

@Suppress("UNCHECKED_CAST")
class FavFragmentFactory(private val repository: RecipesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavViewModel(repository) as T
    }

}