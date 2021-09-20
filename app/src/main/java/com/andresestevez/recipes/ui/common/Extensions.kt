package com.andresestevez.recipes.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.andresestevez.recipes.RecipesApp
import com.andresestevez.recipes.models.server.Recipe

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

inline fun <reified T : Activity> Context.intentFor(body: Intent.() -> Unit): Intent =
    Intent(this, T::class.java).apply(body)

inline fun <reified T : Activity> Context.startActivity(body: Intent.() -> Unit) {
    startActivity(intentFor<T>(body))
}

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

val Context.app: RecipesApp
    get() = applicationContext as RecipesApp

val Fragment.app: RecipesApp
    get() = ((activity?.app)
        ?: IllegalStateException("Fragment needs to be attach to the activity to access the App instance"))
            as RecipesApp

fun Recipe.toRecipeDB(): com.andresestevez.recipes.models.database.Recipe {

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

    return com.andresestevez.recipes.models.database.Recipe(
        0,
        id,
        name,
        thumbnail,
        instructions ?: "",
        country ?: "",
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
        dateModified
    )
}

