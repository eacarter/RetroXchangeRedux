package com.erickson.retroxchange.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.erickson.retroxchange.manager.UserManager
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userManager: UserManager ): ViewModel(){

    fun getUserInfo(): LiveData<FirebaseUser>?{
        return userManager.getUserData()
    }

}