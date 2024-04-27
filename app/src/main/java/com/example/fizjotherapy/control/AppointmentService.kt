package com.example.fizjotherapy.control

import android.content.Context
import android.widget.EditText
import com.example.fizjotherapy.boundry.AppointmentRepository
import com.example.fizjotherapy.boundry.GlobalUser
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.dto.Wizyta
import com.example.fizjotherapy.prompt.PromptService
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

class AppointmentService(val context: Context) {

    private var appointmentRepository = AppointmentRepository(context)
    private var promptService = PromptService(context)

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

        val isCreated = appointmentRepository.create(wizyta)
        if (isCreated) {
            promptService.showSuccessToast("Wizyta została umówiona")
        } else {
            promptService.showFailToast("Coś poszło nie tak spróbuj ponownie")
        }
    }

    private fun findDoctorByName(doctors: List<User>, selectedDocName: String): User? {
        return doctors.firstOrNull { doc -> doc.name.equals(selectedDocName, true) }
    }

    fun findAllUserAppointments(): List<Wizyta> {
        return appointmentRepository.findAllUserAppointments(GlobalUser.user)
    }

    fun findAllUserAppointmentsByAppointDate(): Map<LocalDate, List<Wizyta>> {
        return appointmentRepository.findAllUserAppointments(GlobalUser.user).groupBy { it -> LocalDate.from(it.dataWizyty) }
    }
}