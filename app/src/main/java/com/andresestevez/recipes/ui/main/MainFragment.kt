package com.andresestevez.recipes.ui.main

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.andresestevez.recipes.R
import com.andresestevez.recipes.databinding.FragmentMainBinding
import com.andresestevez.recipes.ui.common.PermissionRequester
import com.andresestevez.recipes.ui.common.app
import com.andresestevez.recipes.ui.common.toast
import com.andresestevez.recipes.ui.main.fragments.FavFragment
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesFragment
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModel
import com.andresestevez.recipes.ui.main.fragments.SearchFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding : FragmentMainBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private val localRecipesViewModel: LocalRecipesViewModel by activityViewModels()

    @Inject lateinit var permissionRequester: PermissionRequester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionRequester = PermissionRequester(requireActivity() as ComponentActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            onRationale = {app.toast(app.getString(R.string.rationale_local_dishes))}
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPagerAndTab()

        viewModel.model.observe(viewLifecycleOwner, { findLocalRecipes(it) })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewPagerAndTab() {
        val fragments: List<Fragment> =
            listOf(FavFragment(), SearchFragment(), LocalRecipesFragment())
        binding.viewPager.adapter = ViewPagerAdapter(fragments, requireActivity())
        val icons = listOf(
            R.drawable.ic_baseline_favorite_24,
            R.drawable.ic_baseline_search_24,
            R.drawable.ic_baseline_location_searching_24
        )

        binding.viewPager.offscreenPageLimit = icons.size

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) = viewModel.onTabSelected(tab)
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) = viewModel.onTabReselected(tab)
        })
    }

    private fun findLocalRecipes(model: MainViewModel.UiModel) {
        when (model) {
            is MainViewModel.UiModel.RequestLocalRecipes ->  if (model.tab?.position == MainViewModel.LOCAL_RECIPES_FRAGMENT) {
                permissionRequester.runWithPermission {
                    localRecipesViewModel.refresh()
                }
            }
        }
    }

}