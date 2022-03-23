package com.andresestevez.recipes.ui.main.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentLocalRecipesBinding
import com.andresestevez.recipes.ui.common.PermissionRequester
import com.andresestevez.recipes.ui.common.toast
import com.andresestevez.recipes.ui.main.RecipesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocalRecipesFragment(private val permissionRequester: PermissionRequester) :
    Fragment(R.layout.fragment_local_recipes) {

    private val adapter = RecipesAdapter()

    @VisibleForTesting
    val viewModel: LocalRecipesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLocalRecipesBinding.bind(view).apply {
            recycler.adapter = adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.map { state -> state.loading }.distinctUntilChanged().collect {
                        binding.progress.isVisible = it
                    }
                }

                launch {
                    viewModel.state.map { state -> state.data }.distinctUntilChanged().collect {
                        adapter.submitList(it)
                    }
                }

                launch {
                    viewModel.state.map { state -> state.userMessage }.distinctUntilChanged()
                        .collect {
                            it?.let { message -> requireContext().toast(message) }
                        }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.justSelected) {
            permissionRequester.runWithPermission {
                viewModel.refresh()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.justSelected = false
    }
}