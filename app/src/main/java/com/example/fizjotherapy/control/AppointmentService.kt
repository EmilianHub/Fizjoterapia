package com.example.fizjotherapy.control

import android.app.Activity
import android.widget.EditText
import com.example.fizjotherapy.boundry.AppointmentRepository
import com.example.fizjotherapy.boundry.GlobalUser
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.dto.Wizyta
import com.example.fizjotherapy.prompt.PromptService
import java.time.LocalDate
import java.util.stream.Collectors

class AppointmentService(private val activity: Activity) {

    private var appointmentRepository = AppointmentRepository(activity)
    private var notificationService = NotificationService(activity)
    private var promptService = PromptService(activity)

    fun create(
        editTextFirstName: EditText,
        doctors: List<User>,
        selectedDocName: String?,
        editTextPhoneNumber: EditText,
        editTextDateOfBirth: EditText,
        savedAppointmentDate: String?
    ): Boolean {
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
            notificationService.create(wizyta)
        }
        return isCreated
    }

    private fun findDoctorByName(doctors: List<User>, selectedDocName: String): User? {
        return doctors.firstOrNull { doc -> doc.name.equals(selectedDocName, true) }
    }

    fun findAllUserAppointments(): List<Wizyta> {
        return appointmentRepository.findAllUserAppointments(GlobalUser.user)
    }

    fun findAllUserAppointmentsByAppointDate(): Map<LocalDate, MutableList<Wizyta>> {
        return appointmentRepository.findAllUserAppointments(GlobalUser.user).groupByTo(mutableMapOf()) { LocalDate.from(it.dataWizyty) }
    }

    fun delete(selectedAppointments: MutableSet<Wizyta>): Boolean {
        val isDeleted = appointmentRepository.delete(selectedAppointments.map {wizyta -> wizyta.id }.toCollection(
            mutableSetOf()
        ))
        if (isDeleted) {
            notificationService.create(selectedAppointments)
        }
        return isDeleted
    }
}