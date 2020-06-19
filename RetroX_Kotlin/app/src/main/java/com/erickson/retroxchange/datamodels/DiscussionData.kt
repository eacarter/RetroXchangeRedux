package com.erickson.retroxchange.datamodels

data class DiscussionData(
    val id: String,
    val userId: String,
    val title: String,
    val text: String,
    val comments: Array<String>,
    val tags: Array<String>
)