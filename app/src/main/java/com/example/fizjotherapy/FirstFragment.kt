package com.example.fizjotherapy

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.fizjotherapy.databinding.FragmentFirstBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var editTextFirstName: EditText? = null
    private var editTextLastName: EditText? = null
    private var editTextDateOfBirth: EditText? = null
    private var editTextPhoneNumber: EditText? = null
    private var editTextAppointmentDate: EditText? = null

    private var savedYear: Int? = null
    private var savedMonth: Int? = null
    private var savedDay: Int? = null
    private var savedHour: Int? = null
    private var savedMinute: Int? = null

    private var savedAppointmentDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextFirstName = binding.editTextFirstName
        editTextLastName = binding.editTextLastName
        editTextDateOfBirth = binding.editTextDateOfBirth
        editTextPhoneNumber = binding.editTextPhoneNumber
        editTextAppointmentDate = binding.editTextAppointmentDate

        openCalendarView()

    }

    private fun openCalendarView() {
        editTextAppointmentDate?.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                this,
                year,
                month,
                day
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        savedYear = year
        savedMonth = month
        savedDay = dayOfMonth

        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val inputFormat = DateTimeFormatter.ofPattern("dd-M-yyyy H:mm", Locale("pl"))
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale("pl"))
        savedHour = hourOfDay
        savedMinute = minute

        val date = LocalDateTime.parse(
            "$savedDay-$savedMonth-$savedYear $savedHour:$savedMinute",
            inputFormat
        )
        savedAppointmentDate = formatter.format(date).toString()
        editTextAppointmentDate?.setText(savedAppointmentDate)
    }
}