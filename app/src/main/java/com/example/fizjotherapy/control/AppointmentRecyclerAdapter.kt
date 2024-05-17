package com.example.fizjotherapy.control

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.fizjotherapy.R
import com.example.fizjotherapy.dto.Rola
import com.example.fizjotherapy.dto.Wizyta

class AppointmentRecyclerAdapter(
    private val appointmentsList: MutableList<Wizyta>,
    private val toolbar: Toolbar,
    private val appointmentService: AppointmentService,
    private val userRole : Rola
) : RecyclerView.Adapter<AppointmentRecyclerAdapter.AppointViewHolder>() {

    private var selectedAppointments =  mutableSetOf<Wizyta>()
    private var selectedItemsPosition = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.appiontment_info_table, parent, false)
        return AppointViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    override fun onBindViewHolder(holder: AppointViewHolder, position: Int) {
        val currentItem = appointmentsList[position]
        holder.nameRow.text = if (userRole == Rola.USER) currentItem.lekarz!!.name else currentItem.imiePacjenta
        val dataWizyty = currentItem.dataWizyty
        holder.appointHour.text =
            String.format("%s:%s", "%02d".format(dataWizyty.hour), "%02d".format(dataWizyty.minute))

        holder.itemView.setOnLongClickListener {
            onItemClick(holder)
            changeMenuItemDisplay(selectedAppointments.isNotEmpty())
            true
        }

        toolbar.setOnMenuItemClickListener {
            toolbarContextAction()
            true
        }
    }

    private fun onItemClick(holder: AppointViewHolder) {
        val selectedAppointment = appointmentsList[holder.adapterPosition]
        if (!selectedAppointments.contains(selectedAppointment)) {
            holder.row.setBackgroundColor(Color.GRAY)
            selectedAppointments.add(selectedAppointment)
            selectedItemsPosition.add(holder.adapterPosition)
        } else {
            holder.row.setBackgroundColor(Color.WHITE)
            selectedAppointments.remove(selectedAppointment)
            selectedItemsPosition.remove(holder.adapterPosition)
        }
    }

    private fun changeMenuItemDisplay(isVisible: Boolean) {
        toolbar.menu.findItem(R.id.menu_delete).setVisible(isVisible)
    }

    private fun toolbarContextAction() {
        appointmentService.delete(selectedAppointments)
        appointmentsList.removeAll(selectedAppointments)
        changeMenuItemDisplay(false)
        selectedAppointments.clear()
        selectedItemsPosition.forEach { position ->
            notifyItemRemoved(position)
        }
    }

    class AppointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val row: TableRow = itemView.findViewById(R.id.appointmentRow)
        val nameRow: TextView = itemView.findViewById(R.id.doc_name_row)
        val appointHour: TextView = itemView.findViewById(R.id.hour_row)
    }

}