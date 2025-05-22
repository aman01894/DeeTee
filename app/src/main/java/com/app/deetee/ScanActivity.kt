package com.app.deetee

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.deetee.api.Controller
import com.app.deetee.api.Preferences
import com.app.deetee.api.ProgressDialogUtil
import com.app.deetee.model.LoginResponse
import com.app.vroomo.api.APIInterface
import com.eld.eld.api.APIClient.getClientLogin
import com.google.gson.Gson
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanActivity : AppCompatActivity() {
    var mContext: Context = this
    private lateinit var barcodeScannerView: BarcodeView
    private lateinit var tv_search: TextView
    private lateinit var txtSample: TextView
    private lateinit var etUserName: EditText
    private val CAMERA_PERMISSION_CODE = 100
    private var apiInterface: APIInterface? = null
    private var isApiCall : Boolean = false

    var scanString = ""
    var lastInputTime: Long = 0
    val SCAN_TIMEOUT = 2000L // 1 second timeout


    private fun disableEditTextFocus() {
        etUserName.clearFocus()
        etUserName.isFocusable = false
        etUserName.isFocusableInTouchMode = false
        etUserName.isCursorVisible = false
    }

    private fun enableEditTextFocus() {
        etUserName.isFocusable = true
        etUserName.isFocusableInTouchMode = true
        etUserName.isCursorVisible = true
        // etUserName.requestFocus()
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
                txtSample.text = scanString
                // scanString = "" // ✅ Clear after processing


                if (scanString != null) {
                    if (!isApiCall){
                        try {
                            isApiCall = true
                            var userID = Integer.parseInt(scanString, 10);
                            userLogin(userID)
                        }catch (e : Exception){
                            isApiCall = false
                            showMessage("Invalid QR")
                        }
                    }

                }else{
                    isApiCall = false
                    showMessage("Invalid QR")
                    enableEditTextFocus()
                }

                return true
            }

            val pressedKey = event.unicodeChar.toChar()
            if (pressedKey.code != 0) {
                disableEditTextFocus()
                etUserName.isFocusable = false
                etUserName.isCursorVisible = false
                scanString += pressedKey
                Log.e("ScanCode", "Scan so far: $scanString")
                return true
            }
        }

        return super.dispatchKeyEvent(event)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_scan)
        apiInterface = getClientLogin().create(APIInterface::class.java)
        ProgressDialogUtil.LoadingDialog(this)
        barcodeScannerView = findViewById(R.id.barcode_scanner)
        tv_search = findViewById(R.id.tv_search)
        etUserName = findViewById(R.id.etUserName)
        txtSample = findViewById(R.id.txtSample)
        checkCameraPermission()


        tv_search.setOnClickListener {
            var userName = etUserName.text.toString()
            if (userName!=null && !userName.equals("")){
                val intent = Intent(this, ManualInputActivity::class.java)
                intent.putExtra("key_username", userName)
                startActivity(intent)
            }else{
                showMessage("Enter Employee ID")
            }

        }

    }
    private fun startCamera() {
        barcodeScannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    val scannedValue = it.text
                    if (scannedValue != null) {
                        if (!isApiCall){
                            isApiCall = true
                            Log.e("ScanData",scannedValue)
                            var userName  =   scannedValue
                            try {
                                var userID = Integer.parseInt(userName, 10);
                                userLogin(userID)
                            }catch (e : Exception){
                                showMessage("Invalid QR")
                                isApiCall = false
                            }
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
        isApiCall =false
    }

    override fun onPause() {
        super.onPause()
        barcodeScannerView.pause()
    }

    private fun userLogin(userNm: Int) {
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
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            JSONObject(errorJson).optString("message", "Something went wrong")
                        } catch (e: Exception) {
                            "Something went wrong"
                        }
                        showMessage(errorMessage)
                        // nextActivity()
                        isApiCall =false
                    }

                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    call.cancel()
                    isApiCall =false
                    showMessage("Something went wrong")
                    ProgressDialogUtil.hide()

                }
            })
        } else {
            showMessage("No internet connection")
        }
    }

}