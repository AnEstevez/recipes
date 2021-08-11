package com.andresestevez.recipes.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.andresestevez.recipes.databinding.FragmentLocalRecipesBinding
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.common.startActivity
import com.andresestevez.recipes.ui.detail.DetailActivity
import com.andresestevez.recipes.ui.main.RecipesAdapter
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModel.UiModel
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModel.UiModel.*

class LocalRecipesFragment : Fragment(){

    private var _binding: FragmentLocalRecipesBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    private lateinit var viewModel: LocalRecipesViewModel
    private val repository by lazy { RecipesRepository(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLocalRecipesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, LocalRecipesViewModelFactory(repository)).get()

        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter { viewModel.onRecipeClicked(it.id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter
        viewModel.model.observe(viewLifecycleOwner, {updateUi(it)})
    }

    private fun updateUi(model: UiModel) {
        binding.progress.visibility = if (model == Loading) View.VISIBLE else View.GONE
        when(model) {
            is Content -> adapter.submitList(model.recipes)
            is Navigation -> activity?.startActivity<DetailActivity> { putExtra(DetailActivity.EXTRA_RECIPE_ID, model.recipeId) }
            else -> {}
        }
    }

    fun requestLocalRecipes() {
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}