package com.prplmnstr.technoplast.viewModel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.prplmnstr.technoplast.models.Operator
import com.prplmnstr.technoplast.utils.Constants


class AddUserFragmentViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    fun sendUserDataToFirestore(name: String, email: String, password: String,  onComplete: (Boolean) -> Unit) {
        val userData = Operator(name, email,password)
        firestore.collection(Constants.OPERATORS)
            .document(email)
            .set(userData)
            .addOnCompleteListener { task ->
                val isSuccess = task.isSuccessful
                onComplete(isSuccess)
            }

    }
    fun deleteOperatorByDocumentName(email: String,onComplete: (Boolean) -> Unit) {


        firestore.collection(Constants.OPERATORS).document(email)
            .delete()
            .addOnCompleteListener { task ->
                val isSuccess = task.isSuccessful
                onComplete(isSuccess)

            }
    }

    fun retrieveUserDataFromFirestore(): LiveData<List<Operator>> {
        val liveData = MutableLiveData<List<Operator>>()

        firestore.collection(Constants.OPERATORS).get().addOnSuccessListener { result ->

            val userList = mutableListOf<Operator>()
            for (document in result) {
                val user = document.toObject(Operator::class.java)
                userList.add(user)
            }
            liveData.value = userList
        }.addOnFailureListener { exception ->
            // Handle error
        }

        return liveData
    }

    fun isEmailExists(operatorList: List<Operator>, targetEmail: String?): Boolean {
        for (operator in operatorList) {
            if (operator.email.equals(targetEmail)) {
                return true // Email address exists in the list
            }
        }
        return false // Email address does not exist in the list
    }

    private fun signInAdmin(email: String, adminPassword: String, context: Context) {
        firebaseAuth.signInWithEmailAndPassword(email, adminPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Operator account created", Toast.LENGTH_SHORT).show()
                } else {

                }
            }
    }

    fun getAdminPassword(context: Context ) {
        val adminDocumentRef = firestore.collection("admin").document("password")

        adminDocumentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val password = documentSnapshot.getString("password")
                    if (password != null) {
                        signInAdmin("admin@gmail.com",password,context)
                    }
                } else {
                    // Document does not exist or there's an error
                }
            }
            .addOnFailureListener { exception ->
               // Handle failure scenario
            }
    }

    fun signUpUser(email: String, password: String, context: Context) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getAdminPassword(context)
                } else {
                    Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show()
                }
            }
    }

}