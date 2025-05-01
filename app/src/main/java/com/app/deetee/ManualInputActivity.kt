package com.app.deetee

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView

class ManualInputActivity : ComponentActivity() {

    private lateinit var barcodeScannerView: BarcodeView
    private lateinit var tvContine: TextView
    private lateinit var rr_machineDetail: RelativeLayout
    private lateinit var rr_MenualINputView: RelativeLayout
    private lateinit var rl_close: RelativeLayout
    private lateinit var ivBack: RelativeLayout
    private lateinit var ivHelp: RelativeLayout
    private lateinit var tv_continue: TextView

    private val CAMERA_PERMISSION_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_manual_input)
        barcodeScannerView = findViewById(R.id.barcode_scanner)
        rr_machineDetail = findViewById(R.id.rr_machineDetail)
        rr_MenualINputView = findViewById(R.id.rr_MenualINputView)
        rl_close = findViewById(R.id.rl_close)
        tvContine = findViewById(R.id.tvContine)
        ivBack = findViewById(R.id.ivBack)
        ivHelp = findViewById(R.id.ivHelp)
        tv_continue = findViewById(R.id.tv_continue)

        checkCameraPermission()


        tvContine.setOnClickListener {
            rr_machineDetail.visibility = View.VISIBLE
            rr_MenualINputView.visibility = View.GONE
        }

        tv_continue.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        rl_close.setOnClickListener {
            rr_machineDetail.visibility = View.GONE
            rr_MenualINputView.visibility = View.VISIBLE
        }

        ivBack.setOnClickListener {
            finish()
        }

        ivHelp.setOnClickListener {
            val intent = Intent(this, SearchRuteCardActivity::class.java)
            startActivity(intent)
        }


    }

    private fun startCamera() {
        barcodeScannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    val scannedValue = it.text
                    //Toast.makeText(this@ScanActivity, "Scanned: $scannedValue", Toast.LENGTH_SHORT).show()
                    nextActivity()
                    // Send result back to MainActivity
                    /*  val intent = Intent()
                      intent.putExtra("QR_RESULT", scannedValue)
                      setResult(RESULT_OK, intent)
                      finish()*/ // Close Scanner Activity
                }
            }
        })
    }
    private fun nextActivity(){
        /*val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)*/
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