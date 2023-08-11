package com.prplmnstr.technoplast.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.prplmnstr.technoplast.databinding.ActivityLoginBinding
import com.prplmnstr.technoplast.utils.Constants
import com.prplmnstr.technoplast.viewModel.LoginActivityViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)


        val editor = viewModel.getSharedPreferencesEditor(this, Constants.SHARED_PREFERENCE)



        editor.putString(Constants.SAVED_USER_TYPE, Constants.ADMIN)

        editor.apply()

    }






}