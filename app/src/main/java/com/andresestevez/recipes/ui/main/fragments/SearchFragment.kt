package com.andresestevez.recipes.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.andresestevez.recipes.databinding.FragmentSearchBinding
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.common.hideKeyboard
import com.andresestevez.recipes.ui.common.startActivity
import com.andresestevez.recipes.ui.detail.DetailActivity
import com.andresestevez.recipes.ui.main.RecipesAdapter
import com.andresestevez.recipes.ui.main.fragments.SearchViewModel.UiModel
import com.andresestevez.recipes.ui.main.fragments.SearchViewModel.UiModel.*


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory(RecipesRepository(requireActivity().application)) }

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
        adapter = RecipesAdapter { viewModel.onRecipeClicked(it.id) }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter

        viewModel.model.observe(viewLifecycleOwner, Observer(::updateUi))

        binding.searchView.setOnQueryTextListener(this)
    }

    private fun updateUi(model: UiModel) {
        binding.progress.visibility = if (model == Loading) View.VISIBLE else View.GONE

        when (model){
            is Content -> adapter.submitList(model.recipes)
            HideKeyboard -> view?.hideKeyboard()
            is Navigation -> context?.startActivity<DetailActivity> {
                putExtra(DetailActivity.EXTRA_RECIPE_ID, model.recipeId)
            }
            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.refresh(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = true

}