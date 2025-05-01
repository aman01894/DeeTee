package com.app.deetee.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.deetee.R


data class ProcessList(val productHeading: String, val productName: String)

class ProcessAdapter(
    private val statuses: List<ProcessList>) : RecyclerView.Adapter<ProcessAdapter.StatusViewHolder>() {

    class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_productNumber: TextView = view.findViewById(R.id.tv_productNumber)
        val tv_productName: TextView = view.findViewById(R.id.tv_productName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_process, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val status = statuses[position]
        holder.tv_productNumber.text = "${status.productHeading}"
        holder.tv_productName.text = "${status.productName}"

        // Handle click on a status item
      /*  holder.itemView.setOnClickListener {
            onStatusClick(status)
        }*/
    }

    override fun getItemCount() = statuses.size
}

