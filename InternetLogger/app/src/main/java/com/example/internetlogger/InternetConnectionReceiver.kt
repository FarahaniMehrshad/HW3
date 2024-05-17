package com.example.internetlogger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class InternetConnectionReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val viewModel = ViewModelProvider(context as MainActivity, MainViewModelFactory(context.application))
            .get(MainViewModel::class.java)

        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val noConnectivity = intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            )
            if (noConnectivity) {
                viewModel.updateConnectionStatus(
                    "Not Connected"
                )
                println("no internet connection")
                // Show a Toast notification for no internet connection
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateConnectionStatus(
                    "Connected"
                )
                println("internet connection")
                // Show a Toast notification for internet connection available
                Toast.makeText(context, "Internet connection available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}