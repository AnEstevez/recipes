package com.andresestevez.recipes.ui.detail
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.andresestevez.recipes.models.Recipe

class RecipeIngredientsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    fun setIngredients(recipe: Recipe) = with(recipe) {
        text = buildSpannedString {
            if(!strIngredient1.isNullOrBlank()) {
                bold { append("${strIngredient1.uppercase()} " ) }
                appendLine(strMeasure1 ?: "")
            }
            if(!strIngredient2.isNullOrBlank()) {
                bold { append("${strIngredient2.uppercase()} " ) }
                appendLine(strMeasure2 ?: "")
            }
            if(!strIngredient3.isNullOrBlank()) {
                bold { append("${strIngredient3.uppercase()} ") }
                appendLine(strMeasure3 ?: "")
            }
            if(!strIngredient4.isNullOrBlank()) {
                bold { append("${strIngredient4.uppercase()} ") }
                appendLine(strMeasure4 ?: "")
            }
            if(!strIngredient5.isNullOrBlank()) {
                bold { append("${strIngredient5.uppercase()} ") }
                appendLine(strMeasure5 ?: "")
            }
            if(!strIngredient6.isNullOrBlank()) {
                bold { append("${strIngredient6.uppercase()} ") }
                appendLine(strMeasure6 ?: "")
            }
            if(!strIngredient7.isNullOrBlank()) {
                bold { append("${strIngredient7.uppercase()} ") }
                appendLine(strMeasure7 ?: "")
            }
            if(!strIngredient8.isNullOrBlank()) {
                bold { append("${strIngredient8.uppercase()} ") }
                appendLine(strMeasure8 ?: "")
            }
            if(!strIngredient9.isNullOrBlank()) {
                bold { append("${strIngredient9.uppercase()} ") }
                appendLine(strMeasure9 ?: "")
            }
            if(!strIngredient10.isNullOrBlank()) {
                bold { append("${strIngredient10.uppercase()} ") }
                appendLine(strMeasure10 ?: "")
            }
            if(!strIngredient11.isNullOrBlank()) {
                bold { append("${strIngredient11.uppercase()} ") }
                appendLine(strMeasure11 ?: "")
            }
            if(!strIngredient12.isNullOrBlank()) {
                bold { append("${strIngredient12.uppercase()} ") }
                appendLine(strMeasure12 ?: "")
            }
            if(!strIngredient13.isNullOrBlank()) {
                bold { append("${strIngredient13.uppercase()} ") }
                appendLine(strMeasure13 ?: "")
            }
            if(!strIngredient14.isNullOrBlank()) {
                bold { append("${strIngredient14.uppercase()} ") }
                appendLine(strMeasure14 ?: "")
            }
            if(!strIngredient15.isNullOrBlank()) {
                bold { append("${strIngredient15.uppercase()} ") }
                appendLine(strMeasure15 ?: "")
            }
            if(!strIngredient16.isNullOrBlank()) {
                bold { append("${strIngredient16.uppercase()} ") }
                appendLine(strMeasure16 ?: "")
            }
            if(!strIngredient17.isNullOrBlank()) {
                bold { append("${strIngredient17.uppercase()} ") }
                appendLine(strMeasure17 ?: "")
            }
            if(!strIngredient18.isNullOrBlank()) {
                bold { append("${strIngredient18.uppercase()} ") }
                appendLine(strMeasure18 ?: "")
            }
            if(!strIngredient19.isNullOrBlank()) {
                bold { append("${strIngredient19.uppercase()} ") }
                appendLine(strMeasure19 ?: "")
            }
            if(!strIngredient20.isNullOrBlank()) {
                bold { append("${strIngredient20.uppercase()} ") }
                appendLine(strMeasure20 ?: "")
            }

        }

    }
}