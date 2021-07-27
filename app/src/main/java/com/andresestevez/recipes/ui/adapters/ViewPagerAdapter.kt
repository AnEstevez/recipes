package com.andresestevez.recipes.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(private val fragments: List<Fragment>, fActivity: FragmentActivity) :
    FragmentStateAdapter(fActivity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}