package com.app.deetee.model

data class SalesOrderResponse(
    val status: Boolean,
    val message: String,
    val data: SalesOrderData?
)

data class SalesOrderData(
    val id: Int,
    val so_no: String,
    val so_id: String,
    val products: List<Product>
)

data class Product(
    val id: Int,
    val so_id: String,
    val sub_product_id: String?,  // Nullable, as shown in your JSON
    val item_name: String
)

