package com.example.booked.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booked.R

@Composable
fun LoginScreen(onSignInClick: () -> Unit)
{
    Box(Modifier.fillMaxSize()
        .background(Color(0xFFFAF7F1)))
    {
        Image(painter=painterResource(R.drawable.logo_green),
            contentDescription="Booked Logo",
            modifier = Modifier.size(width=72.85.dp, height=78.45.dp)
                .align(Alignment.Center))
        Column(modifier = Modifier.align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(start=24.dp, end=24.dp, bottom=70.dp),
            horizontalAlignment=Alignment.CenterHorizontally
            )
        {
            Button(
                onClick = {onSignInClick()},
                modifier = Modifier.fillMaxWidth().height(height = 52.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F4F44),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal=12.dp, vertical=8.dp)
            )
            {
                Icon(
                    painter = painterResource(R.drawable.login_key),
                    contentDescription = "Sign in with NetID",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier=Modifier.width(12.dp))
                Text(
                    text = "Sign in with NetID",
                    fontSize = 18.sp
                )
            }
            Spacer(modifier=Modifier.height(32.dp))
            Image(painter=painterResource(R.drawable.appdev_logo),
                contentDescription="AppDev Logo",
                modifier = Modifier.size(width=101.dp, height=16.5.dp))
        }
    }
}

