package com.example.fizjotherapy.boundry

import android.content.ContentValues
import android.content.Context
import com.example.fizjotherapy.dto.LifeCycleState
import com.example.fizjotherapy.dto.Notification
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.dto.Wizyta
import com.example.fizjotherapy.mapper.NotificationMapper
import java.lang.StringBuilder

class NotificationRepository(private val context: Context) {

    companion object {
        const val TABLE_NAME = "notifications"
        const val COLUMN_ID = "id"
        const val COLUMN_APPOINTMENT_ID = "appointment_id"
        const val COLUMN_RECEIVER_ID = "user_receiver_id"
        const val COLUMN_LIFE_CYCLE_STATE = "lifecyclestate"
    }

    fun create(appointment: Wizyta): Boolean {
        val db = DbHelper(context).writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_APPOINTMENT_ID, appointment.id)
            put(COLUMN_RECEIVER_ID, appointment.lekarz!!.id)
            put(COLUMN_LIFE_CYCLE_STATE, LifeCycleState.ACTIVE.state)
        }
        db.beginTransaction()
        val result = db.insert(TABLE_NAME, null, values)
        db.setTransactionSuccessful()
        db.endTransaction()
        return result > 0
    }

    fun create(appointments: Set<Wizyta>): Boolean {
        val db = DbHelper(context).writableDatabase
        var result: Long = 0
        db.beginTransaction()
        ContentValues().apply {
            appointments.forEach { appointment ->
                put(COLUMN_APPOINTMENT_ID, appointment.id)
                put(COLUMN_RECEIVER_ID, appointment.pacjent!!.id)
                put(COLUMN_LIFE_CYCLE_STATE, LifeCycleState.ACTIVE.state)
                result = db.insert(TABLE_NAME, null, this)
            }
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        return result > 0
    }

    fun update(user: User) {
        val db = DbHelper(context).writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LIFE_CYCLE_STATE, LifeCycleState.CANCELED.state)
        }
        db.beginTransaction()
        db.update(TABLE_NAME, values, "${COLUMN_RECEIVER_ID}=?", arrayOf(user.id.toString()))
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun findByUser(user: User): List<Notification> {
        val db = DbHelper(context).readableDatabase
        val QUERY = "SELECT ${createJoinString()} FROM ${TABLE_NAME} as nt " +
                "INNER JOIN ${AppointmentRepository.TABLE_NAME} as ap ON ap.${AppointmentRepository.COLUMN_ID} = nt.$COLUMN_APPOINTMENT_ID " +
                "WHERE nt.${COLUMN_RECEIVER_ID}=${user.id} AND nt.${COLUMN_LIFE_CYCLE_STATE}='${LifeCycleState.ACTIVE.state}'"
        val result = db.rawQuery(QUERY, null)
        val notificationList = mutableListOf<Notification>()
        while (result.moveToNext()) {
            notificationList.add(NotificationMapper.toDTO(result))
        }
        result.close()
        return notificationList
    }

    private fun createJoinString(): String {
        return StringBuilder()
            .append("nt.$COLUMN_ID as 'nt$COLUMN_ID', ")
            .append("nt.$COLUMN_RECEIVER_ID as 'nt$COLUMN_RECEIVER_ID', ")
            .append("nt.$COLUMN_APPOINTMENT_ID as 'nt$COLUMN_APPOINTMENT_ID', ")
            .append("nt.$COLUMN_LIFE_CYCLE_STATE as 'nt$COLUMN_LIFE_CYCLE_STATE', ")
            .append("ap.${AppointmentRepository.COLUMN_ID} as 'ap${AppointmentRepository.COLUMN_ID}', ")
            .append("ap.${AppointmentRepository.COLUMN_PATIENT} as 'ap${AppointmentRepository.COLUMN_PATIENT}', ")
            .append("ap.${AppointmentRepository.COLUMN_PATIENT_NAME} as 'ap${AppointmentRepository.COLUMN_PATIENT_NAME}', ")
            .append("ap.${AppointmentRepository.COLUMN_DOCTOR} as 'ap${AppointmentRepository.COLUMN_DOCTOR}', ")
            .append("ap.${AppointmentRepository.COLUMN_DATE} as 'ap${AppointmentRepository.COLUMN_DATE}', ")
            .append("ap.${AppointmentRepository.COLUMN_PHONE_NUMBER} as 'ap${AppointmentRepository.COLUMN_PHONE_NUMBER}', ")
            .append("ap.${AppointmentRepository.COLUMN_BIRTHDATE} as 'ap${AppointmentRepository.COLUMN_BIRTHDATE}', ")
            .append("ap.${AppointmentRepository.COLUMN_LIFECYCLESTATE} as 'ap${AppointmentRepository.COLUMN_LIFECYCLESTATE}' ")
            .toString()
    }

}