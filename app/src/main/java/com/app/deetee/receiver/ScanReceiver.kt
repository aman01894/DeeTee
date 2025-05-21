package com.app.deetee.receiver;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ScanReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val data = intent?.getStringExtra("scan_data") // Use actual key from DCode config
        if (!data.isNullOrEmpty()) {
            Toast.makeText(context,"Message comming!",Toast.LENGTH_SHORT).show()
        }
    }
}