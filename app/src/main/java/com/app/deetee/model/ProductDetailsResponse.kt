package com.app.deetee.model

data class ProductDetailsResponse(
    val status: Boolean,
    val message: String,
    val data: ProductDetails?
)

data class ProductDetails(
    val id: Int,
    val so_id: String,
    val so_product_qr_code: String,
    val drawingno: String,
    val sub_product_id: String,
    val item_id: String,
    val item_name: String,
    val partno: String,
    val description: String,
    val unit: String,
    val size1: String,
    val size2: String,
    val size3: String,
    val size4: String,
    val material: String,
    val hardness: String,
    val measureunit: String,
    val quantity: String,
    val rate: String,
    val poquantity: String,
    val porate: String,
    val additionaloperation: String,
    val remark: String,
    val bgroupsheet: String,
    val pcs: String,
    val total: String,
    val totalinr: String,
    val sequence: String,
    val cpoid: String,
    val totalpcs: String,
    val operation1: String,
    val operation2: String,
    val operation3: String,
    val pass_no: String,
    val tube_size: String,
    val pass_sheet: String,
    val soquantity: String,
    val sorate: String,
    val cpoitemid: String,
    val created_at: String?,   // Nullable
    val updated_at: String
)

