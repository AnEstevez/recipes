package com.andresestevez.recipes.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ActivityMainBinding
import com.andresestevez.recipes.ui.adapters.ViewPagerAdapter
import com.andresestevez.recipes.ui.fragments.FavFragment
import com.andresestevez.recipes.ui.fragments.LocalRecipesFragment
import com.andresestevez.recipes.ui.fragments.SearchFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    companion object {
        const val LOCAL_RECIPES_FRAGMENT = 2
    }

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> getChildFragment<LocalRecipesFragment>(
                    binding.viewPager.adapter as ViewPagerAdapter,
                    LOCAL_RECIPES_FRAGMENT).requestLocalRecipes()
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ->
                    toast("Permission required to find local dishes")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = listOf<Fragment>(FavFragment(), SearchFragment(), LocalRecipesFragment())
        binding.viewPager.adapter = ViewPagerAdapter(fragments, this)

        val icons = listOf(
            R.drawable.ic_baseline_favorite_24,
            R.drawable.ic_baseline_search_24,
            R.drawable.ic_baseline_location_searching_24
        )
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == LOCAL_RECIPES_FRAGMENT) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab?.position == LOCAL_RECIPES_FRAGMENT) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }

        })
    }

}