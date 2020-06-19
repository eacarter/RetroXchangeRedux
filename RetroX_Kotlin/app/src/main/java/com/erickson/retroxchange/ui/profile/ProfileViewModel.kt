package com.erickson.retroxchange.ui.profile

import androidx.lifecycle.ViewModel
import com.erickson.retroxchange.manager.DatabaseManager
import com.erickson.retroxchange.manager.UserManager
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val userManager: UserManager, private val databaseManager: DatabaseManager): ViewModel(){

    fun userInfo(): FirebaseUser?{
        return userManager.getUserData()?.value
    }

}