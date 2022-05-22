package com.example.myapplication.dataclass

data class User (
    val id : String = "",
    val phone_number : String = "",
    val username : String = "",
    val email : String = "",
    val user_image : String? = null,
    val user_alias : String? = null
)