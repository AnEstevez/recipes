package com.andresestevez.recipes.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentDetailBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    @VisibleForTesting
    val viewModel : DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingBtn.setOnClickListener { viewModel.onFavoriteClicked() }
        viewModel.model.observe(viewLifecycleOwner, {updateUI(it)})

    }

    private fun updateUI(model: DetailViewModel.UiModel) {

        when (model) {
            is DetailViewModel.UiModel.Content -> {
                model.recipe.let {
                    with(binding) {
                        Glide.with(this@DetailFragment).load(it.thumbnail).into(imageView)
                        toolbar.title = it.name
                        ingredients.setIngredients(it)
                        instructions.text = it.instructions
                        val favIcon = if (it.favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                        floatingBtn.setImageDrawable(getDrawable(requireContext(), favIcon))
                    }
                }
            }
            is DetailViewModel.UiModel.Favorite ->   {
                model.recipe.let {
                    val favIcon = if (it.favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                    binding.floatingBtn.setImageDrawable(getDrawable(requireContext(), favIcon))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
