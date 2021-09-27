package com.andresestevez.recipes.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentFavBinding
import com.andresestevez.recipes.data.PlayServicesLocationDataSource
import com.andresestevez.recipes.data.database.RoomDataSource
import com.andresestevez.recipes.data.server.MealDBDataSource
import com.andresestevez.recipes.ui.common.EventObserver
import com.andresestevez.recipes.ui.common.app
import com.andresestevez.recipes.ui.main.MainFragmentDirections
import com.andresestevez.recipes.ui.main.RecipesAdapter
import com.andresestevez.recipes.ui.main.fragments.FavViewModel.UiModel.Loading
import com.andresestevez.usecases.GetFavoriteRecipes


class FavFragment : Fragment() {

    private var _binding: FragmentFavBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter : RecipesAdapter

    private val viewModel: FavViewModel by activityViewModels {
        FavFragmentFactory(
            GetFavoriteRecipes(
                RecipesRepository(
                    RoomDataSource(app.db),
                    MealDBDataSource(),
                    PlayServicesLocationDataSource(app),
                    getString(R.string.api_key)
                )
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavBinding.inflate(inflater, container, false)

        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter(viewModel::onRecipeClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        viewModel.model.observe(viewLifecycleOwner, {
            updateUi(it)
        })

        viewModel.navigation.observe(viewLifecycleOwner, EventObserver {
            val direction = MainFragmentDirections.actionMainFragmentToDetailFragment(it)
            view.findNavController().navigate(direction)
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