package com.erickson.retroxchange.datamodels

data class DiscussionCommentData(
    val id: String= "",
    val userId: String = "",
    val userName: String = "",
    val text: String = "",
    val timeStamp: String = "",
    val starredUsers: ArrayList<String> = arrayListOf()
)