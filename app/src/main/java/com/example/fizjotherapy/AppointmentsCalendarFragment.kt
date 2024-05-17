package com.example.fizjotherapy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fizjotherapy.boundry.GlobalUser
import com.example.fizjotherapy.control.AppointmentRecyclerAdapter
import com.example.fizjotherapy.control.AppointmentService
import com.example.fizjotherapy.databinding.FragmentAppointmentsCalendarBinding
import com.example.fizjotherapy.dto.Wizyta
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AppointmentsCalendarFragment : Fragment() {

    private var _binding: FragmentAppointmentsCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var appointmentService: AppointmentService

    private lateinit var recyclerView: RecyclerView
    private lateinit var allUserAppointmentsByAppDate: Map<LocalDate, MutableList<Wizyta>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAppointmentsCalendarBinding.inflate(inflater, container, false)
        appointmentService = AppointmentService(requireActivity())
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

        val startUpDate = arguments?.getSerializable("date", LocalDateTime::class.java) ?: LocalDate.now()
        viewAppointmentOnSelectedDate(startUpDate)
    }

    private fun getAllUserAppointments(): Map<LocalDate, MutableList<Wizyta>> {
        return appointmentService.findAllUserAppointmentsByAppointDate()
    }

    private fun <T> viewAppointmentOnSelectedDate(selectedDate: T) {
        val date: LocalDate = if (selectedDate is String) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("pl"))
            LocalDate.from(formatter.parse(selectedDate))
        } else if (selectedDate is LocalDateTime) {
            selectedDate.toLocalDate()
        } else {
            selectedDate as LocalDate
        }
        binding.calendarView.date = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        recyclerView.adapter = AppointmentRecyclerAdapter(allUserAppointmentsByAppDate[date] ?: mutableListOf(), requireActivity().findViewById(R.id.toolbar), appointmentService, GlobalUser.user.rola)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}