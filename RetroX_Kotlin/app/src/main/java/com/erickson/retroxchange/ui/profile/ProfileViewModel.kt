package com.erickson.retroxchange.ui.profile

import android.net.Uri
import android.service.autofill.UserData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.erickson.retroxchange.datamodels.DiscussionData
import com.erickson.retroxchange.datamodels.UserInfoData
import com.erickson.retroxchange.manager.DatabaseManager
import com.erickson.retroxchange.manager.UserManager
import com.google.firebase.auth.FirebaseUser
import java.io.File
import java.net.URI
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val userManager: UserManager, private val databaseManager: DatabaseManager): ViewModel(){

    fun userID(): LiveData<FirebaseUser>{
        return userManager.getUserData()
    }

    fun userProfileImageUpload(uri: Uri, id: FirebaseUser){
        databaseManager.uploadProfileImage(uri, id)
    }

    fun getUserData(id: String): LiveData<UserInfoData>{
       return databaseManager.getUser(id)
    }

    fun getUserDiscussionFeed(id: String) : LiveData<ArrayList<DiscussionData>>{
        return databaseManager.getUserDiscussionfeed(id)
    }

}