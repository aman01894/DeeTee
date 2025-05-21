package com.app.deetee

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.deetee.api.Controller
import com.app.deetee.api.Preferences
import com.app.deetee.api.ProgressDialogUtil
import com.app.deetee.model.LoginResponse
import com.app.vroomo.api.APIInterface
import com.eld.eld.api.APIClient
import com.google.gson.Gson
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManualInputActivity : ComponentActivity() {

    private lateinit var barcodeScannerView: BarcodeView
    private lateinit var tvContine: TextView
    private lateinit var rr_machineDetail: RelativeLayout
    private lateinit var rr_MenualINputView: RelativeLayout
    private lateinit var rl_close: RelativeLayout
    private lateinit var ivBack: RelativeLayout
    private lateinit var ivHelp: RelativeLayout
    private lateinit var tv_continue: TextView
    private lateinit var etUserName: EditText

    private val CAMERA_PERMISSION_CODE = 100
    private var isApiCall : Boolean = false
    private var apiInterface: APIInterface? = null
    var mContext: Context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_manual_input)

        apiInterface = APIClient.getClientLogin().create(APIInterface::class.java)
        ProgressDialogUtil.LoadingDialog(this)
        barcodeScannerView = findViewById(R.id.barcode_scanner)
        rr_machineDetail = findViewById(R.id.rr_machineDetail)
        rr_MenualINputView = findViewById(R.id.rr_MenualINputView)
        rl_close = findViewById(R.id.rl_close)
        tvContine = findViewById(R.id.tvContine)
        ivBack = findViewById(R.id.ivBack)
        ivHelp = findViewById(R.id.ivHelp)
        tv_continue = findViewById(R.id.tv_continue)
        etUserName = findViewById(R.id.etUserName)

        checkCameraPermission()

        val username = intent.getStringExtra("key_username") ?: ""
        //val userId = intent.getIntExtra("key_userid", -1)
        etUserName.setText(username)
        tvContine.setOnClickListener {
            rr_machineDetail.visibility = View.VISIBLE
            rr_MenualINputView.visibility = View.GONE
        }

        tv_continue.setOnClickListener {
            var userName = etUserName.text.toString()
            if (userName!=null && !userName.equals("")){
              //  userLogin(userName)
            }else{
                showMessage("Enter Employee ID")
            }
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
                    if (scannedValue != null) {
                        val jsonObject = JSONObject(scannedValue)

                        if (jsonObject.has("username") && !jsonObject.isNull("username")) {
                            if (!isApiCall){
                                isApiCall = true
                                Log.e("ScanData",scannedValue)
                                var userName  =   jsonObject.getString("username")
                               // userLogin(userName)
                            }
                        }else{
                            isApiCall = false
                            showMessage("Invalid QR")
                        }
                    }else{
                        isApiCall = false
                        showMessage("Invalid QR")
                    }
                }
            }
        })
    }
    private fun showMessage(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    private fun nextActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
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

/*
    private fun userLogin(userNm: String) {
        if (Controller.isOnline(this)) {
            ProgressDialogUtil.show()
            val params = mapOf(
                "username" to userNm
            )
            val call: Call<LoginResponse> = apiInterface!!.userLogin(params)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    Log.i("successResponse", "" + successResponse)
                    ProgressDialogUtil.hide()
                    if (response.code() == 200) {
                        // val status: String = response.body().()
                        Preferences.save(mContext, Preferences.KEY_TOKEN,response.body()!!.token)
                        nextActivity()
                    } else {
                        showMessage(response.body()!!.message)
                    }
                    isApiCall =false
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    call.cancel()
                    isApiCall =false
                    showMessage("Something went wrong")
                }
            })
        } else {
            showMessage("No internet connection")
        }
    }
*/
}