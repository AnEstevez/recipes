package com.andresestevez.recipes.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val idMeal: String,
    val name: String,
    val thumbnail: String,
    val instructions: String,
    @ColumnInfo(name = "country", defaultValue = "unknown")
    val country: String,
    val ingredients: List<String>,
    val measures: List<String>,
    @ColumnInfo(name = "favorite", defaultValue = "0" )
    val favorite: Boolean,
    val strCategory: String?,
    val strCreativeCommonsConfirmed: String?,
    val strDrinkAlternate: String?,
    val strImageSource: String?,
    val strSource: String?,
    val strTags: String?,
    val strYoutube: String?,
    @ColumnInfo(name = "dateModified", defaultValue = "CURRENT_TIMESTAMP" )
    val dateModified: String?
)
