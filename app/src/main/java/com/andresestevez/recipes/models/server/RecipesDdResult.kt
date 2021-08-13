package com.andresestevez.recipes.models.server

data class RecipesDdResult(
    val meals: List<Recipe>? = listOf()
)