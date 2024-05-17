package com.example.fizjotherapy.dto

data class User(
    val id: Int?,
    val name: String,
    val username: String,
    var password: String,
    val email: String,
    val phone: Int?,
    val birthday: String,
    val rola: Rola
) {
    constructor(name: String,
                username: String,
                password: String,
                email: String,
                phone: Int?,
                birthday: String,
                rola: String) : this(null, name, username, password, email, phone, birthday, Rola.getByString(rola))

    constructor(id: Int,
                name: String,
                username: String,
                password: String,
                email: String,
                phone: Int?,
                birthday: String,
                rola: String) : this(id, name, username, password, email, phone, birthday, Rola.getByString(rola))
}
