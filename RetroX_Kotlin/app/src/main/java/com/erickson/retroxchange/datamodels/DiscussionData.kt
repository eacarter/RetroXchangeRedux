package com.erickson.retroxchange.datamodels

data class DiscussionData(
    val id: String= "",
    val userId: String = "",
    val userName: String = "",
    val title: String = "",
    val message: String = "",
    val messageImage: String = "",
    val timeStamp: String = "",
    val comments: ArrayList<String> = arrayListOf(),
    val tags: ArrayList<String> = arrayListOf()
)