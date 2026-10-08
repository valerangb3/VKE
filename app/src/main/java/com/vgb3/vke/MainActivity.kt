package com.vgb3.vke

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgb3.vke.ui.theme.VKETheme
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current

                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        onNavigate = { text ->
                            val intent = Intent(context, SecondActivity::class.java)
                            intent.putExtra(SecondActivity.KEY_TEXT, text)
                            context.startActivity(intent)
                        },
                        onCall = { phone ->
                            if (phone.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.data = "tel:$phone".toUri()
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "Phone is empty", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onShare = { text ->
                            if (text.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_SEND)
                                intent.putExtra(Intent.EXTRA_TEXT, text)
                                intent.type = "text/plain"
                                val chooser = Intent.createChooser(intent, "Share with...")
                                context.startActivity(chooser)
                            } else {
                                Toast.makeText(context, "Text is empty", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    onNavigate: (text: String) -> Unit,
    onCall: (phone: String) -> Unit,
    onShare: (phone: String) -> Unit
) {

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var inputText by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                modifier = Modifier.width(220.dp),
                value = inputText,
                onValueChange = { inputText = it }
            )
            Button(
                onClick = {
                    onNavigate(inputText)
                }
            ) {
                Text("Open second Activity")
            }
        }
        var phoneNumber by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                modifier = Modifier.width(220.dp),
                value = phoneNumber,
                onValueChange = { phoneNumber = it }
            )
            Button(onClick = {
                onCall(phoneNumber)
            }) {
                Text("Call")
            }
        }
        var shareText by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                modifier = Modifier.width(220.dp),
                value = shareText,
                onValueChange = { shareText = it }
            )
            Button(onClick = {
                onShare(shareText)
            }) {
                Text("Share text")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VKETheme {
        //Greeting("Android")
    }
}