package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.domain.Recipe
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

    private var _state: MutableStateFlow<UiState> = MutableStateFlow(UiState(loading = false))
    val state: StateFlow<UiState> = _state.asStateFlow()

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

}