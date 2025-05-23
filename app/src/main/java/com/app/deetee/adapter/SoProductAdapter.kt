package com.app.deetee.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.deetee.R
import com.app.deetee.model.Product
import com.app.deetee.model.SoProduct


class SoProductAdapter(
    private val statuses: MutableList<SoProduct>,
    private val onItemClicked: (Int) -> Unit // Lambda to notify activity of the click
) : RecyclerView.Adapter<SoProductAdapter.StatusViewHolder>() {

    // Keep track of selected item
    private var selectedPosition = 0

    class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt_OrderName: TextView = view.findViewById(R.id.txt_OrderName)
        val txt_orderQtyView: TextView = view.findViewById(R.id.txt_orderQtyView)
        val rrMain: RelativeLayout = view.findViewById(R.id.rrMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_so_product, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val status = statuses[position]

        // Set data
        holder.txt_OrderName.text = status.item_name
        holder.txt_orderQtyView.text = status.quantity

        // Update selection state based on position
        status.isSelected = (position == selectedPosition)

        // Set background based on selection
        if (status.isSelected) {
            holder.rrMain.setBackgroundResource(R.drawable.bg_with_left_border)
        } else {
            holder.rrMain.background = null
        }


        holder.itemView.setOnClickListener {
            Log.d("SoProductAdapter", "Clicked position: $position")
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onItemClicked(position)
        }
    }

    override fun getItemCount(): Int = statuses.size
}



