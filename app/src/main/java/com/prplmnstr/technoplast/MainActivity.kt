package com.prplmnstr.technoplast

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.prplmnstr.technoplast.databinding.ActivityMainBinding
import com.prplmnstr.technoplast.utils.Constants
import com.prplmnstr.technoplast.views.admin.AdminActivity
import com.prplmnstr.technoplast.views.LoginActivity
import com.prplmnstr.technoplast.views.OperatorActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var userType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )




        val currentUser = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed({
            if (currentUser == null) {
                startActivity(Intent(this, AdminActivity::class.java))
            } else {

                try {
                    val sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE)
                    val key = sharedPreferences.contains(Constants.SAVED_USER_TYPE)
                    if (key) {
                        userType = sharedPreferences.getString(Constants.SAVED_USER_TYPE, null)
                    } else {
                        userType = null
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Please reload the app", Toast.LENGTH_SHORT).show()
                }
                if(userType.equals(Constants.ADMIN)){
                    startActivity(Intent(this, AdminActivity::class.java))
                }else{
                    startActivity(Intent(this, OperatorActivity::class.java))
                }

            }


            finish()
        }, 1000)
    }
}