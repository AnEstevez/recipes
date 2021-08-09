package com.andresestevez.recipes.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andresestevez.recipes.databinding.FragmentLocalRecipesBinding
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.detail.DetailActivity
import com.andresestevez.recipes.ui.main.RecipesAdapter

class LocalRecipesFragment : Fragment(), LocalRecipesPresenter.View {

    private var _binding: FragmentLocalRecipesBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    private val presenter by lazy { LocalRecipesPresenter(RecipesRepository(requireActivity())) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLocalRecipesBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter { presenter.onRecipeClicked(it.id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated(this)
        binding.recycler.adapter = adapter
    }

    fun requestLocalRecipes() {
        presenter.onLocalRecipesRequested()
    }

    override fun onDestroyView() {
        presenter.onDestroyView()
        super.onDestroyView()
        _binding = null
    }

    override fun navigateTo(recipeId: String) {
        val intent = Intent(this.context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, recipeId)

        startActivity(intent)
    }

    override fun updateData(recipes: List<Recipe>?) {
        adapter.submitList(recipes)
    }
}