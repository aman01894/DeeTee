package com.app.deetee

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.app.deetee.adapter.ProductList
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView

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

    private val CAMERA_PERMISSION_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
        checkCameraPermission()
        onClick()
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

        rr_producProcessView = findViewById(R.id.rr_producProcessView)
        rv_processRecycler = findViewById(R.id.rv_processRecycler)
        rr_processtView = findViewById(R.id.rr_processtView)
        tv_scanProcessQR = findViewById(R.id.tv_scanProcessQR)


        // for order status
        val products = listOf(
            ProductList("Product 1","Tube Forming Rolls"),
            ProductList("Product 2","Tube Forming Rolls"),
            ProductList("Product 3","Tube Forming Rolls"),
            ProductList("Product 4","Tube Forming Rolls"))

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
            rr_scanMachineDetails.visibility = View.VISIBLE
            rr_scanMachineView.visibility = View.GONE
            rr_saleOrder_master.setBackgroundResource(R.drawable.rounded_corner_white)
            tv_scanOrderQR.setBackgroundResource(R.drawable.rounded_corner_button)
        }

        tv_scanOrderQR.setOnClickListener {
            rr_SalesDetailsView.visibility = View.VISIBLE
            rr_saleOrderView.visibility = View.GONE
            tv_scanProductQR.setBackgroundResource(R.drawable.rounded_corner_button)
            rr_product.setBackgroundResource(R.drawable.rounded_corner_white)
        }

        tv_scanProductQR.setOnClickListener {
            rr_producDetailsView.visibility = View.VISIBLE
            rr_productView.visibility = View.GONE
            tv_startProcess.visibility = View.VISIBLE
            tv_scanProcessQR.setBackgroundResource(R.drawable.rounded_corner_button)
            rr_selectProcess.setBackgroundResource(R.drawable.rounded_corner_white)


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
    private fun startCamera() {
        barcodeScannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    val scannedValue = it.text
                    //Toast.makeText(this@HomeActivity, "Scanned: $scannedValue", Toast.LENGTH_SHORT).show()
                    // Send result back to MainActivity
                    /*  val intent = Intent()
                      intent.putExtra("QR_RESULT", scannedValue)
                      setResult(RESULT_OK, intent)
                      finish()*/ // Close Scanner Activity
                }
            }
        })
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, proceed with scanner
            startCamera()
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
                startCamera()
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

}