package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.ui.common.Event
import com.andresestevez.recipes.ui.common.getMessageFromThrowable
import com.andresestevez.usecases.GetLocalRecipes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalRecipesViewModel @Inject constructor(private val getLocalRecipes: GetLocalRecipes) :
    ViewModel() {

    data class UiState(
        var loading: Boolean = false,
        var data: List<Recipe> = emptyList(),
        var userMessage: String? = null,
    )

    private var _state: MutableStateFlow<UiState> = MutableStateFlow(UiState(loading = true))
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _navigation = MutableLiveData<Event<String>>()
    val navigation: LiveData<Event<String>> get() = _navigation

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, userMessage = null) }

            getLocalRecipes().collect { result ->
                result.fold({ data ->
                    _state.update {
                        it.copy(loading = false,
                            data = data,
                            userMessage = null)
                    }
                })
                { throwable ->
                    _state.update { currentState ->
                        currentState.copy(loading = false,
                            userMessage = throwable.getMessageFromThrowable())
                    }
                }
            }
        }
    }

    fun onRecipeClicked(recipe: Recipe) {
        _navigation.value = Event(recipe.id)
    }
}