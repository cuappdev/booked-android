package com.example.booked

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.booked.ui.screens.HomeScreen
import com.example.booked.ui.screens.LoginScreen
import com.example.booked.ui.theme.BookedTheme
import com.example.booked.ui.screens.SplashScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
@Composable
fun App()
{
    var showSplash by remember { mutableStateOf(true) }
    var showHome by remember { mutableStateOf(false)}
    LaunchedEffect(Unit) {
        delay(2000)
        showSplash=false
    }
    when
    {
        showSplash -> SplashScreen()
        showHome -> HomeScreen()
        else -> LoginScreen(onSignInClick= {showHome=true})
    }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookedTheme {
        Greeting("Android")
    }
}