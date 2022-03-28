package com.andresestevez.recipes.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentMainBinding
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        const val LOCAL_RECIPES_FRAGMENT = 2
    }

    private var tabMediator: TabLayoutMediator? = null

    private val localRecipesViewModel: LocalRecipesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)
        setupViewPagerAndTab(binding)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator?.detach()
        tabMediator = null
    }

    private fun setupViewPagerAndTab(binding: FragmentMainBinding) {

        binding.viewPager.adapter =
            ViewPagerAdapter(this, childFragmentManager, viewLifecycleOwner.lifecycle)
        val icons = listOf(
            R.drawable.ic_baseline_favorite_24,
            R.drawable.ic_baseline_search_24,
            R.drawable.ic_baseline_location_searching_24
        )

        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.registerOnPageChangeCallback(OnPageChangeCallback())

        tabMediator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            tab.setIcon(icons[position])
        }.also { it.attach() }

    }

    inner class OnPageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position == LOCAL_RECIPES_FRAGMENT) {
                localRecipesViewModel.justSelected = true
            }
        }
    }

}