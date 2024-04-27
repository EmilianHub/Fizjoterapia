package com.example.fizjotherapy.boundry

import android.content.ContentValues
import android.content.Context
import com.example.fizjotherapy.dto.LifeCycleState
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.dto.Wizyta
import com.example.fizjotherapy.mapper.WizytaMapper
import java.lang.StringBuilder
import kotlin.reflect.full.companionObject

class AppointmentRepository(val context: Context) {

    companion object Columns {
        const val TABLE_NAME = "appointments"
        const val COLUMN_ID = "id"
        const val COLUMN_PATIENT = "patient"
        const val COLUMN_PATIENT_NAME = "patient_name"
        const val COLUMN_DOCTOR = "doctor"
        const val COLUMN_DATE = "date"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_BIRTHDATE = "birth_date"
        const val COLUMN_LIFECYCLESTATE = "lifecyclestate"
    }

    fun create(wizyta: Wizyta): Boolean {
        val db = DbHelper(context).writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PATIENT, wizyta.pacjent.id)
            put(COLUMN_PATIENT_NAME, wizyta.imiePacjenta)
            put(COLUMN_DOCTOR, wizyta.lekarz.id)
            put(COLUMN_DATE, wizyta.dataWizyty.toString())
            put(COLUMN_PHONE_NUMBER, wizyta.numerTelefonu)
            put(COLUMN_BIRTHDATE, wizyta.dataUrodzenia)
            put(COLUMN_LIFECYCLESTATE, LifeCycleState.ACTIVE.state)
        }
        db.beginTransaction()
        db.insert(TABLE_NAME, null, values)
        db.setTransactionSuccessful()
        db.endTransaction()
        return true
    }

    fun findAllUserAppointments(user: User): List<Wizyta> {
        val db = DbHelper(context).readableDatabase
        val QUERY = "SELECT ${createJoinSelectQuery()} FROM $TABLE_NAME as ap " +
                "INNER JOIN ${UsersTableRepository.TABLE_NAME} as pa on ap${COLUMN_PATIENT} = pa.${UsersTableRepository.COLUMN_ID} " +
                "INNER JOIN ${UsersTableRepository.TABLE_NAME} as doc on ap${COLUMN_DOCTOR} = doc.${UsersTableRepository.COLUMN_ID} " +
                "WHERE ap$COLUMN_PATIENT = '${user.id}' and ap$COLUMN_LIFECYCLESTATE = '${LifeCycleState.ACTIVE.state}'"
        val result = db.rawQuery(QUERY, null)
        val appointments = mutableListOf<Wizyta>()
        while (result.moveToNext()) {
            appointments.add(WizytaMapper.toDTO(result))
        }
        return appointments
    }

    private fun createJoinSelectQuery(): String {
        return StringBuilder()
            .append("ap.$COLUMN_ID as 'ap$COLUMN_ID', ")
            .append("ap.$COLUMN_PATIENT as 'ap$COLUMN_PATIENT', ")
            .append("ap.$COLUMN_PATIENT_NAME as 'ap$COLUMN_PATIENT_NAME', ")
            .append("ap.$COLUMN_DOCTOR as 'ap$COLUMN_DOCTOR', ")
            .append("ap.$COLUMN_DATE as 'ap$COLUMN_DATE', ")
            .append("ap.$COLUMN_PHONE_NUMBER as 'ap$COLUMN_PHONE_NUMBER', ")
            .append("ap.$COLUMN_BIRTHDATE as 'ap$COLUMN_BIRTHDATE', ")
            .append("ap.$COLUMN_LIFECYCLESTATE as 'ap$COLUMN_LIFECYCLESTATE', ")
            .append("pa.${UsersTableRepository.COLUMN_ID} as 'pa${UsersTableRepository.COLUMN_ID}', ")
            .append("pa.${UsersTableRepository.COLUMN_EMAIL} as 'pa${UsersTableRepository.COLUMN_EMAIL}', ")
            .append("pa.${UsersTableRepository.COLUMN_NAME} as 'pa${UsersTableRepository.COLUMN_NAME}', ")
            .append("pa.${UsersTableRepository.COLUMN_USERNAME} as 'pa${UsersTableRepository.COLUMN_USERNAME}', ")
            .append("pa.${UsersTableRepository.COLUMN_PASSWORD} as 'pa${UsersTableRepository.COLUMN_PASSWORD}', ")
            .append("pa.${UsersTableRepository.COLUMN_PHONE_NUMBER} as 'pa${UsersTableRepository.COLUMN_PHONE_NUMBER}', ")
            .append("pa.${UsersTableRepository.COLUMN_BIRTHDAY} as 'pa${UsersTableRepository.COLUMN_BIRTHDAY}', ")
            .append("pa.${UsersTableRepository.COLUMN_ROLE} as 'pa${UsersTableRepository.COLUMN_ROLE}', ")
            .append("doc.${UsersTableRepository.COLUMN_ID} as 'doc${UsersTableRepository.COLUMN_ID}', ")
            .append("doc.${UsersTableRepository.COLUMN_EMAIL} as 'doc${UsersTableRepository.COLUMN_EMAIL}', ")
            .append("doc.${UsersTableRepository.COLUMN_NAME} as 'doc${UsersTableRepository.COLUMN_NAME}', ")
            .append("doc.${UsersTableRepository.COLUMN_USERNAME} as 'doc${UsersTableRepository.COLUMN_USERNAME}', ")
            .append("doc.${UsersTableRepository.COLUMN_PASSWORD} as 'doc${UsersTableRepository.COLUMN_PASSWORD}', ")
            .append("doc.${UsersTableRepository.COLUMN_PHONE_NUMBER} as 'doc${UsersTableRepository.COLUMN_PHONE_NUMBER}', ")
            .append("doc.${UsersTableRepository.COLUMN_BIRTHDAY} as 'doc${UsersTableRepository.COLUMN_BIRTHDAY}', ")
            .append("doc.${UsersTableRepository.COLUMN_ROLE} as 'doc${UsersTableRepository.COLUMN_ROLE}' ")
            .toString()
    }
}