package com.andresestevez.recipes.ui.main.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.andresestevez.recipes.ui.common.PermissionRequester
import javax.inject.Inject

class RecipesFragmentFactory
@Inject
constructor(
    private val permissionRequester: PermissionRequester,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            LocalRecipesFragment::class.java.name -> LocalRecipesFragment(permissionRequester)
            else -> super.instantiate(classLoader, className)
        }
    }
}