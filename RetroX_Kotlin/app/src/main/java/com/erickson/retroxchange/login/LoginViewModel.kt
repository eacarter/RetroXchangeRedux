package com.erickson.retroxchange.login

import androidx.lifecycle.ViewModel
import com.erickson.retroxchange.manager.UserManager
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userManager: UserManager ): ViewModel(){

    fun userInfo(): FirebaseUser?{
        return userManager.getUserData()?.value
    }

}