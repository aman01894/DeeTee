package com.app.deetee.model

data class MachineResponse(
    val status: Boolean,
    val message: String,
    val data: MachineData?
)

data class MachineData(
    val id: Int,
    val unit_name: String,
    val machine: String,
    val machine_image: String,
    val machine_type: String,
    val operations: String,
    val machine_status: String
)

