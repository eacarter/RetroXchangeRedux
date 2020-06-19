package com.erickson.retroxchange.manager

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class UserManager @Inject constructor(var databaseManager: DatabaseManager){
    private val user = MediatorLiveData<FirebaseUser>()

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun getUserData(): LiveData<FirebaseUser>?{
        user.value = auth.currentUser
        return user
    }

    fun signOut(){
        auth.signOut()
        user.value = null
    }

    fun createAccount(email: String, password:String, context: Context){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(Activity()) { task ->
                if (task.isSuccessful && task.isComplete) {
                    user.postValue(auth.currentUser)
                    databaseManager.initializeUser(auth, context)
                } else {
                    Log.d("UserManager", "failed" )
                    Toast.makeText(context, task.exception.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun loginAccount(email: String, password: String, context: Context){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(Activity()) { task ->
                if (task.isSuccessful && task.isComplete) {
                    user.postValue(auth.currentUser)
                } else {
                    Log.d("UserManager", "failed" )
                    Toast.makeText(context, task.exception.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}