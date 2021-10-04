package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.ui.common.Event
import com.andresestevez.usecases.GetRecipesByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getRecipesByName: GetRecipesByName) : ViewModel() {

    sealed class UiModel {
        object HideKeyboard: UiModel()
        object Loading: UiModel()
        class Content(val recipes: List<Recipe>?): UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() = _model

    private val _navigation = MutableLiveData<Event<String>>()
    val navigation : LiveData<Event<String>> get() = _navigation

    fun refresh(query: String?) {
        if (!query.isNullOrBlank()) {
            viewModelScope.launch {
                _model.value = UiModel.HideKeyboard
                _model.value = UiModel.Loading
                _model.value= UiModel.Content(getRecipesByName.invoke(query))
            }
        }
    }

    fun onRecipeClicked(recipe : Recipe) {
        viewModelScope.launch {
            _navigation.value = Event(recipe.id)
        }
    }

}