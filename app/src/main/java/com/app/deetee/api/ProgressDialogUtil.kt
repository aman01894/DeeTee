package com.app.deetee.api

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.app.deetee.R

object ProgressDialogUtil {

    private var loadingView: View? = null
    private var lottieAnimationView: LottieAnimationView? = null


    fun LoadingDialog(activity: Activity) {
        val root = activity.findViewById<ViewGroup>(android.R.id.content)
        loadingView = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, root, false)

        // Position in center or modify as needed (e.g., bottom-right)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER // or Gravity.BOTTOM | Gravity.END for corner
        loadingView!!.layoutParams = params
        lottieAnimationView = loadingView!!.findViewById(R.id.lottieLoader)
        root.addView(loadingView)
        loadingView!!.visibility = View.GONE
    }

    fun show() {
        loadingView!!.visibility = View.VISIBLE
        lottieAnimationView?.playAnimation()
    }

    fun hide() {
        lottieAnimationView?.cancelAnimation()
        loadingView!!.visibility = View.GONE
    }
}