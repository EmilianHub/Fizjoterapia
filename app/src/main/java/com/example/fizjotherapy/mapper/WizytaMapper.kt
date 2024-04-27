package com.example.fizjotherapy.mapper

import android.database.Cursor
import com.example.fizjotherapy.dto.LifeCycleState
import com.example.fizjotherapy.dto.Wizyta

class WizytaMapper {

    companion object {
        const val COLUMN_ID = "apid"
        const val COLUMN_PATIENT = "appatient"
        const val COLUMN_PATIENT_NAME = "appatient_name"
        const val COLUMN_DOCTOR = "apdoctor"
        const val COLUMN_DATE = "apdate"
        const val COLUMN_PHONE_NUMBER = "apphone_number"
        const val COLUMN_BIRTHDATE = "apbirth_date"
        const val COLUMN_LIFECYCLESTATE = "aplifecyclestate"

        fun toDTO(result: Cursor): Wizyta {
            return Wizyta(
                result.getInt(result.getColumnIndexOrThrow(COLUMN_ID)),
                UserMapper.toPatientDTO(result),
                result.getString(result.getColumnIndexOrThrow(COLUMN_PATIENT_NAME)),
                UserMapper.toDocDTO(result),
                result.getString(result.getColumnIndexOrThrow(COLUMN_DATE)),
                result.getInt(result.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_BIRTHDATE)),
                LifeCycleState.getByString(result.getString(result.getColumnIndexOrThrow(COLUMN_LIFECYCLESTATE)))
            )
        }
    }
}