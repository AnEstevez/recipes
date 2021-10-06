package com.andresestevez.recipes.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.andresestevez.recipes.databinding.FragmentLocalRecipesBinding
import com.andresestevez.recipes.ui.common.EventObserver
import com.andresestevez.recipes.ui.main.MainFragmentDirections
import com.andresestevez.recipes.ui.main.RecipesAdapter
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModel.UiModel
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModel.UiModel.Content
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModel.UiModel.Loading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalRecipesFragment : Fragment() {

    private var _binding: FragmentLocalRecipesBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    private val viewModel: LocalRecipesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLocalRecipesBinding.inflate(inflater, container, false)

        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter(viewModel::onRecipeClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter
        viewModel.model.observe(viewLifecycleOwner, {updateUi(it)})
        viewModel.navigation.observe(viewLifecycleOwner, EventObserver {
            val direction = MainFragmentDirections.actionMainFragmentToDetailFragment(it)
            view.findNavController().navigate(direction)
        })

    }

    private fun updateUi(model: UiModel) {
        binding.progress.visibility = if (model == Loading) View.VISIBLE else View.GONE
        when(model) {
            is Content -> adapter.submitList(model.recipes)
            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}