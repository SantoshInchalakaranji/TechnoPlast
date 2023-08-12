package com.prplmnstr.technoplast.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.prplmnstr.technoplast.databinding.ActivityOperatorBinding
import com.prplmnstr.technoplast.utils.CreatePdf
import java.security.AccessController.getContext

class OperatorActivity : AppCompatActivity() {

    val createPdf: CreatePdf = CreatePdf()
    private lateinit var binding : ActivityOperatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperatorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.submitBtn.setOnClickListener {
            createPdf.createPdf(this, this)
        }
    }
}