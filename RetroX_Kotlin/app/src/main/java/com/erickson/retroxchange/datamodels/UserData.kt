package com.erickson.retroxchange.datamodels

data class UserData(
    val id: String,
    val username: String,
    val firstname: String,
    val lastname: String,
    val city: String,
    val portfolio: Array<ItemData>,
    val friends: Array<UserData>,
    val longitude: Double,
    val latitude: Double,
    val about: String,
    val state: String,
    val profile_image: String
)