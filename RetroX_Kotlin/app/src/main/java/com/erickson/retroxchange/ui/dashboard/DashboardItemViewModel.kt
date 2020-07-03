package com.erickson.retroxchange.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.erickson.retroxchange.datamodels.DiscussionCommentData
import com.erickson.retroxchange.datamodels.DiscussionData
import com.erickson.retroxchange.datamodels.UserInfoData
import com.erickson.retroxchange.manager.DatabaseManager
import com.erickson.retroxchange.manager.UserManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import javax.inject.Inject

class DashboardItemViewModel @Inject constructor(val databaseManager: DatabaseManager, val userManager: UserManager): ViewModel() {

    fun getUserData(): LiveData<FirebaseUser>{
        return userManager.getUserData()
    }

    fun getUserInfo(id: String): LiveData<UserInfoData>{
        return databaseManager.getUser(id)
    }

    fun getDiscussionComments(id: String): LiveData<ArrayList<DiscussionCommentData>>{
        return databaseManager.getDiscussionfeedComments(id)
    }

    fun uploadDiscussionComments(id: String, item: DiscussionCommentData, postId: String){
        databaseManager.uploadDiscussionComment(id, postId, item)
    }

    fun getDiscussionSpecific(id:String): LiveData<DiscussionData>{
        return databaseManager.getDiscussionSpecific(id)
    }
}
