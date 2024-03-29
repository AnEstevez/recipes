package com.andresestevez.recipes.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ViewItemBinding
import com.andresestevez.recipes.presentation.common.RecipeItemUiState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipesAdapter : ListAdapter<RecipeItemUiState, RecipesAdapter.ViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
        holder.itemView.setOnClickListener { navigateToDetail(recipe.id, it) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewItemBinding.bind(view)

        fun bind(recipe: RecipeItemUiState) {
            with(binding) {
                textViewRecipe.text = recipe.title
                val shimmer = Shimmer.AlphaHighlightBuilder()
                    .setDuration(1500)
                    .setBaseAlpha(0.9f)
                    .setHighlightAlpha(1f)
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build()
                val shimmerDrawable = ShimmerDrawable().apply {
                    setShimmer(shimmer)
                }
                Glide.with(imageViewBg.context)
                    .load(recipe.thumbnail)
                    .apply(
                        RequestOptions.fitCenterTransform()
                            .placeholder(shimmerDrawable)
                            .error((R.drawable.ic_error))
                    ).centerCrop()
                    .into(imageViewBg)
                val icon = if (recipe.bookmarked) R.drawable.ic_baseline_favorite_24
                else R.drawable.ic_baseline_favorite_border_24
                btnFav.setBackgroundResource(icon)
                btnFav.tag = icon
                btnFav.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        recipe.onBookmark()
                    }
                }
            }
        }
    }

    private fun navigateToDetail(recipeId: String, view: View) {
        val direction = MainFragmentDirections.actionMainFragmentToDetailFragment(recipeId)
        view.findNavController().navigate(direction)
    }

    private object DiffUtilCallback : DiffUtil.ItemCallback<RecipeItemUiState>() {
        override fun areItemsTheSame(
            oldItem: RecipeItemUiState,
            newItem: RecipeItemUiState,
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: RecipeItemUiState,
            newItem: RecipeItemUiState,
        ): Boolean =
            oldItem == newItem
    }

}