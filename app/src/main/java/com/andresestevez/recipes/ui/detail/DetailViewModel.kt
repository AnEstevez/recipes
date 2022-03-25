package com.andresestevez.recipes.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.ui.common.getMessageFromThrowable
import com.andresestevez.usecases.GetRecipeById
import com.andresestevez.usecases.ToggleRecipeFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
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

    data class UiState(
        var loading: Boolean = false,
        var data: Recipe? = null,
        var userMessage: String? = null,
    )

    private var _state: MutableStateFlow<UiState> = MutableStateFlow(UiState(loading = true))
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val recipeId = stateHandle.get<String>(RECIPE_ID_NAV_ARGS)!!

    init {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, userMessage = null) }

            getRecipeById(recipeId).collect { result ->
                result.fold({ data ->
                    _state.update {
                        it.copy(loading = false,
                            data = data,
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

    fun onFavoriteClicked() {
        viewModelScope.launch {
            state.value.data?.let { toggleRecipeFavorite(it) }
        }
    }

}