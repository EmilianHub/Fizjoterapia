package com.example.fizjotherapy.control

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fizjotherapy.R
import com.example.fizjotherapy.dto.Wizyta

class AppointmentRecyclerAdapter(private val itemList: List<Wizyta>) : RecyclerView.Adapter<AppointmentRecyclerAdapter.AppointViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appiontment_info_table, parent, false)
        return AppointViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AppointViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.docName.text = currentItem.lekarz.name
        val dataWizyty = currentItem.dataWizyty
        holder.appointHour.text =
            String.format("%s:%s", "%02d".format(dataWizyty.hour), "%02d".format(dataWizyty.minute))
    }

    class AppointViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val docName: TextView = itemView.findViewById(R.id.doc_name_row)
        val appointHour: TextView = itemView.findViewById(R.id.hour_row)
    }

}