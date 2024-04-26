package com.example.fizjotherapy.dto

data class Wizyta(
    val id: Int?,
    val pacjent: User,
    val imiePacjenta: String,
    val lekarz: User,
    val dataWizyty: String,
    val numerTelefonu: Int,
    val dataUrodzenia: String
)