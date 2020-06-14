package com.erickson.retroxchange.manager

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.erickson.retroxchange.datamodels.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import javax.inject.Inject

class DatabaseManager @Inject constructor(){

    private val data: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val storage: FirebaseStorage by lazy{
        FirebaseStorage.getInstance()
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

    fun getUser(id : String) : LiveData<UserData>{
        val user = MediatorLiveData<UserData>()
        data.collection("User").document(id).get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot != null){
                    user.postValue(documentSnapshot.toObject(UserData::class.java))
                }
            }
        return user
    }

    fun updateUser(user: FirebaseUser): DocumentReference{
        val userId = data.collection("Users").document(user.uid)
        return userId
    }

    fun uploadImage(file: Uri, user: FirebaseUser){
        if(file != null){
            val image = storage.getReference().child("profile_images/" + user.getUid())
            image.putFile(file).addOnCompleteListener {
                if (it.isSuccessful){
                    val image = storage.getReference().child("profile_images/" + user.getUid())
                    image.downloadUrl.addOnSuccessListener{
                        updateUser(user).update("profile_image", it.toString())
                    }
                }
            }
        }
    }
}