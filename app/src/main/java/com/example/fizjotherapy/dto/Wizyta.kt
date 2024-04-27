package com.example.fizjotherapy.dto

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Wizyta (
    var id: Int?,
    var pacjent: User,
    var imiePacjenta: String,
    var lekarz: User,
    var dataWizyty: LocalDateTime,
    var numerTelefonu: Int,
    var dataUrodzenia: String,
    var lifeCycleState: LifeCycleState
) {
    companion object {
        fun toLocalDateTime(time: String): LocalDateTime {
            val timeFormatted = time.replace("T", " ")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale("pl"))
            return LocalDateTime.parse(timeFormatted, formatter)
        }
    }

    constructor(id: Int?,
                pacjent: User,
                imiePacjenta: String,
                lekarz: User,
                dataWizyty: String,
                numerTelefonu: Int,
                dataUrodzenia: String)
            : this(id, pacjent, imiePacjenta, lekarz, toLocalDateTime(dataWizyty), numerTelefonu, dataUrodzenia, LifeCycleState.ACTIVE)

    constructor(id: Int?,
                pacjent: User,
                imiePacjenta: String,
                lekarz: User,
                dataWizyty: String,
                numerTelefonu: Int,
                dataUrodzenia: String,
                lifeCycleState: LifeCycleState)
            : this(id, pacjent, imiePacjenta, lekarz, toLocalDateTime(dataWizyty), numerTelefonu, dataUrodzenia, lifeCycleState)

    constructor(id: Int?,
                pacjent: User,
                imiePacjenta: String,
                lekarz: User,
                dataWizyty: String,
                numerTelefonu: Int,
                dataUrodzenia: String,
                lifeCycleState: String)
            : this(id, pacjent, imiePacjenta, lekarz, toLocalDateTime(dataWizyty), numerTelefonu, dataUrodzenia, LifeCycleState.valueOf(lifeCycleState))
}