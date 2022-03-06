package id.nns.nikos.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import id.nns.nikos.R

@SuppressLint("InflateParams")
class ProgressDialog(private val activity: Activity) {

    private lateinit var alertDialog: AlertDialog

    fun showLoading() {
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.progress_dialog, null)

        alertDialog = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setCancelable(false)
            .create()
            .apply {
                window?.setBackgroundDrawableResource(R.color.transparent)
                show()
            }
    }

    fun dismissLoading() {
        alertDialog.dismiss()
    }

}
