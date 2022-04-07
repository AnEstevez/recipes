package com.andresestevez.recipes.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andresestevez.domain.Recipe
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentDetailBinding
import com.andresestevez.recipes.presentation.common.toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    @VisibleForTesting
    val viewModel: DetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view).apply {
            floatingBtn.setOnClickListener { viewModel.onFavoriteClicked() }
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
                        it?.let { recipe -> binding.updateUI(recipe) }
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

    private fun FragmentDetailBinding.updateUI(recipe: Recipe) {
        recipe.let {
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setDuration(1500)
                .setBaseAlpha(0.9f)
                .setHighlightAlpha(1f)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()

            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
            Glide.with(imageView.context)
                .load(it.thumbnail)
                .apply(
                    RequestOptions.fitCenterTransform()
                        .placeholder(shimmerDrawable)
                        .error(R.drawable.ic_error)
                ).centerCrop()
                .into(imageView)
            toolbar.title = it.name
            ingredients.setIngredients(it)
            instructions.text = it.instructions
            val favIcon =
                if (it.favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            floatingBtn.setImageDrawable(getDrawable(requireContext(), favIcon))
            floatingBtn.tag = favIcon
        }
    }

}
