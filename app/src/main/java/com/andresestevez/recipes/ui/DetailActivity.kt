package com.andresestevez.recipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andresestevez.recipes.databinding.ActivityDetailBinding
import com.andresestevez.recipes.models.Recipe

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECIPE = "DetailActivity:recipe"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipe = intent.getParcelableExtra<Recipe>(EXTRA_RECIPE)
        recipe?.let { toast(recipe.name) }
    }
}