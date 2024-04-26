package com.example.fizjotherapy.control

import android.content.Context
import android.widget.EditText
import com.example.fizjotherapy.boundry.AppointmentRepository
import com.example.fizjotherapy.boundry.GlobalUser
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.dto.Wizyta

class AppointmentService(val context: Context) {

    private var appointmentRepository = AppointmentRepository(context)

    fun create(
        editTextFirstName: EditText,
        doctors: List<User>,
        selectedDocName: String?,
        editTextPhoneNumber: EditText,
        editTextDateOfBirth: EditText,
        savedAppointmentDate: String?
    ) {
        val wizyta = Wizyta(
            null,
            GlobalUser.user,
            editTextFirstName.text.toString(),
            findDoctorByName(doctors, selectedDocName!!)!!,
            savedAppointmentDate!!,
            editTextPhoneNumber.text.toString().toInt(),
            editTextDateOfBirth.text.toString()
        )

        appointmentRepository.create(wizyta)
    }

    private fun findDoctorByName(doctors: List<User>, selectedDocName: String): User? {
        return doctors.filter { doc -> doc.name.equals(selectedDocName, true) }
            .firstOrNull()
    }
}