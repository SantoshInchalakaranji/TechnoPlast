package com.prplmnstr.technoplast.views.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prplmnstr.technoplast.R

class AdminActivity : AppCompatActivity() {

    private val fragmentManager: FragmentManager = supportFragmentManager

    private val machinesFragment = MachinesFragment()
    private val workerFragment = AddWorkerFragment()


    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_machine -> showFragment(machinesFragment)
                R.id.action_add_worker -> showFragment(workerFragment)

            }
            true
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Show the default fragment
        showFragment(machinesFragment)
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}