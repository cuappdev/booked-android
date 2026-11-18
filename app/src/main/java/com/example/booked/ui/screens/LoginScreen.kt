package com.example.booked.ui.screens

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.booked.R

@Composable
fun LoginScreen(onSignInClick: () -> Unit)
{
    Box(Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background))
    {
        Image(painter=painterResource(R.drawable.logo_green),
            contentDescription="Booked Logo",
            modifier = Modifier.size(width=72.85.dp, height=78.45.dp)
                .align(Alignment.Center), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary))
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
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
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier=Modifier.height(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.appdev_logo),
                    contentDescription = null,
                    modifier = Modifier.size(16.5.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Cornell")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("AppDev")
                        }
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

