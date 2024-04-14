package com.example.fizjotherapy.prompt

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.Callable

class PromptService {

    fun setErrorTextView(errorView: TextView, text: String?, color: String?, visible: Int) {
        errorView.text = text
        errorView.setTextColor(Color.parseColor(color))
        errorView.visibility = visible
    }

    fun showSuccessToast(applicationContext: Context?, message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showFailToast(applicationContext: Context?, message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun buildAlertDialog(
        activity: Activity,
        message: String?,
        positiveAction: Callable<Void?>,
        negative: Callable<Void?>
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val alertDialog: AlertDialog = builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialogInterface, i ->
                try {
                    positiveAction.call()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
            .setNegativeButton("No") { dialogInterface, i ->
                try {
                    negative.call()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }.create()
        alertDialog.show()
    }

    companion object {
        val instance:PromptService by lazy {
            PromptService()
        }
    }
}