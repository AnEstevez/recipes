package com.andresestevez.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ViewSearchItemBinding
import com.andresestevez.recipes.models.Recipe
import com.bumptech.glide.Glide

class RecipesAdapter(private val items: List<Recipe>, private val recipeClickedListener : (Recipe) -> Unit) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = items[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener { recipeClickedListener(recipe) }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewSearchItemBinding.bind(view)

        fun bind(recipe: Recipe) {
            with(binding){
                textViewRecipe.text = recipe.name
                Glide.with(root.context).load(recipe.thumbnail).into(imageViewBg)
            }
        }
    }

}