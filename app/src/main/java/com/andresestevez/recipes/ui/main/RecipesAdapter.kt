package com.andresestevez.recipes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ViewItemBinding
import com.bumptech.glide.Glide

class RecipesAdapter(private val recipeClickedListener: (Recipe) -> Unit) :
    ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
        holder.itemView.setOnClickListener { recipeClickedListener(recipe) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewItemBinding.bind(view)

        fun bind(recipe: Recipe) {
            with(binding) {
                textViewRecipe.text = recipe.name
                Glide.with(root.context).load(recipe.thumbnail).into(imageViewBg)
                this.btnFav.setImageResource(if (recipe.favorite) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24)
            }
        }
    }

    private object DiffUtilCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean = oldItem == newItem
    }

}