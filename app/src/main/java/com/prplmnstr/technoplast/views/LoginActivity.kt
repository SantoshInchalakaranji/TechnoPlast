package com.prplmnstr.technoplast.views

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.prplmnstr.technoplast.databinding.ActivityLoginBinding
import com.prplmnstr.technoplast.utils.Constants
import com.prplmnstr.technoplast.viewModel.LoginActivityViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel
    private var spinnerAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)


        // Get SharedPreferences instance


        // set spinner
        spinnerAdapter = ArrayAdapter<String>(
            this,
            R.layout.simple_spinner_item,
            Constants.USER_TYPE_SIGN_IN
        )
        spinnerAdapter!!.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.setAdapter(spinnerAdapter)


        val editor = viewModel.getSharedPreferencesEditor(this, Constants.SHARED_PREFERENCE)



        editor.putString(Constants.SAVED_USER_TYPE, Constants.ADMIN)

        editor.apply()

    }






}