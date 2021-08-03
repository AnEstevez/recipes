package com.andresestevez.recipes.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ActivityDetailBinding
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.TheMealDbClient
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECIPE_ID = "DetailActivity:recipeId"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeId = intent.getStringExtra(EXTRA_RECIPE_ID)
        val apiKey = this.applicationContext.getString(R.string.api_key)
        lifecycleScope.launch {
            recipeId?.let {
                val result = TheMealDbClient.service.findMealById(apiKey, it)
                val recipe = result.meals.firstOrNull()

                recipe?.let {
                    with(binding) {
                        Glide.with(this@DetailActivity).load(it.thumbnail).into(imageView)
                        toolbar.title = it.name
                        bindIngredients(ingredients, it)
                        instructions.text = it.instructions
                    }
                }
            }
        }
    }

    private fun bindIngredients(ingredients: TextView, recipe: Recipe) {
        ingredients.text = buildSpannedString {
            if(!recipe.strIngredient1.isNullOrBlank()) {
                bold { append("${recipe.strIngredient1.uppercase()} " ) }
                appendLine(recipe.strMeasure1 ?: "")
            }
            if(!recipe.strIngredient2.isNullOrBlank()) {
                bold { append("${recipe.strIngredient2.uppercase()} " ) }
                appendLine(recipe.strMeasure2 ?: "")
            }
            if(!recipe.strIngredient3.isNullOrBlank()) {
                bold { append("${recipe.strIngredient3.uppercase()} ") }
                appendLine(recipe.strMeasure3 ?: "")
            }
            if(!recipe.strIngredient4.isNullOrBlank()) {
                bold { append("${recipe.strIngredient4.uppercase()} ") }
                appendLine(recipe.strMeasure4 ?: "")
            }
            if(!recipe.strIngredient5.isNullOrBlank()) {
                bold { append("${recipe.strIngredient5.uppercase()} ") }
                appendLine(recipe.strMeasure5 ?: "")
            }
            if(!recipe.strIngredient6.isNullOrBlank()) {
                bold { append("${recipe.strIngredient6.uppercase()} ") }
                appendLine(recipe.strMeasure6 ?: "")
            }
            if(!recipe.strIngredient7.isNullOrBlank()) {
                bold { append("${recipe.strIngredient7.uppercase()} ") }
                appendLine(recipe.strMeasure7 ?: "")
            }
            if(!recipe.strIngredient8.isNullOrBlank()) {
                bold { append("${recipe.strIngredient8.uppercase()} ") }
                appendLine(recipe.strMeasure8 ?: "")
            }
            if(!recipe.strIngredient9.isNullOrBlank()) {
                bold { append("${recipe.strIngredient9.uppercase()} ") }
                appendLine(recipe.strMeasure9 ?: "")
            }
            if(!recipe.strIngredient10.isNullOrBlank()) {
                bold { append("${recipe.strIngredient10.uppercase()} ") }
                appendLine(recipe.strMeasure10 ?: "")
            }
            if(!recipe.strIngredient11.isNullOrBlank()) {
                bold { append("${recipe.strIngredient11.uppercase()} ") }
                appendLine(recipe.strMeasure11 ?: "")
            }
            if(!recipe.strIngredient12.isNullOrBlank()) {
                bold { append("${recipe.strIngredient12.uppercase()} ") }
                appendLine(recipe.strMeasure12 ?: "")
            }
            if(!recipe.strIngredient13.isNullOrBlank()) {
                bold { append("${recipe.strIngredient13.uppercase()} ") }
                appendLine(recipe.strMeasure13 ?: "")
            }
            if(!recipe.strIngredient14.isNullOrBlank()) {
                bold { append("${recipe.strIngredient14.uppercase()} ") }
                appendLine(recipe.strMeasure14 ?: "")
            }
            if(!recipe.strIngredient15.isNullOrBlank()) {
                bold { append("${recipe.strIngredient15.uppercase()} ") }
                appendLine(recipe.strMeasure15 ?: "")
            }
            if(!recipe.strIngredient16.isNullOrBlank()) {
                bold { append("${recipe.strIngredient16.uppercase()} ") }
                appendLine(recipe.strMeasure16 ?: "")
            }
            if(!recipe.strIngredient17.isNullOrBlank()) {
                bold { append("${recipe.strIngredient17.uppercase()} ") }
                appendLine(recipe.strMeasure17 ?: "")
            }
            if(!recipe.strIngredient18.isNullOrBlank()) {
                bold { append("${recipe.strIngredient18.uppercase()} ") }
                appendLine(recipe.strMeasure18 ?: "")
            }
            if(!recipe.strIngredient19.isNullOrBlank()) {
                bold { append("${recipe.strIngredient19.uppercase()} ") }
                appendLine(recipe.strMeasure19 ?: "")
            }
            if(!recipe.strIngredient20.isNullOrBlank()) {
                bold { append("${recipe.strIngredient20.uppercase()} ") }
                appendLine(recipe.strMeasure20 ?: "")
            }

        }


    }
}