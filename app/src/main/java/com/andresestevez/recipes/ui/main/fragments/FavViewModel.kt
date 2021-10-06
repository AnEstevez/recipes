package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.ui.common.Event
import com.andresestevez.usecases.GetFavoriteRecipes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(private val getFavoriteRecipes: GetFavoriteRecipes): ViewModel() {

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
            _model.value = UiModel.Content(getFavoriteRecipes.invoke())
        }
    }

    fun onRecipeClicked(recipe: Recipe) {
        _navigation.value = Event(recipe.id)
    }
}