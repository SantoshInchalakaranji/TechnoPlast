package com.prplmnstr.technoplast.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.prplmnstr.technoplast.models.Machine
import com.prplmnstr.technoplast.models.Record
import com.prplmnstr.technoplast.utils.Constants

class MachineDetailsActivityViewModel:ViewModel() {


    private val firestore = FirebaseFirestore.getInstance()


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
    fun retrieveRecord(date: String, machineName: String, shift: String): LiveData<Record> {
        val liveData = MutableLiveData<Record>()

        val documentId = "$machineName$date$shift"

        firestore.collection(Constants.RECORDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val record = documentSnapshot.toObject(Record::class.java)
                    liveData.value = record
                } else {
                    liveData.value = Record() // Return an empty Record object
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
                liveData.value = Record() // Return an empty Record object in case of failure
            }

        return liveData
    }
    public override fun onCleared() {
        super.onCleared()

        // Perform any cleanup or final operations here
        // For example, releasing resources, stopping background tasks, etc.
    }
}