package com.app.deetee.model

import com.google.gson.annotations.SerializedName

data class SalesOrderRequest(
    @SerializedName("machine_id") val machineID: Int,
    @SerializedName("so_id") val soId: Int
)