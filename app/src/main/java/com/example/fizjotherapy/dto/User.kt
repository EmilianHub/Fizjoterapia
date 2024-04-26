package com.example.fizjotherapy.dto

data class User(
    val id: Int?,
    val name: String,
    val username: String,
    val password: String?,
    val email: String?,
    val phone: Int?,
    val birthday: String,
    val rola: String?
)
