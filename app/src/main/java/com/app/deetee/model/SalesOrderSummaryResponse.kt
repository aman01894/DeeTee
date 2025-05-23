package com.app.deetee.model

data class SalesOrderSummaryResponse(
    val status: Boolean,
    val message: String,
    val data: SalesOrderDataa
)

data class SalesOrderDataa(
    val user: User,
    val machine: Machine,
    val so_details: SoDetails
)

data class User(
    val id: Int,
    val name: String,
    val profile_image: String,
    val department: String,
    val role_name: String
)

data class Machine(
    val id: Int,
    val unit_name: String,
    val machine: String,
    val machine_image: String,
    val machine_type: String,
    val machine_status: String
)

data class SoDetails(
    val id: Int,
    val so_no: String,
    val so_id: String,
    val scr_status: String,
    val so_products: List<SoProduct>
)

data class SoProduct(
    val id: Int,
    val so_id: String,
    val sub_product_id: String?, // nullable due to `null` in JSON
    val item_name: String,
    val quantity: String,
    val measureunit: String,
    var isSelected: Boolean
)

