package com.example.fizjotherapy

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.fizjotherapy.boundry.GlobalUser
import com.example.fizjotherapy.control.AppointmentService
import com.example.fizjotherapy.control.UsersService
import com.example.fizjotherapy.databinding.FragmentAppointementRegisterBinding
import com.example.fizjotherapy.dto.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AppointmentRegisterFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentAppointementRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var usersService: UsersService
    private lateinit var appointmentService: AppointmentService
    private lateinit var doctors: List<User>

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextDateOfBirth: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextAppointmentDate: EditText
    private lateinit var docSpinner: Spinner
    private lateinit var registerButton: Button
    private lateinit var sameDataCheckBox: CheckBox

    private var savedYear: Int? = null
    private var savedMonth: Int? = null
    private var savedDay: Int? = null
    private var savedHour: Int? = null
    private var savedMinute: Int? = null

    private var savedAppointmentDate: String? = null
    private var selectedDocName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAppointementRegisterBinding.inflate(inflater, container, false)
        usersService = UsersService(requireContext())
        appointmentService = AppointmentService(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextFirstName = binding.editTextPatientName
        docSpinner = binding.editTextDocName
        editTextDateOfBirth = binding.editTextDateOfBirth
        editTextPhoneNumber = binding.editTextPhoneNumber
        editTextAppointmentDate = binding.editTextAppointmentDate
        sameDataCheckBox = binding.useSameData
        registerButton = binding.buttonRegister

        checkboxState()
        intDocsSpinner()
        openCalendarView()

        registerButton.setOnClickListener {
            appointmentService.create(
                editTextFirstName,
                doctors,
                selectedDocName,
                editTextPhoneNumber,
                editTextDateOfBirth,
                savedAppointmentDate)
        }
    }

    private fun checkboxState() {
        sameDataCheckBox.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    editTextFirstName.setText(GlobalUser.user.name)
                    editTextDateOfBirth.setText(GlobalUser.user.birthday.toString())
                    editTextPhoneNumber.setText(GlobalUser.user.phone.toString())
                }
            }
        }
    }

    private fun intDocsSpinner() {
        doctors = usersService.findAllDoctorNames()
        val doctorNames = doctors.map { doc -> doc.name }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, doctorNames)
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        docSpinner.adapter = arrayAdapter

        docSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedDocName = p0!!.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun openCalendarView() {
        editTextAppointmentDate.setOnClickListener {
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
        savedMonth = month + 1
        savedDay = dayOfMonth

        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale("pl"))
        val formattedDate = (savedYear.toString() + "-" + "%02d".format(savedMonth) + "-" + "%02d".format(savedDay) +
                " " + "%02d".format(hourOfDay) + ":" + "%02d".format(minute) + ":" + "01")

        savedAppointmentDate = LocalDateTime.parse(formattedDate, formatter).toString().replace("T", " ")
        editTextAppointmentDate.setText(savedAppointmentDate)
    }
}