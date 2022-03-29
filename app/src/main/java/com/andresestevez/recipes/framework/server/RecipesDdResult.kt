package com.andresestevez.recipes.framework.server

import androidx.annotation.Keep

@Keep
data class RecipesDdResult(
    val meals: List<RecipeDto>? = listOf()
)