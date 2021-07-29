package com.andresestevez.recipes.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentSearchBinding
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.TheMealDbClient
import com.andresestevez.recipes.ui.DetailActivity
import com.andresestevez.recipes.ui.adapters.RecipesAdapter
import com.andresestevez.recipes.ui.hideKeyboard
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding : FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        adapter = RecipesAdapter {navigateTo(it) }
    }

    private fun navigateTo(recipe: Recipe) {
        val intent = Intent(this.context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_RECIPE, recipe)

        startActivity(intent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun searchRecipesByName(name: String) {
        lifecycleScope.launch {
            val mealsByName =
                TheMealDbClient.service.listMealsByName(getString(R.string.api_key), name.lowercase())
            adapter.submitList(mealsByName.meals)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrBlank()) {
            searchRecipesByName(query)
            binding.root.hideKeyboard()
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = true
}