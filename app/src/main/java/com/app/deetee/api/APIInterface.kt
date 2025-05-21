package com.app.vroomo.api

import com.app.deetee.model.LoginResponse
import com.app.deetee.model.MachineResponse
import com.app.deetee.model.ProductDetailsResponse
import com.app.deetee.model.SalesOrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface APIInterface {

        @Headers("Content-Type: application/json")
        @POST("operator-login")
        fun userLogin(@Body params: Map<String, Int>): Call<LoginResponse>

        @GET("scan-machine/{id}")
        fun getMachine(@Path("id") id: Int): Call<MachineResponse>

        @GET("scan-sales-order/{id}")
        fun getSalesMachine(@Path("id") id: Int): Call<SalesOrderResponse>

        @GET("scan-product/{id}")
        fun getProductDetails(@Path("id") id: Int): Call<ProductDetailsResponse>
}