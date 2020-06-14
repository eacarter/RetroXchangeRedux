package com.erickson.retroxchange.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickson.retroxchange.manager.UserManager
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val userManager: UserManager): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun signOut(){
        userManager.signOut()
    }
}