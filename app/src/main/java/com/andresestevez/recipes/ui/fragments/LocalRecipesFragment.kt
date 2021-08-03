package com.andresestevez.recipes.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentLocalRecipesBinding
import com.andresestevez.recipes.models.CountryCodeToNationality
import com.andresestevez.recipes.models.TheMealDbClient
import com.andresestevez.recipes.ui.DetailActivity
import com.andresestevez.recipes.ui.adapters.RecipesAdapter
import com.andresestevez.recipes.ui.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class LocalRecipesFragment : Fragment() {

    companion object {
        private const val DEFAULT_COUNTRY_CODE = "XX"
    }

    private var _binding: FragmentLocalRecipesBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: RecipesAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                when {
                    isGranted -> requestLocalRecipes()
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ->
                        context?.toast("Permission required to find local dishes")
                }
        }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        // Inflate the layout for this fragment
        _binding = FragmentLocalRecipesBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = RecipesAdapter { navigateTo(it.id) }
    }

    private fun navigateTo(recipeId: String) {
        val intent = Intent(this.context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, recipeId)

        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocalRecipes() {
        lifecycleScope.launch {
            val countryCode = getCountryCode()
            val nationality =
                if (CountryCodeToNationality.values()
                        .map { country -> country.name }
                        .contains(countryCode))
                    CountryCodeToNationality.valueOf(countryCode).nationality
                else CountryCodeToNationality.XX.nationality
            val mealsByNationality =
                TheMealDbClient.service.listMealsByNationality(
                    getString(R.string.api_key),
                    nationality.lowercase()
                )
            adapter.submitList(mealsByNationality.meals)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCountryCode(): String =
        suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                continuation.resume(getCountryCodeFromLocation(it?.result))
            }
        }

    private fun getCountryCodeFromLocation(location: Location?): String {
        if (location == null) {
            return DEFAULT_COUNTRY_CODE
        }

        val geocoder = Geocoder(context)

        return geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )?.firstOrNull()?.countryCode ?: DEFAULT_COUNTRY_CODE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}