package com.andresestevez.recipes.presentation.main.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.recipes.framework.toRecipeItemUiState
import com.andresestevez.recipes.presentation.common.RecipeItemUiState
import com.andresestevez.recipes.presentation.common.getMessageFromThrowable
import com.andresestevez.usecases.GetRecipesByName
import com.andresestevez.usecases.ToggleRecipeFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRecipesByName: GetRecipesByName,
    private val toggleRecipeFavorite: ToggleRecipeFavorite
) :
    ViewModel() {

    data class UiState(
        var loading: Boolean = false,
        var data: List<RecipeItemUiState> = emptyList(),
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
                                data = data.map { recipe ->
                                    recipe.toRecipeItemUiState { toggleRecipeFavorite(recipe) }
                                },
                                userMessage = null)
                        }
                    })
                    { throwable ->
                        Timber.d(throwable)
                        _state.update {
                            it.copy(loading = false,
                                userMessage = throwable.getMessageFromThrowable())
                        }
                    }
                }
            }
        }
    }

    fun clearUserMessage() {
        _state.update { it.copy(userMessage = null) }
    }
}