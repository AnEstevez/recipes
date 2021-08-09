package com.andresestevez.recipes.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andresestevez.recipes.databinding.ActivityDetailBinding
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity(), DetailPresenter.View {

    companion object {
        const val EXTRA_RECIPE_ID = "DetailActivity:recipeId"
    }

    private lateinit var binding: ActivityDetailBinding

    private val presenter by lazy { DetailPresenter(RecipesRepository(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onCreate(this, intent.getStringExtra(EXTRA_RECIPE_ID))
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun updateUI(recipe: Recipe?) {
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