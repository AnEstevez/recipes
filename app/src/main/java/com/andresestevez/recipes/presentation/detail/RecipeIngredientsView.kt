package com.andresestevez.recipes.presentation.detail
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.andresestevez.domain.Recipe

class RecipeIngredientsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    fun setIngredients(recipe: Recipe){
        with(recipe) {
            text = buildSpannedString {
                for(i in 0..ingredients.lastIndex){
                    if(ingredients[i].isNotBlank()) {
                        bold { append("${ingredients[i].uppercase()} " ) }
                        appendLine(measures[i])
                    }
                }
            }
        }
    }

}