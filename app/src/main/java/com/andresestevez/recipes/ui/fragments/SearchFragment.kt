package com.andresestevez.recipes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.andresestevez.recipes.ui.adapters.RecipesAdapter
import com.andresestevez.recipes.databinding.FragmentSearchBinding
import com.andresestevez.recipes.models.Recipe
import com.andresestevez.recipes.ui.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initRecyclerView(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView(inflater: LayoutInflater, container: ViewGroup?, b: Boolean) {
        _binding = FragmentSearchBinding.inflate(inflater, container, b)
        adapter = RecipesAdapter { context?.toast("${it.name}", Toast.LENGTH_SHORT) }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recipesList = listOf(Recipe("Cocido Gallego", "https://www.themealdb.com/images/media/meals/2dsltq1560461468.jpg"),
            Recipe("Calamares a la romana", "https://www.themealdb.com/images/media/meals/7ttta31593350374.jpg"),
            Recipe("Steak Tartar", "https://www.themealdb.com/images/media/meals/1520081754.jpg"),
            Recipe("Lasaña", "https://www.themealdb.com/images/media/meals/rvxxuy1468312893.jpg"),
            Recipe("Cocido Gallego", "https://www.themealdb.com/images/media/meals/2dsltq1560461468.jpg"),
            Recipe("Cocido Gallego", "https://www.themealdb.com/images/media/meals/2dsltq1560461468.jpg"),
            Recipe("Cocido Gallego", "https://www.themealdb.com/images/media/meals/2dsltq1560461468.jpg"),
            Recipe("Cocido Gallego", "https://www.themealdb.com/images/media/meals/2dsltq1560461468.jpg"),
            Recipe("Cocido Gallego", "https://www.themealdb.com/images/media/meals/2dsltq1560461468.jpg"),
            Recipe("Cocido Gallego", "https://www.themealdb.com/images/media/meals/2dsltq1560461468.jpg"),
        Recipe("Calamares a la romana", "https://www.themealdb.com/images/media/meals/7ttta31593350374.jpg"),
        Recipe("Steak Tartar", "https://www.themealdb.com/images/media/meals/1520081754.jpg"),
        Recipe("Lasaña", "https://www.themealdb.com/images/media/meals/rvxxuy1468312893.jpg")
            )
        binding.recycler.adapter = adapter


        lifecycleScope.launch {
            var newRecipesList: List<Recipe> = emptyList()
            for (recipe in recipesList) {
                newRecipesList = listOf(recipe) + newRecipesList
                adapter.items = newRecipesList
                delay(1000)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}