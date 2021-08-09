package com.andresestevez.recipes.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.andresestevez.recipes.databinding.FragmentSearchBinding
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.common.hideKeyboard
import com.andresestevez.recipes.ui.detail.DetailActivity
import com.andresestevez.recipes.ui.main.RecipesAdapter


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), SearchView.OnQueryTextListener, SearchPresenter.View {

    private var _binding : FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    private val presenter by lazy {SearchPresenter(RecipesRepository(requireActivity()))}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter { presenter.onRecipeClicked(it.id) }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter

        presenter.onViewCreated(this)

        binding.searchView.setOnQueryTextListener(this)
    }

    override fun onDestroyView() {
        presenter.onDestroyView()
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.onQueryTextSubmited(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = true

    override fun updateData(recipes: List<Recipe>) {
        adapter.submitList(recipes)
    }

    override fun navigateTo(recipeId: String) {
        val intent = Intent(this.context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, recipeId)

        startActivity(intent)
    }

    override fun hideKeyboard() {
        binding.root.hideKeyboard()
    }

    override fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progress.visibility = View.GONE
    }
}