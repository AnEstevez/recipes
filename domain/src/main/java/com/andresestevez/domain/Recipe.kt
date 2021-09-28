package com.andresestevez.domain

import java.util.*

data class Recipe(
    val id: String,
    val name: String,
    val thumbnail: String,
    val instructions: String,
    val country: String,
    val ingredients: List<String>,
    val measures: List<String>,
    var favorite: Boolean,
    val strCategory: String?,
    val strCreativeCommonsConfirmed: String?,
    val strDrinkAlternate: String?,
    val strImageSource: String?,
    val strSource: String?,
    val strTags: String?,
    val strYoutube: String?,
    val dateModified: Date?
)