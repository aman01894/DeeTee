package com.app.deetee.model

data class LoginResponse(
    val message: String,
    val data: UserData,
    val token: String
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String
)