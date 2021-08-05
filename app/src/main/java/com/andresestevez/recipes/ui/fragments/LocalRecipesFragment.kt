package com.andresestevez.recipes.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.databinding.FragmentLocalRecipesBinding
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.DetailActivity
import com.andresestevez.recipes.ui.adapters.RecipesAdapter
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class LocalRecipesFragment : Fragment() {

    private var _binding: FragmentLocalRecipesBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    private val recipesRepository by lazy { RecipesRepository(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentLocalRecipesBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter { navigateTo(it.id) }
    }

    private fun navigateTo(recipeId: String) {
        val intent = Intent(this.context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, recipeId)

        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter
    }

    fun requestLocalRecipes() {
        lifecycleScope.launch {
            val mealsByNationality = recipesRepository.listRecipesByRegion()
            mealsByNationality?.let { adapter.submitList(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}