package com.app.deetee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.deetee.R
import com.app.deetee.model.Product




class ProductAdapter(
    private val statuses: MutableList<Product>
) : RecyclerView.Adapter<ProductAdapter.StatusViewHolder>() {

    class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_productNumber: TextView = view.findViewById(R.id.tv_productNumber)
        val tv_productName: TextView = view.findViewById(R.id.tv_productName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val status = statuses[position]
        val  productNumber = 1+ position
        holder.tv_productNumber.text = "${"Product "+productNumber}"
        holder.tv_productName.text = "${status.item_name}"

        // Handle click on a status item
      /*  holder.itemView.setOnClickListener {
            onStatusClick(status)
        }*/
    }

    override fun getItemCount() = statuses.size
}

