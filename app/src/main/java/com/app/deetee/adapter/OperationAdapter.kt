package com.app.deetee.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.deetee.R
import com.app.deetee.model.Operation
import com.app.deetee.model.Product
import com.app.deetee.model.SoProduct


class OperationAdapter(
    private val statuses: MutableList<Operation>,
    private val onItemClicked: (Int) -> Unit // Lambda to notify activity of the click
) : RecyclerView.Adapter<OperationAdapter.StatusViewHolder>() {

    // Keep track of selected item
    private var selectedPosition = 0

    class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtOperation: TextView = view.findViewById(R.id.txtOperation)
        val txt_time: TextView = view.findViewById(R.id.txt_time)
        val rr_blankingDate: RelativeLayout = view.findViewById(R.id.rr_blankingDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_operation, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val status = statuses[position]
        // Set data
        holder.txtOperation.text = status.operation_name
        // Update selection state based on position
        status.isSelected = (position == selectedPosition)

        // Set background based on selection
        if (status.isSelected) {
            holder.rr_blankingDate.setBackgroundResource(R.drawable.bg_blue_bg)
            holder.txt_time.visibility =View.VISIBLE
            holder.txt_time.text = status.ideal_cycle_time
        } else {
            holder.txt_time.visibility =View.GONE

            holder.rr_blankingDate.background = null
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



