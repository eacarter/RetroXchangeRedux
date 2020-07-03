package com.erickson.retroxchange.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickson.retroxchange.datamodels.DiscussionData
import com.erickson.retroxchange.manager.DatabaseManager
import javax.inject.Inject

class DashboardViewModel @Inject constructor(val databaseManager: DatabaseManager): ViewModel() {

    fun getDiscussionFeed() : LiveData<ArrayList<DiscussionData>>{
       return databaseManager.getDiscussionfeed()
    }


}