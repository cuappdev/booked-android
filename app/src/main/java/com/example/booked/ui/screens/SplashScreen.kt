package com.example.booked.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.booked.ui.theme.BookedTheme
import com.example.booked.ui.theme.SplashBackground
import com.example.booked.ui.theme.SplashText
import com.example.booked.R

@Composable
fun SplashScreen()
{
    Box(Modifier.fillMaxSize()
        .background(SplashBackground))
    {
        Image(painter=painterResource(R.drawable.booked_logo),
            contentDescription="Booked Logo",
            modifier = Modifier.size(width=72.85.dp, height=78.45.dp)
                .align(Alignment.Center))

        Text(text="Booked",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 21.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.sp
            ),
            color = SplashText,
            modifier= Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom=44.dp))
    }
}

@Preview(widthDp = 402, heightDp = 874, showBackground = true)
@Composable
private fun SplashScreenPreview() {
    BookedTheme {
        SplashScreen()
    }
}

@Preview(widthDp = 402, heightDp = 874, showBackground = true)
@Composable
private fun SplashScreenDarkPreview() {
    BookedTheme(darkTheme = true) {
        SplashScreen()
    }
}
