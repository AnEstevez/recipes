package com.andresestevez.recipes.ui.main.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.recipes.data.toRecipeItemUiState
import com.andresestevez.recipes.ui.common.RecipeItemUiState
import com.andresestevez.recipes.ui.common.getMessageFromThrowable
import com.andresestevez.usecases.GetFavoriteRecipes
import com.andresestevez.usecases.ToggleRecipeFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val getFavoriteRecipes: GetFavoriteRecipes,
    private val toggleRecipeFavorite: ToggleRecipeFavorite,
) :
    ViewModel() {

    data class UiState(
        var loading: Boolean = false,
        var data: List<RecipeItemUiState> = emptyList(),
        var userMessage: String? = null,
    )

    private var _state: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, userMessage = null) }

            getFavoriteRecipes().collect { result ->
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
                    _state.update { currentState ->
                        currentState.copy(loading = false,
                            userMessage = throwable.getMessageFromThrowable())
                    }
                }
            }
        }
    }
}