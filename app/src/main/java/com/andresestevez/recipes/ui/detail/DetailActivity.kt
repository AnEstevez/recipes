package com.andresestevez.recipes.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andresestevez.recipes.databinding.ActivityDetailBinding
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.common.getViewModel
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECIPE_ID = "DetailActivity:recipeId"
    }

    private lateinit var binding: ActivityDetailBinding

    private val recipesRepository by lazy { RecipesRepository(this) }

    private lateinit var viewModel : DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // viewModel = ViewModelProvider(this, DetailViewModelFactory(recipesRepository)).get()
        viewModel = getViewModel { DetailViewModel(recipesRepository) }
        viewModel.model.observe(this, {updateUI(it)})

        viewModel.refresh(intent.getStringExtra(EXTRA_RECIPE_ID))
    }

    private fun updateUI(model: DetailViewModel.UiModel) {

        when (model) {
            is DetailViewModel.UiModel.Content -> {
                model.recipe?.let {
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