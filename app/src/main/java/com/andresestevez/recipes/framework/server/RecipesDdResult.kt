package com.andresestevez.recipes.framework.server

data class RecipesDdResult(
    val meals: List<RecipeDto>? = listOf()
)