package com.andresestevez.recipes.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.ActivityMainBinding
import com.andresestevez.recipes.ui.common.getChildFragment
import com.andresestevez.recipes.ui.main.MainPresenter.Companion.LOCAL_RECIPES_FRAGMENT
import com.andresestevez.recipes.ui.main.fragments.FavFragment
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesFragment
import com.andresestevez.recipes.ui.main.fragments.SearchFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity(), MainPresenter.View {

    private lateinit var binding: ActivityMainBinding

    private val presenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onCreate(this)

        val fragments: List<Fragment> = listOf(FavFragment(), SearchFragment(), LocalRecipesFragment())
        binding.viewPager.adapter = ViewPagerAdapter(fragments, this)

        val icons = listOf(
            R.drawable.ic_baseline_favorite_24,
            R.drawable.ic_baseline_search_24,
            R.drawable.ic_baseline_location_searching_24
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                presenter.onTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                presenter.onTabReselected(tab)
            }

        })

    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun findLocalRecipes(tab: TabLayout.Tab?) {
        if (tab?.position == LOCAL_RECIPES_FRAGMENT) {
            presenter.permissionRequester.runWithPermission {
                getChildFragment<LocalRecipesFragment>(
                    binding.viewPager.adapter as ViewPagerAdapter,
                    LOCAL_RECIPES_FRAGMENT
                ).requestLocalRecipes()
            }
        }
    }
}