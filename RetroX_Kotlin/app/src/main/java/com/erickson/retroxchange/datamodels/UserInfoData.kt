package com.erickson.retroxchange.datamodels

data class UserInfoData(
    val id: String = "",
    val username: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val city: String = "",
    val portfolio: ArrayList<ItemData> = arrayListOf(),
    val friends: ArrayList<UserInfoData> = arrayListOf(),
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val about: String = "",
    val state: String = "",
    val profile_image: String = ""
)