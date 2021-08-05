package com.andresestevez.recipes.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.databinding.FragmentSearchBinding
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.DetailActivity
import com.andresestevez.recipes.ui.adapters.RecipesAdapter
import com.andresestevez.recipes.ui.hideKeyboard
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding : FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    private val recipesRepository by lazy { RecipesRepository(requireActivity())  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter {navigateTo(it.id) }
    }

    private fun navigateTo(recipeId: String) {
        val intent = Intent(this.context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, recipeId)

        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun requestRecipesByName(name: String) {
        lifecycleScope.launch {
            val mealsByName = recipesRepository.listRecipesByName(name)
            adapter.submitList(mealsByName.meals)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrBlank()) {
            requestRecipesByName(query)
            binding.root.hideKeyboard()
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = true
}