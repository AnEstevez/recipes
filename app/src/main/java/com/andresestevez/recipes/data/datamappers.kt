package com.andresestevez.recipes.data

import java.util.*
import com.andresestevez.domain.Recipe as DomainRecipe
import com.andresestevez.recipes.data.database.RecipeEntity as RoomRecipe
import com.andresestevez.recipes.data.server.RecipeDto as ServerRecipe


fun DomainRecipe.toEntity(): RoomRecipe = RoomRecipe(
        id,
        name,
        thumbnail,
        instructions,
        country,
        ingredients,
        measures,
        favorite,
        strCategory,
        strCreativeCommonsConfirmed,
        strDrinkAlternate,
        strImageSource,
        strSource,
        strTags,
        strYoutube,
        dateModified?.time ?: System.currentTimeMillis()
)

fun RoomRecipe.toDomain(): DomainRecipe = DomainRecipe(
    id,
    name,
    thumbnail,
    instructions,
    country,
    ingredients,
    measures,
    favorite,
    strCategory,
    strCreativeCommonsConfirmed,
    strDrinkAlternate,
    strImageSource,
    strSource,
    strTags,
    strYoutube,
    Date(dateModified)
)

fun ServerRecipe.toDomain(): DomainRecipe {

    val ingredientsList: MutableList<String> = mutableListOf()
    val measuresList: MutableList<String> = mutableListOf()

    if(!strIngredient1.isNullOrBlank()) {
        ingredientsList.add(strIngredient1)
        measuresList.add(strMeasure1 ?: "")
    }
    if(!strIngredient2.isNullOrBlank()) {
        ingredientsList.add(strIngredient2)
        measuresList.add(strMeasure2 ?: "")
    }
    if(!strIngredient3.isNullOrBlank()) {
        ingredientsList.add(strIngredient3)
        measuresList.add(strMeasure3 ?: "")
    }
    if(!strIngredient4.isNullOrBlank()) {
        ingredientsList.add(strIngredient4)
        measuresList.add(strMeasure4 ?: "")
    }
    if(!strIngredient5.isNullOrBlank()) {
        ingredientsList.add(strIngredient5)
        measuresList.add(strMeasure5 ?: "")
    }
    if(!strIngredient6.isNullOrBlank()) {
        ingredientsList.add(strIngredient6)
        measuresList.add(strMeasure6 ?: "")
    }
    if(!strIngredient7.isNullOrBlank()) {
        ingredientsList.add(strIngredient7)
        measuresList.add(strMeasure7 ?: "")
    }
    if(!strIngredient8.isNullOrBlank()) {
        ingredientsList.add(strIngredient8)
        measuresList.add(strMeasure8 ?: "")
    }
    if(!strIngredient9.isNullOrBlank()) {
        ingredientsList.add(strIngredient9)
        measuresList.add(strMeasure9 ?: "")
    }
    if(!strIngredient10.isNullOrBlank()) {
        ingredientsList.add(strIngredient10)
        measuresList.add(strMeasure10 ?: "")
    }
    if(!strIngredient11.isNullOrBlank()) {
        ingredientsList.add(strIngredient11)
        measuresList.add(strMeasure11 ?: "")
    }
    if(!strIngredient12.isNullOrBlank()) {
        ingredientsList.add(strIngredient12)
        measuresList.add(strMeasure12 ?: "")
    }
    if(!strIngredient13.isNullOrBlank()) {
        ingredientsList.add(strIngredient13)
        measuresList.add(strMeasure13 ?: "")
    }
    if(!strIngredient14.isNullOrBlank()) {
        ingredientsList.add(strIngredient14)
        measuresList.add(strMeasure14 ?: "")
    }
    if(!strIngredient15.isNullOrBlank()) {
        ingredientsList.add(strIngredient15)
        measuresList.add(strMeasure15 ?: "")
    }
    if(!strIngredient16.isNullOrBlank()) {
        ingredientsList.add(strIngredient16)
        measuresList.add(strMeasure16 ?: "")
    }
    if(!strIngredient17.isNullOrBlank()) {
        ingredientsList.add(strIngredient17)
        measuresList.add(strMeasure17 ?: "")
    }
    if(!strIngredient18.isNullOrBlank()) {
        ingredientsList.add(strIngredient18)
        measuresList.add(strMeasure18 ?: "")
    }
    if(!strIngredient19.isNullOrBlank()) {
        ingredientsList.add(strIngredient19)
        measuresList.add(strMeasure19 ?: "")
    }
    if(!strIngredient20.isNullOrBlank()) {
        ingredientsList.add(strIngredient20)
        measuresList.add(strMeasure20 ?: "")
    }

    return DomainRecipe(
        id,
        name = name.lowercase(),
        thumbnail,
        instructions ?: "",
        country = if(country.isNullOrBlank()) "" else country.lowercase(),
        ingredientsList,
        measuresList,
        false,
        strCategory,
        strCreativeCommonsConfirmed,
        strDrinkAlternate,
        strImageSource,
        strSource,
        strTags,
        strYoutube,
        null
    )
}