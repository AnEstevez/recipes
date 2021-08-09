package com.andresestevez.recipes.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andresestevez.recipes.R
import com.andresestevez.recipes.ui.main.ViewPagerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun <T> Activity.getChildFragment(adapter: ViewPagerAdapter, fragmentPosition: Int): T {
    return adapter.createFragment(fragmentPosition) as T
}

fun Context.openAppSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("package:$packageName")
    }.let (::startActivity)
}

fun Activity.snackBar(message: CharSequence, view: View? = findViewById(R.id.container), duration: Int = Snackbar.LENGTH_SHORT, action: String? =
    null, actionEvt: (v: View) -> Unit = {}) {
    if (view != null) {
        var snackBar = Snackbar.make(view, message, duration)
        if (!action.isNullOrEmpty()) {
            snackBar.setAction(action, actionEvt)
        }
        snackBar.show()
    }
}

fun <VH : RecyclerView.ViewHolder, T> RecyclerView.Adapter<VH>.basicDiffUtil(
    initialValue: List<T> = emptyList(),
    areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) =
    Delegates.observable(initialValue) { _, old, new ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = new.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areContentsTheSame(old[oldItemPosition], new[newItemPosition])

        }).dispatchUpdatesTo(this)
    }

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}
