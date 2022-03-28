package com.andresestevez.recipes.presentation.common

data class RecipeItemUiState(
    var id: String,
    var title: String,
    var thumbnail: String,
    var bookmarked: Boolean = false,
) {
    var onBookmark: suspend () -> Unit = {}
}

