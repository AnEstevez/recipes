package com.andresestevez.recipes.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.andresestevez.recipes.databinding.FragmentFavBinding
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.common.app
import com.andresestevez.recipes.ui.common.startActivity
import com.andresestevez.recipes.ui.detail.DetailActivity
import com.andresestevez.recipes.ui.main.RecipesAdapter
import com.andresestevez.recipes.ui.main.fragments.FavViewModel.UiModel.Loading


class FavFragment : Fragment() {

    private var _binding: FragmentFavBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter : RecipesAdapter

    private val viewModel: FavViewModel by activityViewModels { FavFragmentFactory(RecipesRepository(requireActivity().app)) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavBinding.inflate(inflater, container, false)

        initRecyclerView()
        return binding.root    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter {viewModel.onRecipeClicked(it.idMeal)}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        viewModel.model.observe(viewLifecycleOwner, {
            updateUi(it)
        })

        viewModel.navigation.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled().let {
                activity?.startActivity<DetailActivity> {
                    putExtra(DetailActivity.EXTRA_RECIPE_ID, it)
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun updateUi(model: FavViewModel.UiModel) {
        binding.progress.visibility = if (model == Loading) View.VISIBLE else View.GONE

        when(model) {
            is FavViewModel.UiModel.Content -> adapter.submitList(model.recipes)
            else -> {}

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}