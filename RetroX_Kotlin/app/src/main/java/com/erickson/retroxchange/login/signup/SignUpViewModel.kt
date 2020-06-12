package com.erickson.retroxchange.login.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.erickson.retroxchange.manager.UserManager
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {

    fun getUserInfo() : LiveData<FirebaseUser>?{
        return userManager.getUserData()
    }

    fun createAccount(email: String, pass: String, context: Context){
        userManager.createAccount(email, pass, context)
    }

}
