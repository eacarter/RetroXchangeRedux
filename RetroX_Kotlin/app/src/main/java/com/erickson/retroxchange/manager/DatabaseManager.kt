package com.erickson.retroxchange.manager

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import java.util.*
import javax.inject.Inject

class DatabaseManager @Inject constructor(){

    private val data: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun initializeUser(firebaseAuth: FirebaseAuth, context: Context){
       val map = mutableMapOf(
           "id" to "",
           "username" to "",
           "firstname" to "",
           "lastname" to "",
           "city" to "",
           "state" to "",
           "about" to "",
           "profile_image" to "",
           "latitude" to 0.0,
           "longitude" to 0.0,
           "friends" to arrayListOf<String>(),
           "portfolio" to arrayListOf<String>()
       );

        data.collection("User")
            .document(firebaseAuth.currentUser!!.uid)
            .set(map, SetOptions.merge())
            .addOnSuccessListener{
                Toast.makeText(context, "Welcome to RetroXchange",
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something went wrong",
                    Toast.LENGTH_SHORT).show()
            }
    }
}