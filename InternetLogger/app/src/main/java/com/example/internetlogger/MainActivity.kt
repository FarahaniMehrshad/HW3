package com.example.internetlogger

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.internetlogger.ui.theme.InternetLoggerTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _connectionStatus = MutableStateFlow("Not Connected")
    val connectionStatus: StateFlow<String> = _connectionStatus

    fun updateConnectionStatus(status: String) {
        _connectionStatus.value = status
    }
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(application)
    }

    private val internetConnectionReceiver = InternetConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(
            internetConnectionReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        setContent {
            val connectionStatus by viewModel.connectionStatus.collectAsState()
            ConnectionStatusScreen(connectionStatus)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetConnectionReceiver)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ConnectionStatusScreen(connectionStatus: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Connection Status: $connectionStatus",
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InternetLoggerTheme {
        ConnectionStatusScreen(connectionStatus = "test")
    }
}