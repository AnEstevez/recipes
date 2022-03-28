package com.andresestevez.recipes.presentation.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.andresestevez.data.repository.NoDataFoundException
import com.andresestevez.recipes.RecipesApp
import retrofit2.HttpException
import java.io.IOException

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

fun Throwable.getMessageFromThrowable(): String {
    return when (this) {
        is IOException -> "Recipes is offline: Check your internet connection"
        is HttpException -> "Server Error ${code()}"
        is NoDataFoundException -> message
        else -> message?.let { "Unknown Error: $message" } ?: "Unknown Error"
    }
}