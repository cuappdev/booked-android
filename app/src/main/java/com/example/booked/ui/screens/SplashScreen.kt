package com.example.booked.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booked.R

@Composable
fun SplashScreen()
{
    Box(Modifier.fillMaxSize()
        .background(Color(0xFF3F4F44)))
    {
        Image(painter=painterResource(R.drawable.booked_logo),
            contentDescription="Booked Logo",
            modifier = Modifier.size(width=72.85.dp, height=78.45.dp)
                .align(Alignment.Center))

        Text(text="Booked",
            fontSize=20.sp,
            color = Color(0xFFFFFFFF),
            modifier= Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom=48.dp))
    }
}