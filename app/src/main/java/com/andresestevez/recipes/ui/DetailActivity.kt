package com.andresestevez.recipes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ActivityDetailBinding
import com.andresestevez.recipes.models.RecipesRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECIPE_ID = "DetailActivity:recipeId"
    }

    private lateinit var binding: ActivityDetailBinding

    private val recipesRepository by lazy { RecipesRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeId = intent.getStringExtra(EXTRA_RECIPE_ID)
        val apiKey = this.applicationContext.getString(R.string.api_key)
        lifecycleScope.launch {
            recipeId?.let {

                val recipe = recipesRepository.findRecipeById(it)

                recipe?.let {
                    with(binding) {
                        Glide.with(this@DetailActivity).load(it.thumbnail).into(imageView)
                        toolbar.title = it.name
                        ingredients.setIngredients(it)
                        instructions.text = it.instructions
                    }
                }
            }
        }
    }

}