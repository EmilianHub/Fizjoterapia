package com.example.fizjotherapy

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fizjotherapy.control.AppointmentRecyclerAdapter
import com.example.fizjotherapy.control.AppointmentService
import com.example.fizjotherapy.databinding.FragmentAppointmentsCalendarBinding
import com.example.fizjotherapy.dto.Wizyta
import com.google.android.material.datepicker.DayViewDecorator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AppointmentsCalendarFragment : Fragment() {

    private var _binding: FragmentAppointmentsCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var wizytaService: AppointmentService

    private lateinit var recyclerView: RecyclerView
    private lateinit var allUserAppointmentsByAppDate: Map<LocalDate, List<Wizyta>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAppointmentsCalendarBinding.inflate(inflater, container, false)
        wizytaService = AppointmentService(requireContext())
        allUserAppointmentsByAppDate = getAllUserAppointments()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.appointmentRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        binding.calendarView.setOnDateChangeListener{_, year, month, day ->
            val date = (year.toString() + "-" + "%02d".format(month+1) + "-" + "%02d".format(day))
            viewAppointmentOnSelectedDate(date)
        }

        viewAppointmentOnSelectedDate(LocalDate.now())
    }

    private fun getAllUserAppointments(): Map<LocalDate, List<Wizyta>> {
        return wizytaService.findAllUserAppointmentsByAppointDate()
    }

    private fun <T> viewAppointmentOnSelectedDate(selectedDate: T) {
        val date: LocalDate
        if (selectedDate is String) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("pl"))
            date = LocalDate.from(formatter.parse(selectedDate))
        } else {
            date = selectedDate as LocalDate
        }
        val appointInDate = allUserAppointmentsByAppDate[date].orEmpty()
        recyclerView.adapter = AppointmentRecyclerAdapter(appointInDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}