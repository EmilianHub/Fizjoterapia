package com.example.fizjotherapy.boundry

import android.content.ContentValues
import android.content.Context
import com.example.fizjotherapy.dto.Wizyta

class AppointmentRepository(val context: Context) {
    companion object {
        const val TABLE_NAME = "appointments"
        const val COLUMN_ID = "id"
        const val COLUMN_PATIENT = "patient"
        const val COLUMN_PATIENT_NAME = "patient_name"
        const val COLUMN_DOCTOR = "doctor"
        const val COLUMN_DATE = "date"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_BIRTHDATE = "birth_date"
    }

    fun create(wizyta: Wizyta): Boolean {
        val db = DbHelper(context).writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PATIENT, wizyta.pacjent.id)
            put(COLUMN_PATIENT_NAME, wizyta.imiePacjenta)
            put(COLUMN_DOCTOR, wizyta.lekarz.id)
            put(COLUMN_DATE, wizyta.dataWizyty)
            put(COLUMN_PHONE_NUMBER, wizyta.numerTelefonu)
            put(COLUMN_BIRTHDATE, wizyta.dataUrodzenia)
        }
        db.beginTransaction()
        db.insert(TABLE_NAME, null, values)
        db.setTransactionSuccessful()
        db.endTransaction()
        return true
    }
}