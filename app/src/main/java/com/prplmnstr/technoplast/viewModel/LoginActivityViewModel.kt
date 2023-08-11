package com.prplmnstr.technoplast.viewModel
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import java.io.File


class LoginActivityViewModel : ViewModel() {

    fun getSharedPreferencesEditor(context: Context, preferenceName: String): SharedPreferences.Editor {
        val sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.edit()
    }
}