package com.andresestevez.recipes.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Recipe(
    @PrimaryKey
    val id: String,
    val name: String,
    val thumbnail: String,
    val instructions: String,
    @ColumnInfo(name = "country", defaultValue = "unknown")
    val country: String,
    val ingredients: List<String>,
    val measures: List<String>,
    @ColumnInfo(name = "favorite", defaultValue = "0" )
    var favorite: Boolean,
    val strCategory: String?,
    val strCreativeCommonsConfirmed: String?,
    val strDrinkAlternate: String?,
    val strImageSource: String?,
    val strSource: String?,
    val strTags: String?,
    val strYoutube: String?,
    val dateModified: Long
)
