package com.prplmnstr.technoplast.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.prplmnstr.technoplast.models.Machine
import com.prplmnstr.technoplast.models.Operator
import com.prplmnstr.technoplast.utils.Constants
import javax.crypto.Mac

class MachineFragmentViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    fun uploadMachine(machine: Machine,
          onComplete: (Boolean) -> Unit) {

        firestore.collection(Constants.MACHINES)
            .document(machine.name)
            .set(machine)
            .addOnCompleteListener { task ->
                val isSuccess = task.isSuccessful
                onComplete(isSuccess)
            }

    }
    fun deleteMachine(machineName: String,onComplete: (Boolean) -> Unit) {


        firestore.collection(Constants.MACHINES).document(machineName)
            .delete()
            .addOnCompleteListener { task ->
                val isSuccess = task.isSuccessful
                onComplete(isSuccess)
            }
    }
    fun retrieveMachines(): LiveData<List<Machine>> {
        val liveData = MutableLiveData<List<Machine>>()

        firestore.collection(Constants.MACHINES).get().addOnSuccessListener { result ->

            val machineList = mutableListOf<Machine>()
            for (document in result) {
                val machine = document.toObject(Machine::class.java)
                machineList.add(machine)
            }
            liveData.value = machineList
        }.addOnFailureListener { exception ->
            // Handle error
        }

        return liveData
    }

    fun isMachineExist(machineList: List<Machine>, machineName: String?): Boolean {
        for (machine in machineList) {
            if (machine.name.equals(machineName)) {
                return true // Email address exists in the list
            }
        }
        return false // Email address does not exist in the list
    }

    fun removeUserTypeFromSharedPref(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.remove(Constants.SAVED_USER_TYPE)
        editor.apply()
    }
}