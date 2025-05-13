package com.app.deetee

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView

class StartProcessActivity : ComponentActivity() {

    private lateinit var rr_soView: RelativeLayout
    private lateinit var rrDriveRols: RelativeLayout
    private lateinit var rr_stageView: RelativeLayout
    private lateinit var rr_DriveRollView: RelativeLayout
    private lateinit var ll_nosView: LinearLayout
    private lateinit var view20: View
    private lateinit var rrStageRollView: RelativeLayout
    private lateinit var rr_NosRollView: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_start_process)


        rr_soView = findViewById(R.id.rr_soView)
        rrDriveRols = findViewById(R.id.rrDriveRols)
        rr_stageView = findViewById(R.id.rr_stageView)
        rr_DriveRollView = findViewById(R.id.rr_DriveRollView)
        view20 = findViewById(R.id.view20)
        ll_nosView = findViewById(R.id.ll_nosView)
        rrStageRollView = findViewById(R.id.rrStageRollView)
        rr_NosRollView = findViewById(R.id.rr_NosRollView)


        rr_soView.setOnClickListener {

            rr_stageView.visibility = View.VISIBLE
            rr_DriveRollView.visibility = View.GONE
            view20.visibility = View.GONE
            ll_nosView.visibility = View.GONE
            rr_NosRollView.visibility = View.GONE
            rrStageRollView.visibility = View.VISIBLE

        }

        rrDriveRols.setOnClickListener {

            rr_stageView.visibility = View.GONE
            rr_DriveRollView.visibility = View.VISIBLE
            view20.visibility = View.VISIBLE
            ll_nosView.visibility = View.VISIBLE
            rr_NosRollView.visibility = View.VISIBLE
            rrStageRollView.visibility = View.GONE
        }
        deleteAllRecordsDialog()
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
}