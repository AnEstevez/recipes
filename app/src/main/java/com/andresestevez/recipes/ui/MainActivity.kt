package com.andresestevez.recipes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ActivityMainBinding
import com.andresestevez.recipes.ui.adapters.ViewPagerAdapter
import com.andresestevez.recipes.ui.fragments.FavFragment
import com.andresestevez.recipes.ui.fragments.LocalRecipesFragment
import com.andresestevez.recipes.ui.fragments.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
    }

}