package com.prplmnstr.technoplast.viewModel
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.prplmnstr.technoplast.utils.Constants
import java.io.File


class LoginActivityViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    fun getSharedPreferencesEditor(context: Context, preferenceName: String): SharedPreferences.Editor {
        val sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.edit()
    }

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onLoginSuccess: (FirebaseUser) -> Unit,
        onLoginError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                onLoginSuccess(authResult.user!!)
            }
            .addOnFailureListener { exception ->
                onLoginError(exception.message ?: "Login failed")
            }
    }

    fun saveUserType(context: Context, userType:String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(Constants.SAVED_USER_TYPE, userType)
        editor.apply()
    }
}
