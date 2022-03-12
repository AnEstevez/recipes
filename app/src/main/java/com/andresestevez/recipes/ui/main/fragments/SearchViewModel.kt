package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.ui.common.getMessageFromThrowable
import com.andresestevez.usecases.GetRecipesByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getRecipesByName: GetRecipesByName) :
    ViewModel() {

    data class UiState(
        var loading: Boolean = false,
        var data: List<Recipe> = emptyList(),
        var userMessage: String? = null,
    )

    private var _state: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun refresh(query: String?) {
        if (!query.isNullOrBlank()) {
            viewModelScope.launch {
                _state.update { it.copy(loading = true, userMessage = null) }

                getRecipesByName(query).collect { result ->
                    result.fold({ data ->
                        _state.update {
                            it.copy(loading = false,
                                data = data,
                                userMessage = null)
                        }
                    })
                    { throwable ->
                        _state.update {
                            it.copy(loading = false,
                                userMessage = throwable.getMessageFromThrowable())
                        }
                    }
                }
            }
        }
    }

}