package com.app.deetee

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.deetee.adapter.ProcessAdapter
import com.app.deetee.adapter.ProcessList
import com.app.deetee.adapter.ProductAdapter
import com.app.deetee.adapter.ProductDetailList
import com.app.deetee.adapter.ProductDetailsAdapter
import com.app.deetee.api.Controller
import com.app.deetee.api.ProgressDialogUtil
import com.app.deetee.model.MachineResponse
import com.app.deetee.model.Product
import com.app.deetee.model.ProductDetailsResponse
import com.app.deetee.model.SalesOrderResponse
import com.app.vroomo.api.APIInterface
import com.eld.eld.api.APIClient
import com.google.gson.Gson
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt

class HomeActivity : ComponentActivity() {
    private lateinit var barcodeScannerView: BarcodeView
    private lateinit var tv_search: TextView
    private lateinit var tv_scanMachineQR: TextView
    private lateinit var tv_scanOrderQR: TextView
    private lateinit var tv_scanProductQR: TextView
    private lateinit var tv_scanProcessQR: TextView
    private lateinit var tv_startProcess: TextView
    private lateinit var rr_machineDetail: RelativeLayout
    private lateinit var rl_close: RelativeLayout
    private lateinit var ivBack: RelativeLayout
    private lateinit var rr_scanMachineView: RelativeLayout
    private lateinit var rr_saleOrderView: RelativeLayout
    private lateinit var rr_saleOrder_master: RelativeLayout
    private lateinit var rr_scanMachineDetails: RelativeLayout
    private lateinit var rr_SalesDetailsView: RelativeLayout
    private lateinit var rr_productView: RelativeLayout
    private lateinit var rv_productList: RecyclerView
    private lateinit var rv_productDetailsRecycler: RecyclerView
    private lateinit var rr_producDetailsView: RelativeLayout
    private lateinit var rr_producProcessView: LinearLayout
    private lateinit var rr_processtView: RelativeLayout
    private lateinit var rv_processRecycler: RecyclerView
    private lateinit var rr_product: RelativeLayout
    private lateinit var rr_selectProcess: RelativeLayout
    private lateinit var imgMachineScan: ImageView
    private lateinit var tv_machine_model: TextView
    private lateinit var tv_machine_modelNumber: TextView
    private lateinit var tv_Available: TextView
    private lateinit var tv_machineDetail: TextView
    private lateinit var tv_machine_SoNumber: TextView
    private lateinit var tv_machine_ProductName: TextView
    private lateinit var tv_QT_Order: TextView
    private lateinit var etEnterID: EditText

    private lateinit var tv_verifyMachine: TextView
    private lateinit var imgScnMachine: ImageView
    private lateinit var tv_modelNumber: TextView
    private lateinit var tv_mDetail: TextView
    private lateinit var tv_continue: TextView
    private lateinit var tv_reScan: TextView





    private val CAMERA_PERMISSION_CODE = 100
    private var apiInterface: APIInterface? = null
    private var isMachineScan : Boolean = false
    private var isSalesOrderScan : Boolean = false
    private var isProductScan : Boolean = false
    private var isScanType : Int = 1



    val products = mutableListOf<Product>()
    val adapter = ProductAdapter(products)

    var scanString = ""
    var lastInputTime: Long = 0
    val SCAN_TIMEOUT = 2000L // 1 second timeout


    private fun disableEditTextFocus() {
        etEnterID.clearFocus()
        etEnterID.isFocusable = false
        etEnterID.isFocusableInTouchMode = false
        etEnterID.isCursorVisible = false
    }

    private fun enableEditTextFocus() {
        etEnterID.isFocusable = true
        etEnterID.isFocusableInTouchMode = true
        etEnterID.isCursorVisible = true
        // etUserName.requestFocus()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        apiInterface = APIClient.getClientLogin().create(APIInterface::class.java)
        ProgressDialogUtil.LoadingDialog(this)
        init()
        checkCameraPermission()
        onClick()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastInputTime > SCAN_TIMEOUT) {
                scanString = "" // ✅ Clear scan if timeout passed
                if (scanString.isEmpty()) {
                    disableEditTextFocus()
                }
            }
            lastInputTime = currentTime

            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.e("ScanCode", "✅ Final scan: $scanString")
                if (scanString != null) {
                    if (isScanType.equals(1)){
                        //   var machineID  =  scanString
                        try {
                            var machineID = parseInt(scanString, 10);
                            scanMachine(machineID)
                        }catch (e : Exception){
                            showMessage("Invalid QR")
                        }

                    }
                    else if (isScanType.equals(2)){
                        try {
                            var machineID = parseInt(scanString, 10);
                            scanSalesORder(machineID)
                        }catch (e : Exception){
                            showMessage("Invalid QR")
                        }
                    }else if (isScanType.equals(3)){
                        try {
                            var machineID = parseInt(scanString, 10);
                            productDetails(machineID)
                        }catch (e : Exception){
                            showMessage("Invalid QR")
                        }
                    }
                }else{
                    showMessage("Invalid QR")
                }

