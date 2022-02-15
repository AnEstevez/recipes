package com.andresestevez.recipes.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.andresestevez.recipes.databinding.FragmentLocalRecipesBinding
import com.andresestevez.recipes.ui.common.EventObserver
import com.andresestevez.recipes.ui.common.toast
import com.andresestevez.recipes.ui.main.MainFragmentDirections
import com.andresestevez.recipes.ui.main.RecipesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocalRecipesFragment : Fragment() {

    private var _binding: FragmentLocalRecipesBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    @VisibleForTesting
    val viewModel: LocalRecipesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    binding.progress.isVisible = it.loading
                    adapter.submitList(it.data)
                    it.userMessage?.let { message -> requireContext().toast(message) }
                }
            }
        }

        viewModel.navigation.observe(viewLifecycleOwner, EventObserver {
            val direction = MainFragmentDirections.actionMainFragmentToDetailFragment(it)
            view.findNavController().navigate(direction)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}