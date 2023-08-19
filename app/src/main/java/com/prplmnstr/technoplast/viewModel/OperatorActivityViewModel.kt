package com.prplmnstr.technoplast.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.prplmnstr.technoplast.models.Machine
import com.prplmnstr.technoplast.models.Operator
import com.prplmnstr.technoplast.models.Record
import com.prplmnstr.technoplast.utils.Constants

class OperatorActivityViewModel : ViewModel(){

    private val firestore = FirebaseFirestore.getInstance()
    private val userNameLiveData = MutableLiveData<String>()


    fun showToast(s: String,context: Context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

     fun saveRecordToSharedPreferences(record: Record,sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val userJson = gson.toJson(record)
        editor.putString(Constants.RECORD, userJson)
        editor.apply()
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

    fun setMachineDetails(selectedMachine: Machine,shiftRecord: Record){

        shiftRecord.name = selectedMachine.name
        shiftRecord.mould = selectedMachine.mould
        shiftRecord.productionWT = selectedMachine.productionWT
        shiftRecord.orderQty = selectedMachine.orderQty
        shiftRecord.loadTime = selectedMachine.loadTime
        shiftRecord.unloadTime = selectedMachine.unloadTime
        shiftRecord.article = selectedMachine.article
        shiftRecord.heating = selectedMachine.heating
        shiftRecord.numCavity = selectedMachine.numCavity
        shiftRecord.heatingAct = selectedMachine.heatingAct
        shiftRecord.rawMaterial = selectedMachine.rawMaterial
        shiftRecord.totalMaterialUsed = selectedMachine.totalMaterialUsed
        shiftRecord.pigment = selectedMachine.pigment
        shiftRecord.totalMbUsed = selectedMachine.totalMbUsed


    }


    fun setAlternateRecord(shiftRecord: Record,alternateRecord: Record,){

        alternateRecord.name = shiftRecord.name
        alternateRecord.mould = shiftRecord.mould
        alternateRecord.productionWT = shiftRecord.productionWT
        alternateRecord.orderQty = shiftRecord.orderQty
        alternateRecord.loadTime = shiftRecord.loadTime
        alternateRecord.unloadTime = shiftRecord.unloadTime
        alternateRecord.article = shiftRecord.article
        alternateRecord.heating = shiftRecord.heating
        alternateRecord.numCavity = shiftRecord.numCavity
        alternateRecord.heatingAct = shiftRecord.heatingAct
        alternateRecord.rawMaterial = shiftRecord.rawMaterial
        alternateRecord.totalMaterialUsed = shiftRecord.totalMaterialUsed
        alternateRecord.pigment = shiftRecord.pigment
        alternateRecord.totalMbUsed = shiftRecord.totalMbUsed



    }

    fun sendUserDataToFirestore(record: Record, machine : Machine,  onComplete: (Boolean) -> Unit) {

        firestore.collection(Constants.RECORDS)
            .document(record.name+record.date+record.shift)
            .set(record)
            .addOnCompleteListener { task ->
                val isSuccess = task.isSuccessful
                onComplete(isSuccess)
            }
        firestore.collection(Constants.MACHINES)
            .document(machine.name)
            .set(machine)

    }
    fun retrieveUserName(userEmail: String): LiveData<String> {
        firestore.collection(Constants.OPERATORS).document(userEmail).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name")
                    userNameLiveData.value = name
                } else {
                    userNameLiveData.value = null
                }
            }
            .addOnFailureListener { exception ->
                userNameLiveData.value = null
                // Handle failure here
            }

        return userNameLiveData

}

    fun removeUserTypeFromSharedPref(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.remove(Constants.SAVED_USER_TYPE)
        editor.apply()
    }

}