                return true
            }

            val pressedKey = event.unicodeChar.toChar()
            if (pressedKey.code != 0) {
                disableEditTextFocus()
                etEnterID.isFocusable = false
                etEnterID.isCursorVisible = false
                scanString += pressedKey
                Log.e("ScanCode", "Scan so far: $scanString")
                return true
            }
        }

        return super.dispatchKeyEvent(event)
    }


    private fun init(){
        rr_saleOrder_master = findViewById(R.id.rr_saleOrder_master)
        tv_search = findViewById(R.id.tv_search)
        tv_scanMachineQR = findViewById(R.id.tv_scanMachineQR)
        rr_machineDetail = findViewById(R.id.rr_machineDetail)
        rl_close = findViewById(R.id.rl_close)
        ivBack = findViewById(R.id.ivBack)
        rr_scanMachineView = findViewById(R.id.rr_scanMachineView)
        rr_scanMachineDetails = findViewById(R.id.rr_scanMachineDetails)
        barcodeScannerView = findViewById(R.id.barcode_scanner)
        rv_productList = findViewById(R.id.rv_productList)
        rr_SalesDetailsView = findViewById(R.id.rr_SalesDetailsView)
        tv_scanOrderQR = findViewById(R.id.tv_scanOrderQR)
        rr_saleOrderView = findViewById(R.id.rr_saleOrderView)
        rv_productDetailsRecycler = findViewById(R.id.rv_productDetailsRecycler)
        rr_producDetailsView = findViewById(R.id.rr_producDetailsView)
        rr_productView = findViewById(R.id.rr_productView)
        tv_scanProductQR = findViewById(R.id.tv_scanProductQR)
        rr_product = findViewById(R.id.rr_product)
        rr_selectProcess = findViewById(R.id.rr_selectProcess)
        tv_startProcess = findViewById(R.id.tv_startProcess)
        etEnterID = findViewById(R.id.etEnterID)
        tv_continue = findViewById(R.id.tv_continue)
        tv_reScan = findViewById(R.id.tv_reScan)

        rr_producProcessView = findViewById(R.id.rr_producProcessView)
        rv_processRecycler = findViewById(R.id.rv_processRecycler)
        rr_processtView = findViewById(R.id.rr_processtView)
        tv_scanProcessQR = findViewById(R.id.tv_scanProcessQR)
        imgMachineScan = findViewById(R.id.imgMachineScan)
        tv_machine_model = findViewById(R.id.tv_machine_model)
        tv_machine_modelNumber = findViewById(R.id.tv_machine_modelNumber)
        tv_Available = findViewById(R.id.tv_Available)
        tv_machineDetail = findViewById(R.id.tv_machineDetail)
        tv_machine_SoNumber = findViewById(R.id.tv_machine_SoNumber)
        tv_machine_ProductName = findViewById(R.id.tv_machine_ProductName)
        tv_QT_Order = findViewById(R.id.tv_QT_Order)
        tv_verifyMachine = findViewById(R.id.tv_verifyMachine)
        imgScnMachine = findViewById(R.id.imgScnMachine)
        tv_modelNumber = findViewById(R.id.tv_modelNumber)
        tv_mDetail = findViewById(R.id.tv_mDetail)

        /*  // for order status
          val products = listOf(
              ProductList("Product 1","Tube Forming Rolls"),
              ProductList("Product 2","Tube Forming Rolls"),
              ProductList("Product 3","Tube Forming Rolls"),
              ProductList("Product 4","Tube Forming Rolls"))*/

        val productDetails = listOf(
            ProductDetailList("Order Qty.","5"),
            ProductDetailList("JC Qty.","5"),
            ProductDetailList("Roll Punch No.","-"),
            ProductDetailList("Special Operation","Spline")
        )

        val processList = listOf(
            ProcessList("Roll Detail","#3"),
            ProcessList("Barrel Diameter","132.6"),
            ProcessList("Barrel Length","686.000"),
            ProcessList("Total Length","777.000")
        )

        //product list adapter
        rv_productList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rv_productList.adapter = ProductAdapter(products)

        //product details adapter
        rv_productDetailsRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rv_productDetailsRecycler.adapter = ProductDetailsAdapter(productDetails)

        rv_processRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rv_processRecycler.adapter = ProcessAdapter(processList)

    }
    private fun onClick(){
        tv_search.setOnClickListener {
            rr_machineDetail.visibility = View.VISIBLE
            tv_search.visibility = View.GONE


        }
        tv_scanMachineQR.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed with scanner
                startCamera(1)
                showMessage("Scan Machine QR")
            } else {
                // Request camera permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }


            /* rr_scanMachineDetails.visibility = View.VISIBLE
             rr_scanMachineView.visibility = View.GONE
             rr_saleOrder_master.setBackgroundResource(R.drawable.rounded_corner_white)
             tv_scanOrderQR.setBackgroundResource(R.drawable.rounded_corner_button)*/
        }

        tv_scanOrderQR.setOnClickListener {
            /* rr_SalesDetailsView.visibility = View.VISIBLE
             rr_saleOrderView.visibility = View.GONE
             tv_scanProductQR.setBackgroundResource(R.drawable.rounded_corner_button)
             rr_product.setBackgroundResource(R.drawable.rounded_corner_white)*/

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed with scanner
                barcodeScannerView.resume()
                startCamera(2)
                showMessage("Scan SalesOrder QR")
            } else {
                // Request camera permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }
        }


        tv_scanProductQR.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed with scanner
                barcodeScannerView.resume()
                startCamera(3)
                showMessage("Scan Product QR")
            } else {
                // Request camera permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }



        }
        tv_scanProcessQR.setOnClickListener {
            rr_producProcessView.visibility = View.VISIBLE
            rr_processtView.visibility = View.GONE
        }

        rl_close.setOnClickListener {
            rr_machineDetail.visibility = View.GONE
            tv_search.visibility = View.VISIBLE
        }
        ivBack.setOnClickListener {
            finish()
        }
        tv_startProcess.setOnClickListener {
            val intent = Intent(this, StartProcessActivity::class.java)
            startActivity(intent)
        }
    }
    private fun startCamera(scanType : Int) {
        barcodeScannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    val scannedValue = it.text
                    if (scannedValue != null) {
                        Log.e("ScanMachine",scannedValue)

                        if (scanType.equals(1)){

                            if (!isMachineScan){
                                try {
                                    isMachineScan = true
                                    var machineID = parseInt(scannedValue, 10);
                                    scanMachine(machineID)
                                }catch (e : Exception){
                                    showMessage("Invalid QR")
                                    isMachineScan = false
                                }
                            }

                        }
                        else if (scanType.equals(2)){

                            if (!isSalesOrderScan){
                                try {
                                    isSalesOrderScan = true
                                    Log.e("ScanData",scannedValue)
                                    var machineID = parseInt(scannedValue, 10);
                                    scanSalesORder(machineID)
                                }catch (e : Exception){
                                    showMessage("Invalid QR")
                                    isSalesOrderScan = false
                                }
                            }

                        }else if (scanType.equals(3)){

                            if (!isProductScan){
                                try {
                                    isProductScan = true
                                    Log.e("ScanData",scannedValue)
                                    var machineID = parseInt(scannedValue, 10);
                                    productDetails(machineID)
                                }catch (e : Exception){
                                    showMessage("Invalid QR")
                                    isProductScan = false
                                }
                            }
                        }
                    }else{
                        // isApiCall = false
                        showMessage("Invalid QR")
                    }
                }
            }
        })
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, proceed with scanner
            //  startCamera()
        } else {
            // Request camera permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, start scanner
                // startCamera()
            } else {
                // Permission denied, show message
                Toast.makeText(this, "Camera permission is required for scanning", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        barcodeScannerView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeScannerView.pause()
    }

    private fun scanMachine(id: Int) {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()
            /* val params = mapOf(
                 "username" to userNm
             )*/
            val call: Call<MachineResponse> = apiInterface!!.getMachine(id)

            call.enqueue(object : Callback<MachineResponse> {
                override fun onResponse(call: Call<MachineResponse>, response: Response<MachineResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("successResponse", "Machine Scan==" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {
                        verifyMachine(1,response,null,null)
                    } else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                        isMachineScan = false
                    }

                }
                override fun onFailure(call: Call<MachineResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    ProgressDialogUtil.hide()
                    isMachineScan = false

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }

    fun verifyMachine(i: Int, response: Response<MachineResponse>?, SOresponse: Response<SalesOrderResponse>?,
                      Prresponse: Response<ProductDetailsResponse>?) {

        rr_machineDetail.visibility = View.VISIBLE
        tv_search.visibility = View.GONE
        if (i==1){
            tv_verifyMachine.setText("Verify Machine Details")
            tv_modelNumber.setText(response?.body()?.data?.machine)
            tv_mDetail.setText(response?.body()?.data?.machine_type)
            Picasso.get().load((response?.body()?.data?.machine_image)).into(imgScnMachine)
        }

        if (i==2){
            tv_verifyMachine.setText("Verify Sale Order")
            tv_modelNumber.setText(SOresponse?.body()?.data?.so_no)
            tv_mDetail.setText("SOID: "+SOresponse?.body()?.data?.so_id)
            //   Picasso.get().load((response?.body()?.data?.machine_image)).into(imgScnMachine)
        }

        if (i==3){
            tv_verifyMachine.setText("Verify Product")
            tv_modelNumber.setText(Prresponse?.body()?.data?.item_name)
            tv_mDetail.setText("")
            //   Picasso.get().load((response?.body()?.data?.machine_image)).into(imgScnMachine)
        }

        tv_continue.setOnClickListener{
            if (i==1){
                isScanType = 2;
                rr_scanMachineDetails.visibility = View.VISIBLE
                rr_scanMachineView.visibility = View.GONE
                rr_saleOrder_master.setBackgroundResource(R.drawable.rounded_corner_white)
                tv_scanOrderQR.setBackgroundResource(R.drawable.rounded_corner_button)
                tv_machine_modelNumber.setText(response?.body()?.data?.machine)
                tv_machineDetail.setText(response?.body()?.data?.machine_type)
                tv_Available.setText(response?.body()?.data?.machine_status)
                Picasso.get().load((response?.body()?.data?.machine_image)).into(imgMachineScan)
            }else if (i==2){
                isScanType = 3;
                rr_SalesDetailsView.visibility = View.VISIBLE
                rr_saleOrderView.visibility = View.GONE
                tv_scanProductQR.setBackgroundResource(R.drawable.rounded_corner_button)
                rr_product.setBackgroundResource(R.drawable.rounded_corner_white)


                val data = SOresponse?.body()?.data
                tv_machine_SoNumber.setText(SOresponse?.body()?.data?.so_no)
                //product list adapter
                data?.products?.let { newProducts ->
                    products.clear()               // Clear old list if needed
                    products.addAll(newProducts)
                    adapter.notifyDataSetChanged()
                }
            }else if (i==3){
                // isScanType = 3;
                rr_producDetailsView.visibility = View.VISIBLE
                rr_productView.visibility = View.GONE
                tv_startProcess.visibility = View.VISIBLE
                rr_selectProcess.setBackgroundResource(R.drawable.rounded_corner_white)
                tv_machine_ProductName.setText(Prresponse?.body()?.data?.item_name)
                tv_QT_Order.setText(Prresponse?.body()?.data?.quantity)
            }

            rr_machineDetail.visibility = View.GONE
            tv_search.visibility = View.VISIBLE
        }

        tv_reScan.setOnClickListener{
            rr_machineDetail.visibility = View.GONE
            tv_search.visibility = View.VISIBLE

            if (i==1){
                isMachineScan = false
            }else if(i==2){
                isSalesOrderScan = false
            }else if(i==3){
                isProductScan = false
            }
        }
    }

    private fun scanSalesORder(id: Int) {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()
            /* val params = mapOf(
                 "username" to userNm
             )*/
            val call: Call<SalesOrderResponse> = apiInterface!!.getSalesMachine(id)

            call.enqueue(object : Callback<SalesOrderResponse> {
                override fun onResponse(call: Call<SalesOrderResponse>, response: Response<SalesOrderResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("successResponse", "Machine Scan==" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {
                        verifyMachine(2,null,response,null)
                    } else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                        isSalesOrderScan = false
                    }

                }
                override fun onFailure(call: Call<SalesOrderResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    ProgressDialogUtil.hide()
                    isSalesOrderScan = false

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }

    private fun productDetails(id: Int) {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()
            /* val params = mapOf(
                 "username" to userNm
             )*/
            val call: Call<ProductDetailsResponse> = apiInterface!!.getProductDetails(id)

            call.enqueue(object : Callback<ProductDetailsResponse> {
                override fun onResponse(call: Call<ProductDetailsResponse>, response: Response<ProductDetailsResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("successResponse", "Machine Scan==" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {
                        verifyMachine(3,null,null,response)
                    } else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                        isProductScan = false
                    }

                }
                override fun onFailure(call: Call<ProductDetailsResponse>, t: Throwable) {
                    call.cancel()
                    showMessage("Something went wrong")
                    ProgressDialogUtil.hide()
                    isProductScan = false

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }


    private fun showMessage(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}