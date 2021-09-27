package com.andresestevez.recipes.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentDetailBinding
import com.andresestevez.recipes.data.PlayServicesLocationDataSource
import com.andresestevez.recipes.data.database.RoomDataSource
import com.andresestevez.recipes.data.server.MealDBDataSource
import com.andresestevez.recipes.ui.common.app
import com.andresestevez.usecases.GetRecipeById
import com.andresestevez.usecases.ToggleRecipeFavorite
import com.bumptech.glide.Glide

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val viewModel : DetailViewModel by viewModels {
        val recipesRepository = RecipesRepository(
            RoomDataSource(app.db),
            MealDBDataSource(),
            PlayServicesLocationDataSource(app),
            getString(R.string.api_key)
        )
        DetailViewModelFactory(
            GetRecipeById(recipesRepository),
            ToggleRecipeFavorite(recipesRepository)
        )
    }

    private val args: DetailFragmentArgs by navArgs()

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

        viewModel.refresh(args.recipeID)
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
