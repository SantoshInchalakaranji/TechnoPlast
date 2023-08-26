package com.prplmnstr.technoplast.views

import android.Manifest
import android.R
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.prplmnstr.technoplast.databinding.ActivityLoginBinding
import com.prplmnstr.technoplast.utils.Constants
import com.prplmnstr.technoplast.viewModel.LoginActivityViewModel
import com.prplmnstr.technoplast.views.admin.AdminActivity
import com.prplmnstr.technoplast.views.operator.OperatorActivity


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel
    private var spinnerAdapter: ArrayAdapter<String>? = null
    private lateinit var userType :String
    private lateinit var loader: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )


        initializeLoader()
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

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                userType = Constants.USER_TYPE_SIGN_IN[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if(email.isEmpty()){
                binding.emailEt.error = "Enter email ID"
                showToast("Enter email ID")
                return@setOnClickListener
            }
            if(password.isEmpty()){
                binding.passwordEt.error = "Enter password"
                showToast( "Enter password")
                return@setOnClickListener
            }
            if(!email.endsWith("@gmail.com")){
                binding.emailEt.error = "Enter valid email"
                showToast("Enter valid email")
                return@setOnClickListener
            }

            if(userType.equals(Constants.ADMIN) && !email.equals("admin@gmail.com")){
                binding.emailEt.error = "Wrong admin credentials"
                showToast("Wrong admin credentials")
                return@setOnClickListener
            }

            if(userType.equals(Constants.OPERATOR) && email.equals("admin@gmail.com")){
                binding.emailEt.error = "No operator exist"
                showToast("No operator exist")
                return@setOnClickListener
            }



            loader.show()
            viewModel.loginWithEmailAndPassword(email, password,
                onLoginSuccess = { user ->
                    loader.hide()
                    viewModel.saveUserType(this,userType)
                    showToast("Login successful")
                    if(userType.equals(Constants.ADMIN)){
                        startActivity(Intent(this, AdminActivity::class.java))
                    }else{
                        startActivity(Intent(this, OperatorActivity::class.java))
                    }

                    finish()
                },
                onLoginError = { errorMessage ->
                    loader.hide()

                    showToast("Login error: $errorMessage")
                }
            )
        }




    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


    private fun initializeLoader() {
        loader = Dialog(this)
        loader.setContentView(com.prplmnstr.technoplast.R.layout.loader)
        loader.window?.setBackgroundDrawableResource(com.prplmnstr.technoplast.R.color.transparent)
        loader.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        loader.setCancelable(false)
        loader.window?.attributes?.windowAnimations = com.prplmnstr.technoplast.R.style.animation
    }



}