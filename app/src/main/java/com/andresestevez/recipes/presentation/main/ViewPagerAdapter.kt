package com.andresestevez.recipes.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andresestevez.recipes.presentation.main.fragments.FavFragment
import com.andresestevez.recipes.presentation.main.fragments.LocalRecipesFragment
import com.andresestevez.recipes.presentation.main.fragments.SearchFragment

class ViewPagerAdapter(
    private val fragment: Fragment,
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    ) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> fragmentManager.fragmentFactory.instantiate(fragment.requireContext().classLoader,
            FavFragment::class.java.name)
        1 -> fragmentManager.fragmentFactory.instantiate(fragment.requireContext().classLoader,
            SearchFragment::class.java.name)
        2 -> fragmentManager.fragmentFactory.instantiate(fragment.requireContext().classLoader,
            LocalRecipesFragment::class.java.name)
        else -> throw IllegalStateException("Unexpected position $position")
    }
}

