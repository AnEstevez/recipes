package com.andresestevez.recipes.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andresestevez.recipes.ui.main.MainViewModel.UiModel.RequestLocalRecipes
import com.google.android.material.tabs.TabLayout
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    lateinit var vm: MainViewModel

    @Mock
    lateinit var observer: Observer<MainViewModel.UiModel>

    @Mock
    lateinit var tab: TabLayout.Tab

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        vm = MainViewModel()
    }

    @Test
    fun `when onTabSelected, RequestLocalRecipes is called`() {
        // Given
        vm.model.observeForever(observer)

        // When
        vm.onTabSelected(tab)

        // Then
        verify(observer).onChanged(RequestLocalRecipes(tab))
        Assert.assertEquals(RequestLocalRecipes(tab), vm.model.value)
        vm.model.removeObserver(observer)
    }

    @Test
    fun `when onTabReselected, RequestLocalRecipes is called`() {
        // Given
        vm.model.observeForever(observer)

        // When
        vm.onTabSelected(tab)

        // Then
        verify(observer).onChanged(RequestLocalRecipes(tab))
        Assert.assertEquals(RequestLocalRecipes(tab), vm.model.value)
        vm.model.removeObserver(observer)
    }

}