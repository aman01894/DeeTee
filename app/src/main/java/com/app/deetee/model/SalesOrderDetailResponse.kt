package com.app.deetee.model

data class SalesOrderDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val so_details: SoDetailss,
    val operations: List<Operation>
)

data class SoDetailss(
    val id: Int,
    val so_id: String,
    val drawingno: String,
    val sub_product_id: String,
    val item_name: String,
    val description: String,
    val measureunit: String,
    val quantity: String,
    val operation1: String,
    val operation2: String,
    val operation3: String,
    val pass_sheet: List<List<Any>>
)

data class Operation(
    val id: Int,
    val operation_id: String,
    val operation_name: String,
    val unit: String,
    val ideal_cycle_time: String,
    var isSelected: Boolean
)
