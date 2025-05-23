package com.app.deetee.model

data class OperationDetailFetchResponse(
    val status: Boolean,
    val message: String,
    val data: Dataa
)

data class Dataa(
    val current_roll_no: Int,
    val details: List<Detail>
)

data class Detail(
    val id: Int,
    val so_id: Int,
    val so_product_id: Int,
    val sub_product_id: Int,
    val machine_id: Int,
    val operator_id: Int,
    val pass_id: Int?, // Nullable
    val operation_id: String,
    val start_date_time: String,
    val end_date_time: String,
    val time_taken: Int,
    val total_quantity: Int,
    val quantity_processed: Int?, // Nullable
    val reason: String,
    val roll_status: String,
    val time_taken_formatted: String
)