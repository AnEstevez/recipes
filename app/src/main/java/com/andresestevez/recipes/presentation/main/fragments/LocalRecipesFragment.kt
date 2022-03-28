package com.andresestevez.recipes.presentation.main.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentLocalRecipesBinding
import com.andresestevez.recipes.framework.PermissionRequester
import com.andresestevez.recipes.presentation.common.toast
import com.andresestevez.recipes.presentation.main.RecipesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocalRecipesFragment(private val permissionRequester: PermissionRequester) :
    Fragment(R.layout.fragment_local_recipes) {

    @VisibleForTesting
    val viewModel: LocalRecipesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecipesAdapter()
        val binding = FragmentLocalRecipesBinding.bind(view).apply {
            recycler.adapter = adapter
            (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearUserMessage()
    }
}