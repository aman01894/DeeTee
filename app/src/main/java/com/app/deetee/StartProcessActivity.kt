package com.app.deetee

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.deetee.adapter.OperationAdapter
import com.app.deetee.adapter.PassSheetAdapter
import com.app.deetee.adapter.ProductAdapter
import com.app.deetee.adapter.SoProductAdapter
import com.app.deetee.api.Controller
import com.app.deetee.api.Preferences
import com.app.deetee.api.ProgressDialogUtil
import com.app.deetee.model.Operation
import com.app.deetee.model.OperationDetailFetchResponse
import com.app.deetee.model.OperationStartResponse
import com.app.deetee.model.Product
import com.app.deetee.model.SalesOrderDetailResponse
import com.app.deetee.model.SalesOrderRequest
import com.app.deetee.model.SalesOrderSummaryResponse
import com.app.deetee.model.SoProduct
import com.app.vroomo.api.APIInterface
import com.eld.eld.api.APIClient
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class StartProcessActivity : ComponentActivity() {

    private lateinit var rr_soView: RelativeLayout
    private lateinit var rrDriveRols: RelativeLayout
    private lateinit var rr_NosView: RelativeLayout
    private lateinit var rr_passSheetView: RelativeLayout
    private lateinit var ll_nosView: LinearLayout
    private lateinit var view20: View
    private lateinit var rrStageRollView: RelativeLayout
    private lateinit var rr_NosRollView: RelativeLayout
    private lateinit var operatorIMG: ImageView
    private lateinit var machineImg: ImageView
    private lateinit var txt_operatorName: TextView
    private lateinit var txt_operatorPost: TextView
    private lateinit var txt_machineName: TextView
    private lateinit var txt_machineDetail: TextView
    private lateinit var txt_salesOrderName: TextView
    private lateinit var txt_orderNumber: TextView
    private lateinit var rv_productList: RecyclerView
    private lateinit var rv_operationRecyclerView: RecyclerView
    private lateinit var rv_PassSheetRecyclerView: RecyclerView
    private lateinit var rv_operationSETRecyclerView: RecyclerView
    private lateinit var txtOperatorName: TextView
    private lateinit var txt_drowingNum: TextView
    private lateinit var txt_gradNum: TextView
    private lateinit var txt_cappingNum: TextView
    private lateinit var txt_labNum: TextView
    private lateinit var txt_wDDNum: TextView
    private lateinit var txt_quantity: TextView
    private lateinit var txt_jcQty: TextView
    private lateinit var txt_punchNumber: TextView
    private lateinit var txt_operationName: TextView
    private lateinit var txtRollNum: TextView
    // timer one
    private lateinit var tv_submitTask: TextView
    private lateinit var tv_min: TextView
    private lateinit var tv_sec: TextView
    private lateinit var tv_start: TextView
    private lateinit var tv_maintenance: TextView
    private lateinit var tvSubmit: TextView
    private lateinit var tv_submitFinalTask: TextView


    private lateinit var txtRollNummSET: TextView
    private lateinit var tv_submitTaskSET: TextView
    private lateinit var tv_minSET: TextView
    private lateinit var tv_secSET: TextView
    private lateinit var tv_startSET: TextView
    private lateinit var tv_maintenanceSET: TextView
    private lateinit var tvSubmitSET: TextView
    private lateinit var txtProductD: TextView

    private lateinit var adapter: SoProductAdapter
    private val products = mutableListOf<SoProduct>()

    private var apiInterface: APIInterface? = null
    var mContext: Context = this
    var mID : Int = 0
    var so_product_id : Int = 0
    var operation_id : Int = 0
    var pass_id : Int = 0
    var operationStartID : Int = 0
    var currentRollID : Int = 0

    // operation nos View
    private lateinit var operationAdapter: OperationAdapter
    private val operationDataList = mutableListOf<Operation>()

    //Pass Sheeet view
    private lateinit var passSheetAdapter: PassSheetAdapter


    // operation set View
    private lateinit var operationSETAdapter: OperationAdapter
    private val operationSETDataList = mutableListOf<Operation>()

    // timer
    var isFirstTimer :Boolean = false
    lateinit var startTimerDateTimeFirst : String
    lateinit var endTimerDateTimeFirst : String
    private var firstHandler: Handler? = null
    private var firstUpdateTask: Runnable? = null


    fun init(){
        operatorIMG = findViewById(R.id.operatorIMG);
        txt_operatorName = findViewById(R.id.txt_operatorName);
        txt_operatorPost = findViewById(R.id.txt_operatorPost);
        txtProductD = findViewById(R.id.txtProductD);

        machineImg = findViewById(R.id.machineImg);
        txt_machineName = findViewById(R.id.txt_machineName);
        txt_machineDetail = findViewById(R.id.txt_machineDetail);

        txt_salesOrderName = findViewById(R.id.txt_salesOrderName);
        txt_orderNumber = findViewById(R.id.txt_orderNumber);

        rv_productList = findViewById(R.id.rv_soproductList)
        rv_operationRecyclerView = findViewById(R.id.rv_operationRecyclerView)
        rv_PassSheetRecyclerView = findViewById(R.id.rv_PassSheetRecyclerView)
        rv_operationSETRecyclerView = findViewById(R.id.rv_operationSETRecyclerView)

        txtOperatorName = findViewById(R.id.txtOperatorName)
        txt_drowingNum = findViewById(R.id.txt_drowingNum)
        txt_gradNum = findViewById(R.id.txt_gradNum)
        txt_cappingNum = findViewById(R.id.txt_cappingNum)
        txt_labNum = findViewById(R.id.txt_labNum)
        txt_wDDNum = findViewById(R.id.txt_wDDNum)
        txt_quantity = findViewById(R.id.txt_quantity)
        txt_jcQty = findViewById(R.id.txt_jcQty)
        txt_punchNumber = findViewById(R.id.txt_punchNumber)
        txt_operationName = findViewById(R.id.txt_operationName)
        txtRollNum = findViewById(R.id.txtRollNum)
        tv_submitTask = findViewById(R.id.tv_submitTask)
        tv_min = findViewById(R.id.tv_min)
        tv_sec = findViewById(R.id.tv_sec)
        tv_start = findViewById(R.id.tv_start)
        tv_maintenance = findViewById(R.id.tv_maintenance)
        tvSubmit = findViewById(R.id.tvSubmit)
        tv_submitFinalTask = findViewById(R.id.tv_submitFinalTask)

        txtRollNummSET = findViewById(R.id.txtRollNummSET)
        tv_submitTaskSET = findViewById(R.id.tv_submitTaskSET)
        tv_minSET = findViewById(R.id.tv_minSET)
        tv_secSET = findViewById(R.id.tv_secSET)

        tv_startSET = findViewById(R.id.tv_startSET)
        tv_maintenanceSET = findViewById(R.id.tv_maintenanceSET)
        tvSubmitSET = findViewById(R.id.tvSubmitSET)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_start_process)
        apiInterface = APIClient.getClientLogin().create(APIInterface::class.java)
        ProgressDialogUtil.LoadingDialog(this)
        init()
        rr_soView = findViewById(R.id.rr_soView)
        rrDriveRols = findViewById(R.id.rrDriveRols)
        rr_NosView = findViewById(R.id.rr_stageView)
        rr_passSheetView = findViewById(R.id.rr_DriveRollView)
        view20 = findViewById(R.id.view20)
        ll_nosView = findViewById(R.id.ll_nosView)
        rrStageRollView = findViewById(R.id.rrStageRollView)
        rr_NosRollView = findViewById(R.id.rr_NosRollView)


        mID = intent.getIntExtra("machineID", 0)
        val soID = intent.getIntExtra("soId", 0)
        txtProductD.setText("Machine"+mID+"SOID"+soID)
        Log.e("SOID" ,  "idd"+soID)
        // view for show product
        rv_productList.layoutManager = LinearLayoutManager(this)
        adapter = SoProductAdapter(products) { position ->
            // Handle click here
            val clickedItem = products[position]
            try {
                var so_product_ids = Integer.parseInt(clickedItem.sub_product_id, 10);
                //getSalesOrderDetails(mID,so_product_id)
                so_product_id    = 0
                operation_id   = 0
                pass_id   = 0
                getSalesOrderDetails(mID,so_product_ids)
            }catch (e : Exception){
                showMessage("SO Id not found")
            }

        }
        rv_productList.adapter = adapter
        // rv_productList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        //view of operation NOS
        rv_operationRecyclerView.layoutManager = LinearLayoutManager(this)
        operationAdapter = OperationAdapter(operationDataList) { position ->
            // Handle click here
            val clickedItem = operationDataList[position]
            try {
                //  var so_product_id = Integer.parseInt(clickedItem.sub_product_id, 10);
                operation_id = clickedItem.id
                getOperationDetails()
            }catch (e : Exception){
                showMessage("SO Id not found")
            }

        }
        rv_operationRecyclerView.adapter = operationAdapter

        // operation SET View

        rv_operationSETRecyclerView.layoutManager = LinearLayoutManager(this)
        operationSETAdapter = OperationAdapter(operationSETDataList) { position ->
            // Handle click here
            val clickedItem = operationSETDataList[position]
            try {
                //  var so_product_id = Integer.parseInt(clickedItem.sub_product_id, 10);
                operation_id = clickedItem.id
                getOperationDetails()
            }catch (e : Exception){
                showMessage("SO Id not found")
            }

        }
        rv_operationSETRecyclerView.adapter = operationSETAdapter
        operationSETDataList
        // passsheet
        rv_PassSheetRecyclerView.layoutManager = LinearLayoutManager(this)


        getSalesOrderSummery (mID,soID)
        //  getSalesOrderSummery (1,86)
        onclick()
        /*  rr_soView.setOnClickListener {

              rr_NosView.visibility = View.VISIBLE
              rr_passSheetView.visibility = View.GONE
              view20.visibility = View.GONE
              ll_nosView.visibility = View.GONE
              rr_NosRollView.visibility = View.GONE
              rrStageRollView.visibility = View.VISIBLE
          }*/

        /* rrDriveRols.setOnClickListener {

             rr_NosView.visibility = View.GONE
             rr_passSheetView.visibility = View.VISIBLE
             view20.visibility = View.VISIBLE
             ll_nosView.visibility = View.VISIBLE
             rr_NosRollView.visibility = View.VISIBLE
             rrStageRollView.visibility = View.GONE
         }*/
        // deleteAllRecordsDialog()
    }


    fun onclick(){

        // type NOS
        tv_start.setOnClickListener{
            if (isFirstTimer){
                operationStartAPI ()
            }
        }

        tvSubmit.setOnClickListener{
            if (!isFirstTimer){
                operationStopAPI()
            }
        }

        tv_submitFinalTask.setOnClickListener{

            submitRolAPI()
        }

        // type SET
        tv_startSET.setOnClickListener{
            if (isFirstTimer){
                operationStartAPI ()
            }
        }
        tvSubmitSET.setOnClickListener{
            if (!isFirstTimer){
                operationStopAPI()
            }
        }


    }

    private fun deleteAllRecordsDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.worning_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        /* val cc_logOut = dialog.findViewById<CardView>(R.id.cc_ok)
         val cc_cancel = dialog.findViewById<CardView>(R.id.cc_cancel)
         val txt_alert_message = dialog.findViewById<TextView>(R.id.txt_alert_message)
         val txt_okButton = dialog.findViewById<TextView>(R.id.txt_okButton)
         val progress_wheelss = dialog.findViewById<ProgressBar>(R.id.progress_wheelss)
         txt_alert_message.text = "Are you sure you want to delete your all record?"*/

        /*  cc_cancel.setOnClickListener { view: View? -> dialog.dismiss() }*/
        dialog.show()
    }


    private fun getSalesOrderSummery (machine_id: Int,so_id: Int) {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()

            val params = mapOf(
                "machine_id" to machine_id,
                "so_id" to so_id
            )

            val token = "Bearer "+ Preferences.get(mContext, Preferences.KEY_TOKEN)
            //val token = "Bearer 16|2Z64c8n39fWVWpaXWUYWnnz4qB7rjc5OSdsfvVQV3b3d9d04"
            val call: Call<SalesOrderSummaryResponse> = apiInterface!!.getSalesOrderSummary(params,token)

            call.enqueue(object : Callback<SalesOrderSummaryResponse> {
                override fun onResponse(call: Call<SalesOrderSummaryResponse>, response: Response<SalesOrderSummaryResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("SalesSummeryRes", "" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {

                        if (response.body()!!.data!=null){

                            // user detail
                            txt_operatorName.setText(response.body()!!.data.user.name)
                            txt_operatorPost.setText(response.body()!!.data.user.role_name)
                            Picasso.get().load((response.body()!!.data.user.profile_image)).into(operatorIMG)

                            //machine details
                            Picasso.get().load((response.body()!!.data.machine.machine_image)).into(machineImg)
                            txt_machineName.setText(response.body()!!.data.machine.machine)
                            txt_machineDetail.setText(response.body()!!.data.machine.unit_name+", "+response.body()!!.data.machine.machine_type)

                            //sales order
                            txt_salesOrderName.setText(response.body()!!.data.so_details.so_no)
                            //txt_orderNumber.setText(""+response.body()!!.data.so_details.so_products.size)

                            /* val data = response?.body()?.data?.so_details
                             data?.so_products?.let { newProducts ->
                                 products.clear()               // Clear old list if needed
                                 products.addAll(newProducts)
                                 adapter.notifyDataSetChanged()
                             }*/

                            val newProducts = response.body()!!.data.so_details.so_products
                            products.clear()
                            products.addAll(newProducts)
                            adapter.notifyDataSetChanged()

                            if (newProducts.size>0){

                                var so_product_id = newProducts.get(0).id
                                getSalesOrderDetails(machine_id,so_product_id)
                            }


                        }
                    } else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                    }

                }
                override fun onFailure(call: Call<SalesOrderSummaryResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    Log.e("Error","== "+t.message)
                    ProgressDialogUtil.hide()

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }

    private fun getSalesOrderDetails (machine_id: Int,so_pro_id: Int) {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()
            val params = mapOf(
                "machine_id" to machine_id,
                "so_product_id" to so_pro_id
            )
            val token = "Bearer "+ Preferences.get(mContext, Preferences.KEY_TOKEN)
            //val token = "Bearer 16|2Z64c8n39fWVWpaXWUYWnnz4qB7rjc5OSdsfvVQV3b3d9d04"
            val call: Call<SalesOrderDetailResponse> = apiInterface!!.getSalesOrderDetail(params,token)

            call.enqueue(object : Callback<SalesOrderDetailResponse> {
                override fun onResponse(call: Call<SalesOrderDetailResponse>, response: Response<SalesOrderDetailResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("SalesDetailsRes", "" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {

                        if (response.body()!!.data!=null){

                            txtOperatorName.setText(response.body()!!.data.so_details.item_name)
                            txt_drowingNum.setText(response.body()!!.data.so_details.drawingno)
                            txt_quantity.setText(response.body()!!.data.so_details.quantity)


                            if (response.body()!!.data.so_details.measureunit.equals("SET")){

                                rr_NosView.visibility = View.GONE
                                rr_passSheetView.visibility = View.VISIBLE
                                view20.visibility = View.VISIBLE
                                ll_nosView.visibility = View.VISIBLE
                                rr_NosRollView.visibility = View.VISIBLE
                                rrStageRollView.visibility = View.GONE

                                val passSheetList = response.body()?.data?.so_details?.pass_sheet ?: emptyList()

                                passSheetAdapter = PassSheetAdapter(passSheetList) { position ->
                                    // Handle click here
                                    val clickedItem = passSheetList[position]
                                    try {

                                    }catch (e : Exception){
                                        showMessage("SO Id not found")
                                    }

                                }
                                rv_PassSheetRecyclerView.adapter = passSheetAdapter


                                // show SET operation
                                val newOperation = response.body()!!.data.operations
                                operationSETDataList.clear()
                                operationSETDataList.addAll(newOperation)
                                operationSETAdapter.notifyDataSetChanged()

                                if (newOperation.size>0){
                                    so_product_id = response.body()!!.data.so_details.id
                                    operation_id = newOperation.get(0).id
                                    pass_id = 0
                                    getOperationDetails ()
                                }

                            }
                            else if (response.body()!!.data.so_details.measureunit.equals("NOS")){

                                rr_NosView.visibility = View.VISIBLE
                                rr_passSheetView.visibility = View.GONE
                                view20.visibility = View.GONE
                                ll_nosView.visibility = View.GONE
                                rr_NosRollView.visibility = View.GONE
                                rrStageRollView.visibility = View.VISIBLE


                                // show operation
                                val newOperation = response.body()!!.data.operations
                                operationDataList.clear()
                                operationDataList.addAll(newOperation)
                                operationAdapter.notifyDataSetChanged()

                                if (newOperation.size>0){
                                    so_product_id = response.body()!!.data.so_details.id
                                    operation_id = newOperation.get(0).id
                                    pass_id = 0
                                    getOperationDetails ()
                                }
                            }

                            /*  txt_gradNum = findViewById(R.id.txt_gradNum)
                              txt_cappingNum = findViewById(R.id.txt_cappingNum)
                              txt_labNum = findViewById(R.id.txt_labNum)
                              txt_wDDNum = findViewById(R.id.txt_wDDNum)
                              txt_quantity = findViewById(R.id.txt_quantity)
                              txt_jcQty = findViewById(R.id.txt_jcQty)
                              txt_punchNumber = findViewById(R.id.txt_punchNumber)
                              txt_operationName = findViewById(R.id.txt_operationName)
  */
                        }
                    } else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                    }

                }
                override fun onFailure(call: Call<SalesOrderDetailResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    Log.e("Error","== "+t.message)
                    ProgressDialogUtil.hide()

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }

    private fun getOperationDetails () {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()
            var params: Map<String, Any>?
            if (pass_id.equals(0)){
                params = mapOf(
                    "machine_id" to mID,
                    "so_product_id" to so_product_id,
                    "operation_id" to operation_id
                )
            }else{
                params = mapOf(
                    "machine_id" to mID,
                    "so_product_id" to so_product_id,
                    "operation_id" to operation_id,
                    "pass_id" to pass_id
                )
            }

            val token = "Bearer "+ Preferences.get(mContext, Preferences.KEY_TOKEN)
            //val token = "Bearer 16|2Z64c8n39fWVWpaXWUYWnnz4qB7rjc5OSdsfvVQV3b3d9d04"
            val call: Call<OperationDetailFetchResponse> = apiInterface!!.getOperationDetail(params,token)

            call.enqueue(object : Callback<OperationDetailFetchResponse> {
                override fun onResponse(call: Call<OperationDetailFetchResponse>, response: Response<OperationDetailFetchResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("SalesOperationFetch", "" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {
                        if (response.body()!!.data!=null){
                            var currentRoll = response.body()!!.data.current_roll_no
                            txtRollNum.setText("Roll No. "+currentRoll.toString())
                            txtRollNummSET.setText("Roll No. "+currentRoll.toString())
                            currentRollID = response.body()!!.data.current_roll_no

                            if (response.body()!!.data.details.size>0){
                                isFirstTimer = true
                                startTimerDateTimeFirst = response.body()!!.data.details.get(0).start_date_time
                                operationStartID = response.body()!!.data.details.get(0).id
                                //  endTimerDateTimeFirst = response.body()!!.data.details.get(0).end_date_time

                            }

                        }
                    }
                    else if (response.code() == 404){
                        isFirstTimer = true

                    }
                    else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                    }

                }
                override fun onFailure(call: Call<OperationDetailFetchResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    Log.e("Error","== "+t.message)
                    ProgressDialogUtil.hide()

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }

    private fun showMessage(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun operationStartAPI () {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()

            var params: Map<String, Any>?
            if (pass_id.equals(0)){
                params = mapOf(
                    "machine_id" to mID,
                    "so_product_id" to so_product_id,
                    "operation_id" to operation_id,
                    "current_roll_no" to currentRollID
                )
            }else{
                params = mapOf(
                    "machine_id" to mID,
                    "pass_id" to pass_id,
                    "so_product_id" to so_product_id,
                    "operation_id" to operation_id,
                    "current_roll_no" to currentRollID
                )
            }

            val token = "Bearer "+ Preferences.get(mContext, Preferences.KEY_TOKEN)
            // val token = "Bearer 16|2Z64c8n39fWVWpaXWUYWnnz4qB7rjc5OSdsfvVQV3b3d9d04"
            val call: Call<OperationStartResponse> = apiInterface!!.operationStartAPI(params,token)

            call.enqueue(object : Callback<OperationStartResponse> {
                override fun onResponse(call: Call<OperationStartResponse>, response: Response<OperationStartResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("SalesOperationFetch", "" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {
                        showMessage(response.body()!!.message)
                        startTimer()
                        isFirstTimer = false
                    }
                    else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                    }

                }
                override fun onFailure(call: Call<OperationStartResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    Log.e("Error","== "+t.message)
                    ProgressDialogUtil.hide()

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }


    private fun operationStopAPI () {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()

            var params: Map<String, Any>?
            if (pass_id.equals(0)){
                params = mapOf(
                    "id" to ""+operationStartID,
                    "machine_id" to ""+mID,
                    "so_product_id" to ""+so_product_id,
                    "operation_id" to ""+operation_id,
                    "reason" to "completed"
                )
            }else{
                params = mapOf(
                    "id" to ""+operationStartID,
                    "machine_id" to ""+mID,
                    "pass_id" to ""+pass_id,
                    "so_product_id" to ""+so_product_id,
                    "operation_id" to ""+operation_id,
                    "reason" to "completed"
                )
            }

            val token = "Bearer "+ Preferences.get(mContext, Preferences.KEY_TOKEN)
            //val token = "Bearer 16|2Z64c8n39fWVWpaXWUYWnnz4qB7rjc5OSdsfvVQV3b3d9d04"
            val call: Call<OperationStartResponse> = apiInterface!!.operationStopAPI(params,token)

            call.enqueue(object : Callback<OperationStartResponse> {
                override fun onResponse(call: Call<OperationStartResponse>, response: Response<OperationStartResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("SalesOperationFetch", "" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {
                        showMessage(response.body()!!.message)
                        stopTimer()
                        isFirstTimer = true
                    }
                    else if (response.code() == 404){


                    }
                    else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                    }

                }
                override fun onFailure(call: Call<OperationStartResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    Log.e("Error","== "+t.message)
                    ProgressDialogUtil.hide()

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }

    private fun submitRolAPI () {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()
            var params: Map<String, Any>?
            if (pass_id.equals(0)){
                params = mapOf(
                    "machine_id" to ""+mID,
                    "so_product_id" to ""+so_product_id,
                    "operation_id" to ""+operation_id,
                )
            }else{
                params = mapOf(
                    "machine_id" to ""+mID,
                    "pass_id" to ""+pass_id,
                    "so_product_id" to ""+so_product_id,
                    "operation_id" to ""+operation_id,
                )
            }
            val token = "Bearer "+ Preferences.get(mContext, Preferences.KEY_TOKEN)
            //  val token = "Bearer 16|2Z64c8n39fWVWpaXWUYWnnz4qB7rjc5OSdsfvVQV3b3d9d04"
            val call: Call<OperationStartResponse> = apiInterface!!.submitRoll(params,token)
            call.enqueue(object : Callback<OperationStartResponse> {
                override fun onResponse(call: Call<OperationStartResponse>, response: Response<OperationStartResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("SalesOperationFetch", "" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {
                        showMessage(response.body()!!.message)
                        finish()
                    }
                    else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                    }

                }
                override fun onFailure(call: Call<OperationStartResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    Log.e("Error","== "+t.message)
                    ProgressDialogUtil.hide()
                }
            })
        } else {
            showMessage("No internet connection")
        }
    }


    // first timer
    fun startTimer() {
        firstHandler = Handler(Looper.getMainLooper())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()

        val fallbackStartDate = Date()
        val parsedStartDate = if (!startTimerDateTimeFirst.isNullOrBlank()) {
            try {
                dateFormat.parse(startTimerDateTimeFirst)
            } catch (e: Exception) {
                fallbackStartDate
            }
        } else {
            fallbackStartDate
        }

        firstUpdateTask = object : Runnable {
            override fun run() {
                val now = Date()
                val diffInMillis = now.time - parsedStartDate.time

                val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60

                tv_submitTask.text = String.format("%02d", hours)
                tv_min.text = String.format("%02d", minutes)
                tv_sec.text = String.format("%02d", seconds)

                // settime for SET

                tv_submitTaskSET.text = String.format("%02d", hours)
                tv_minSET.text = String.format("%02d", minutes)
                tv_secSET.text = String.format("%02d", seconds)

                firstHandler?.postDelayed(this, 1000)
            }
        }

        firstHandler?.post(firstUpdateTask!!)
    }

    fun stopTimer() {
        firstHandler?.removeCallbacks(firstUpdateTask!!)
    }

}