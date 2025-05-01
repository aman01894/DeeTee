package com.app.deetee.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.deetee.R


data class ProductDetailList(val productHeading: String, val productName: String)

class ProductDetailsAdapter(
    private val statuses: List<ProductDetailList>) : RecyclerView.Adapter<ProductDetailsAdapter.StatusViewHolder>() {

    class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_productNumber: TextView = view.findViewById(R.id.tv_productNumber)
        val tv_productName: TextView = view.findViewById(R.id.tv_productName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_detail, parent, false)
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

