package com.andresestevez.recipes.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andresestevez.recipes.ui.main.MainViewModel.UiModel.RequestLocalRecipes
import com.google.android.material.tabs.TabLayout

class MainViewModel : ViewModel() {

    sealed class UiModel {
        class RequestLocalRecipes(val tab: TabLayout.Tab?): UiModel()
    }

    companion object {
        const val LOCAL_RECIPES_FRAGMENT = 2
    }

    private val _model = MutableLiveData<UiModel>()
    val model : LiveData<UiModel>
        get() = _model



    fun onTabSelected(tab: TabLayout.Tab?) {
        _model.value = RequestLocalRecipes(tab)
    }

    fun onTabReselected(tab: TabLayout.Tab?) {
        _model.value = RequestLocalRecipes(tab)
    }
}
