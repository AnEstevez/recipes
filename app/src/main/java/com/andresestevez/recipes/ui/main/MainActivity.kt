package com.andresestevez.recipes.ui.main

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ActivityMainBinding
import com.andresestevez.recipes.models.PermissionRequester
import com.andresestevez.recipes.models.RecipesRepository
import com.andresestevez.recipes.ui.common.toast
import com.andresestevez.recipes.ui.main.MainViewModel.Companion.LOCAL_RECIPES_FRAGMENT
import com.andresestevez.recipes.ui.main.MainViewModel.UiModel
import com.andresestevez.recipes.ui.main.MainViewModel.UiModel.RequestLocalRecipes
import com.andresestevez.recipes.ui.main.fragments.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private val localRecipesViewModel: LocalRecipesViewModel by viewModels { LocalRecipesViewModelFactory(
        RecipesRepository(application)
    ) }


    private val permissionRequester =
        PermissionRequester(this as ComponentActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            onRationale = {application.toast(application.getString(R.string.rationale_local_dishes))}
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments: List<Fragment> = listOf(FavFragment(), SearchFragment(), LocalRecipesFragment())
        binding.viewPager.adapter = ViewPagerAdapter(fragments, this)
        val icons = listOf(
            R.drawable.ic_baseline_favorite_24,
            R.drawable.ic_baseline_search_24,
            R.drawable.ic_baseline_location_searching_24
        )

        binding.viewPager.offscreenPageLimit = icons.size

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

        viewModel.model.observe(this, { findLocalRecipes(it) })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.onTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.onTabReselected(tab)
            }

        })

    }

    private fun findLocalRecipes(model: UiModel) {
        when (model) {
            is RequestLocalRecipes ->  if (model.tab?.position == LOCAL_RECIPES_FRAGMENT) {
                permissionRequester.runWithPermission {
                    localRecipesViewModel.refresh()
                }
            }
        }
    }
}