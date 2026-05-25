package com.example.iptvapp

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun LoginScreen(onConnect: (String, String) -> Unit) {
    var url by remember { mutableStateOf("") }
    var mac by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = url, onValueChange = { url = it }, label = { Text("Portal URL") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = mac, onValueChange = { mac = it }, label = { Text("MAC Address") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onConnect(url, mac) }) {
            Text("Connect")
        }
    }
}
