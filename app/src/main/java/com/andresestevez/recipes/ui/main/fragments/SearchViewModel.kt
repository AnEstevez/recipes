package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.*
import com.andresestevez.recipes.models.server.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.common.Event
import kotlinx.coroutines.launch

class SearchViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    sealed class UiModel {
        object HideKeyboard: UiModel()
        object Loading: UiModel()
        class Content(val recipes: List<Recipe>?): UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() = _model

    private val _navigation = MutableLiveData<Event<String>>()
    val navigation : LiveData<Event<String>>
        get() = _navigation

    fun refresh(query: String?) {
        if (!query.isNullOrBlank()) {
            viewModelScope.launch {
                _model.value = UiModel.HideKeyboard
                _model.value = UiModel.Loading
                _model.value= UiModel.Content(recipesRepository.listRecipesByName(query))
            }
        }
    }

    fun onRecipeClicked(id: String) {
        _navigation.value = Event(id)
    }

}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val recipesRepository: RecipesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(recipesRepository) as T
    }

}