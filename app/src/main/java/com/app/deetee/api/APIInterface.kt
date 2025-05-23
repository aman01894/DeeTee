package com.app.vroomo.api

import com.app.deetee.model.LoginResponse
import com.app.deetee.model.MachineResponse
import com.app.deetee.model.OperationDetailFetchResponse
import com.app.deetee.model.OperationStartResponse
import com.app.deetee.model.ProductDetailsResponse
import com.app.deetee.model.SalesOrderDetailResponse
import com.app.deetee.model.SalesOrderRequest
import com.app.deetee.model.SalesOrderResponse
import com.app.deetee.model.SalesOrderSummaryResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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


        @Headers("Content-Type: application/json")
        @POST("sales-order-summary")
        fun salesOrderSummery(@Body params: Map<String, Int>): Call<SalesOrderSummaryResponse>

        @Headers("Content-Type: application/json")
        @POST("sales-order-summary")
        fun getSalesOrderSummary(
                @Body params: Map<String, Int>,
                @Header("Authorization") token: String
        ): Call<SalesOrderSummaryResponse>


        @Headers("Content-Type: application/json")
        @POST("sales-order-details")
        fun getSalesOrderDetail(@Body params: Map<String, Int>, @Header("Authorization") token: String): Call<SalesOrderDetailResponse>

        @Headers("Content-Type: application/json")
        @POST("operation-details-fetch")
        fun getOperationDetail(@Body params: Map<String, Int>, @Header("Authorization") token: String): Call<OperationDetailFetchResponse>


        @Headers("Content-Type: application/json")
        @POST("operation-start")
        fun operationStartAPI(@Body params: Map<String, Int>, @Header("Authorization") token: String): Call<OperationStartResponse>

        @Headers("Content-Type: application/json")
        @POST("operation-stop")
        fun operationStopAPI(@Body params: Map<String, String>, @Header("Authorization") token: String): Call<OperationStartResponse>

        @Headers("Content-Type: application/json")
        @POST("submit-roll")
        fun submitRoll(@Body params: Map<String, String>, @Header("Authorization") token: String): Call<OperationStartResponse>


}