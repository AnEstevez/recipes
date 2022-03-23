package com.andresestevez.recipes.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andresestevez.recipes.ui.main.fragments.FavFragment
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesFragment
import com.andresestevez.recipes.ui.main.fragments.SearchFragment

class ViewPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fActivity: FragmentActivity,
) :
    FragmentStateAdapter(fActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> fragmentManager.fragmentFactory.instantiate(fActivity.classLoader,
            FavFragment::class.java.name)
        1 -> fragmentManager.fragmentFactory.instantiate(fActivity.classLoader,
            SearchFragment::class.java.name)
        2 -> fragmentManager.fragmentFactory.instantiate(fActivity.classLoader,
            LocalRecipesFragment::class.java.name)
        else -> throw IllegalStateException("Unexpected position $position")
    }
}

